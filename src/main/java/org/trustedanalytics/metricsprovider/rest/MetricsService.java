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

import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgQuota;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgSummary;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgUserList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MetricsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsService.class);

    private final MetricsDownloadTasks tasks;

    public MetricsService(MetricsDownloadTasks tasks) {
        this.tasks = tasks;
    }

    private List<SpaceMetrics> getSpaceMetrics(Collection<UUID> spaces) {
        List<CompletableFuture<SpaceMetrics>> futures = spaces.stream()
            .map(tasks::getSpaceMetricsSingle)
            .collect(Collectors.toList());

        return futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
    }

    public Map<String, Object> collectMetrics(UUID org) {

        CompletableFuture<List<SpaceMetrics>> appsMetricsFuture =
            tasks.getSpacesGuids(org).thenApply(this::getSpaceMetrics);

        CompletableFuture<Long> publicDatasetCount = tasks.getPublicDatasetCount(org);

        CompletableFuture<Long> privateDatasetCount = tasks.getPrivateDatasetCount(org);

        CompletableFuture<CfOrgSummary> orgSummaryFuture = tasks.getOrgSummary(org);

        CompletableFuture<CfOrgQuota> orgQuotaFuture = tasks.getOrgQuota(org);

        CompletableFuture<CfOrgUserList> orgUserCountFuture = tasks.getOrgUserList(org);

        CompletableFuture<EventSummary> latestEvents = tasks.getLatestEvents(org);

        LOGGER.debug("futures created for getting '{}' metrics", org);

        return new OrgMetricsBuilder()
            .collectDatasetMetrics(publicDatasetCount, privateDatasetCount)
            .collectSpaceMetrics(appsMetricsFuture, orgQuotaFuture)
            .collectOrgMetrics(orgSummaryFuture, orgQuotaFuture)
            .collectOrgUserMetrics(orgUserCountFuture)
            .collectLatestEvents(latestEvents)
            .build();
    }
}
