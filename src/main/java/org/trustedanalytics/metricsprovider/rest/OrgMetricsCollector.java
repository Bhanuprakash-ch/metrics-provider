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
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgQuotaEntity;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgSummary;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgSummaryEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class OrgMetricsCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrgMetricsCollector.class);

    public static void collect(final Map<String, Object> metricsMap, final CompletableFuture<CfOrgSummary> orgSummaryFuture,
            final CompletableFuture<CfOrgQuota> orgQuotaFuture) {

        CfOrgSummary orgSummary;

        try {
            orgSummary = orgSummaryFuture.get();
        } catch (Exception ex) {
            LOGGER.error("Error: ", ex);
            return;
        }

        int memory = putMemoryUsageAbsolute(metricsMap, orgSummary);

        tryPutPercentageMemoryMetricsOrLogError(metricsMap, memory, orgQuotaFuture);
    }

    private static int putMemoryUsageAbsolute(final Map<String, Object> metricsMap, final CfOrgSummary orgSummary) {
        int memory = orgSummary.getSpaces().stream().map(CfOrgSummaryEntry::getMemory)
            .collect(Collectors.summingInt(i -> i));

        metricsMap.put(MetricsSchema.MEMORY_USAGE_ABSOLUTE, memory);
        return memory;
    }

    private static void tryPutPercentageMemoryMetricsOrLogError(final Map<String, Object> metricsMap, final int memory,
            final CompletableFuture<CfOrgQuota> orgQuotaFuture) {

        CfOrgQuotaEntity quota;
        try {
            quota = orgQuotaFuture.get().getEntity();
        } catch (Exception ex) {
            LOGGER.error("Error: ", ex);
            return;
        }

        metricsMap.put(MetricsSchema.MEMORY_USAGE, new SimpleFraction(memory, quota.getMemoryQuota()));
    }
}
