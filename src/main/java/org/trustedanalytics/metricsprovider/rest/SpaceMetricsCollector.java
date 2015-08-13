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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SpaceMetricsCollector {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrgMetricsCollector.class);

    public static void collect(final Map<String, Object> metricsMap,
            final CompletableFuture<List<SpaceMetrics>> appsMetricsFuture,
            final CompletableFuture<CfOrgQuota> orgQuotaFuture) {

        List<SpaceMetrics> spaceMetrics;

        try {
            spaceMetrics = appsMetricsFuture.get();
        } catch (Exception ex) {
            LOGGER.error("Error: ", ex);
            return;
        }

        SpaceMetrics cumulatedSpaceMetrics = putSpaceMetrics(metricsMap, spaceMetrics);

        tryPutPercentageSpaceMetricsOrLogError(metricsMap, cumulatedSpaceMetrics, orgQuotaFuture);
    }

    private static SpaceMetrics putSpaceMetrics(final Map<String, Object> metricsMap,
            final List<SpaceMetrics> spaceMetrics) {

        Long appsRunning = spaceMetrics.stream().collect(Collectors.summingLong(SpaceMetrics::getAppsRunning));
        Long appsDown = spaceMetrics.stream().collect(Collectors.summingLong(SpaceMetrics::getAppsDown));
        Integer routesCount = spaceMetrics.stream().collect(Collectors.summingInt(SpaceMetrics::getRouteCount));
        Integer servicesCount = spaceMetrics.stream().collect(Collectors.summingInt(SpaceMetrics::getServiceCount));

        metricsMap.put(MetricsSchema.APPS_RUNNING, appsRunning);
        metricsMap.put(MetricsSchema.APPS_DOWN, appsDown);
        metricsMap.put(MetricsSchema.DOMAINS_USAGE, routesCount);
        metricsMap.put(MetricsSchema.SERVICE_USAGE, servicesCount);

        return new SpaceMetrics(appsRunning, appsDown, routesCount, servicesCount);
    }

    private static void tryPutPercentageSpaceMetricsOrLogError(final Map<String, Object> metricsMap,
            final SpaceMetrics summedSpaceMetrics, final CompletableFuture<CfOrgQuota> orgQuotaFuture) {

        CfOrgQuotaEntity quota;
        try {
            quota = orgQuotaFuture.get().getEntity();
        } catch (Exception ex) {
            LOGGER.error("Error: ", ex);
            return;
        }

        metricsMap.put(MetricsSchema.DOMAINS_USAGE_PERCENT,
                new SimpleFraction(summedSpaceMetrics.getRouteCount(), quota.getRoutesQuota()));
        metricsMap.put(MetricsSchema.SERVICE_USAGE_PERCENT,
                new SimpleFraction(summedSpaceMetrics.getServiceCount(), quota.getServicesQuota()));
    }
}
