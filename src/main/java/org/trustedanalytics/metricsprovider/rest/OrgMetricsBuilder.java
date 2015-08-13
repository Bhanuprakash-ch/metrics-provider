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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class OrgMetricsBuilder {

    private final Map<String, Object> orgMetrics;

    public OrgMetricsBuilder() {
        this.orgMetrics = new HashMap<>();
    }

    public Map<String, Object> build() {
        return orgMetrics;
    }

    public OrgMetricsBuilder collectDatasetMetrics(CompletableFuture<Long> publicDatasetCountFuture,
        CompletableFuture<Long> privateDatasetCountFuture) {
        DatasetMetricsCollector.collect(orgMetrics, publicDatasetCountFuture, privateDatasetCountFuture);

        return this;
    }

    public OrgMetricsBuilder collectSpaceMetrics(
            CompletableFuture<List<SpaceMetrics>> appsMetricsFuture,
            CompletableFuture<CfOrgQuota> orgQuotaFuture) {

        SpaceMetricsCollector.collect(orgMetrics, appsMetricsFuture, orgQuotaFuture);

        return this;
    }

    public OrgMetricsBuilder collectOrgMetrics(CompletableFuture<CfOrgSummary> orgSummaryFuture,
            CompletableFuture<CfOrgQuota> orgQuotaFuture) {

        OrgMetricsCollector.collect(orgMetrics, orgSummaryFuture, orgQuotaFuture);

        return this;
    }

    public OrgMetricsBuilder collectOrgUserMetrics(CompletableFuture<CfOrgUserList> orgUserListFuture) {
        OrgUserMetricsCollector.collect(orgMetrics, orgUserListFuture);

        return this;
    }

    public OrgMetricsBuilder collectLatestEvents(CompletableFuture<EventSummary> latestEvents) {
        LatestEventsCollector.collect(orgMetrics, latestEvents);

        return this;
    }
}
