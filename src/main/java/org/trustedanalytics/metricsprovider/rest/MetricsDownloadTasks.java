/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trustedanalytics.metricsprovider.rest;

import static java.util.stream.Collectors.partitioningBy;
import static net.javacrumbs.futureconverter.springjava.FutureConverter.toCompletableFuture;

import org.trustedanalytics.cloud.auth.AuthTokenRetriever;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfApp;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrg;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgQuota;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgSummary;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgUserList;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfSpace;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfSpacesList;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.client.AsyncRestOperations;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MetricsDownloadTasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsDownloadTasks.class);

    private final String apiBaseUrl;
    private final String dataCatalogBaseUrl;
    private final String latestEventsService;
    private final AsyncRestOperations asyncTemplate;
    private final HttpEntity<String> requestEntity;

    public MetricsDownloadTasks(String apiBaseUrl, String dataCatalogBaseUrl, String latestEventsService,
        AuthTokenRetriever tokenRetriever, AsyncRestOperations asyncTemplate) {

        this.apiBaseUrl = apiBaseUrl;
        this.dataCatalogBaseUrl = dataCatalogBaseUrl;
        this.asyncTemplate = asyncTemplate;
        this.latestEventsService = latestEventsService;
        this.requestEntity = setAuthToken(tokenRetriever);
    }

    private HttpEntity<String> setAuthToken(AuthTokenRetriever tokenRetriever) {
        SecurityContext context = SecurityContextHolder.getContext();
        OAuth2Authentication auth = (OAuth2Authentication) context.getAuthentication();
        String token = tokenRetriever.getAuthToken(auth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "bearer " + token);
        return new HttpEntity<>("params", headers);
    }

    public CompletableFuture<Collection<UUID>> getSpacesGuids(UUID org) {
        String path = apiBaseUrl + "/v2/organizations/{org}/spaces";
        return getAsynchronouslyWithLogging(path, CfSpacesList.class, org,
            "getSpacesGuids, org=" + org)
            .thenApply(s ->
                s.getSpaces().stream().map(CfSpace::getGuid).collect(Collectors.toList()));
    }

    public CompletableFuture<SpaceMetrics> getSpaceMetricsSingle(UUID space) {

        return getSpaceSummary(space)
                .thenApply(
                    response -> {

                        Map<Boolean, List<CfApp>> apps =
                            response.getApps().stream().collect(partitioningBy(CfApp::isStarted));

                        int routesCount =
                            response.getApps().stream().map(app -> app.getUrls().size())
                                .collect(Collectors.summingInt(i -> i));

                        int servicesCount = response.getServiceInstances().size();

                        return new SpaceMetrics(apps.get(true).size(), apps.get(false).size(),
                            routesCount, servicesCount);
                    });
    }

    private CompletableFuture<CfSummary> getSpaceSummary(UUID space) {
        String path = apiBaseUrl + "/v2/spaces/{space}/summary?inline-relations-depth=1";
        return getAsynchronouslyWithLogging(path, CfSummary.class, space,
            "getSpaceSummary, space=" + space);
    }

    public CompletableFuture<CfOrgSummary> getOrgSummary(UUID org) {
        String path = apiBaseUrl + "/v2/organizations/{org}/summary";
        return getAsynchronouslyWithLogging(path, CfOrgSummary.class, org,
            "getOrgSummary, org=" + org);
    }

    public CompletableFuture<CfOrgQuota> getOrgQuota(UUID org) {
        String path = apiBaseUrl + "/v2/organizations/{org}?inline-relations-depth=1";
        return getAsynchronouslyWithLogging(path, CfOrg.class, org, "getOrgQuota, org=" + org)
            .thenApply(o -> o.getEntity().getQuota());
    }

    public CompletableFuture<CfOrgUserList> getOrgUserList(UUID org) {
        String path = apiBaseUrl + "/v2/organizations/{org}/users";
        return getAsynchronouslyWithLogging(path, CfOrgUserList.class, org, "getOrgUserList, org=" + org);
    }

    public CompletableFuture<Long> getPrivateDatasetCount(UUID org) {
        String path =
            dataCatalogBaseUrl + "/rest/datasets/count?onlyPrivate=true&orgs=" + org.toString();
        return getAsynchronouslyWithLogging(path, Long.class, org,
            "getPrivateDatasetCount, org=" + org);
    }

    public CompletableFuture<Long> getPublicDatasetCount(UUID org) {
        String path =
            dataCatalogBaseUrl + "/rest/datasets/count?onlyPublic=true&orgs=" + org.toString();
        return getAsynchronouslyWithLogging(path, Long.class, org,
            "getPublicDatasetCount, org=" + org);
    }

    public CompletableFuture<EventSummary> getLatestEvents() {
        String path = latestEventsService + "/rest/les/events?size=10";
        return getAsynchronouslyWithLogging(path, EventSummary.class, null, "getting last events");
    }

    private <T> CompletableFuture<T> getAsynchronouslyWithLogging(String path,
        Class<T> responseType, Object uriVars, String logMessage) {

        LOGGER.debug("STARTasync " + logMessage);

        CompletableFuture<T> future = toCompletableFuture(
            asyncTemplate.exchange(path, HttpMethod.GET, requestEntity, responseType, uriVars)
        ).thenApply(response -> {
            LOGGER.debug("STOP " + logMessage);
            return response.getBody();
        });

        LOGGER.debug("STOPasync " + logMessage);

        return future;
    }
}
