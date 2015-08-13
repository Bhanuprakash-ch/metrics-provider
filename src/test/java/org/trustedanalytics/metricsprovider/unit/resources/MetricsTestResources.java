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
package org.trustedanalytics.metricsprovider.unit.resources;

import org.trustedanalytics.metricsprovider.cloudadapter.api.CfApp;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfAppState;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfMetadata;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrg;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgEntity;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgQuota;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgQuotaEntity;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgSummary;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgSummaryEntry;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgUserList;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfSpace;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfSpacesList;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfSummary;
import org.trustedanalytics.metricsprovider.rest.*;

import java.util.*;

public class MetricsTestResources {

    private final static int memory = 120;
    private final static int memoryQuota = 1200;
    private final static int serviceCount = 16;
    private final static int serviceQuota = 50;
    private final static int throughput = 456;
    private final static int domainsUsage = 24;
    private final static int domainsQuota = 100;
    private final static double responseTime = 0.55;
    private final static SimpleFraction capacity = new SimpleFraction(45, 185);
    private final static long appsRunning = 20L;
    private final static long appsDown = 2L;
    private final static int totalUsers = 17;

    public static long testPrivateDatasetCount() {
        return 3L;
    }

    public static long testPublicDatasetCount() {
        return 9L;
    }

    public static long testDatasetCount() {
        return testPrivateDatasetCount() + testPublicDatasetCount();
    }

    public static CfOrgSummary testOrgSummary() {
        CfOrgSummaryEntry spaceSummary = new CfOrgSummaryEntry();
        spaceSummary.setMemory(memory);

        CfOrgSummary orgSummary = new CfOrgSummary();
        orgSummary.setSpaces(Collections.singletonList(spaceSummary));
        return orgSummary;
    }

    public static CfOrgUserList testOrgUserList() {
        CfOrgUserList orgUserList = new CfOrgUserList();
        orgUserList.setTotalUsers(totalUsers);
        return orgUserList;
    }

    public static CfOrg testOrg() {
        CfOrgQuota orgQuota = testOrgQuota();

        CfOrgEntity orgEntity = new CfOrgEntity();
        orgEntity.setQuota(orgQuota);

        CfOrg org = new CfOrg();
        org.setEntity(orgEntity);
        return org;
    }

    public static EventSummary testEvents() {
        EventSummary eventSummary = new EventSummary();
        eventSummary.setTotal(10);
        EventInfo eventInfo = new EventInfo("id", "sourceId", "sourceName", 123, "category", "message");
        List<EventInfo> eventsList = new ArrayList<EventInfo>();
        eventsList.add(eventInfo);
        eventSummary.setEvents(eventsList);
        return eventSummary;
    }

    public static CfOrgQuota testOrgQuota() {
        CfOrgQuotaEntity orgQuotaEntity = new CfOrgQuotaEntity();
        orgQuotaEntity.setMemoryQuota(memoryQuota);
        orgQuotaEntity.setRoutesQuota(domainsQuota);
        orgQuotaEntity.setServicesQuota(serviceQuota);

        CfOrgQuota orgQuota = new CfOrgQuota();
        orgQuota.setEntity(orgQuotaEntity);
        return orgQuota;
    }

    public static CfSpacesList testSpacesList() {
        CfMetadata testSpaceMetadata = new CfMetadata();
        testSpaceMetadata.setGuid(UUID.fromString("35ff2e18-e4a8-4d7c-bb32-7aa254d0a4fa"));

        CfSpace space = new CfSpace();
        space.setMetadata(testSpaceMetadata);

        CfSpacesList spacesList = new CfSpacesList();
        spacesList.setSpaces(Collections.singletonList(space));
        return spacesList;
    }

    public static CfSummary testSummary() {
        CfSummary summary = new CfSummary();

        summary.setServiceInstances(Arrays.asList(null, null, null, null, null));

        summary.setApps(Arrays.asList(
            new CfApp(UUID.fromString("768d17f6-725e-4a1b-a445-c5a30f5a7780"),
                Collections.singletonList("sample"), 0, null, null, CfAppState.STARTED),
            new CfApp(UUID.fromString("7c3b999b-23d1-45ec-ad2e-59cda7ee1487"),
                Collections.singletonList("sample"), 0, null, null, CfAppState.STARTED),
            new CfApp(UUID.fromString("c3d806f0-bfaf-4e2c-842f-5d06243df71b"),
                Arrays.asList("a", "b"), 0, null, null, CfAppState.STOPPED)
        ));

        return summary;
    }

    private static SpaceMetrics testSpaceMetrics() {
        return new SpaceMetrics(appsRunning / 2, appsDown / 2, domainsUsage / 2, serviceCount / 2);
    }

    public static SpaceMetrics testSpace1Metrics() {
        return testSpaceMetrics();
    }

    public static SpaceMetrics testSpace2Metrics() {
        return testSpaceMetrics();
    }

    public static Map<String, Object> allDownloadsOK() {
        HashMap<String, Object> expectedMetrics = new HashMap<>();
        expectedMetrics.put(MetricsSchema.DATASET_COUNT, testDatasetCount());
        expectedMetrics.put(MetricsSchema.SERVICE_USAGE_PERCENT, new SimpleFraction(serviceCount, serviceQuota));
        expectedMetrics.put(MetricsSchema.MEMORY_USAGE_ABSOLUTE, memory);
        expectedMetrics.put(MetricsSchema.MEMORY_USAGE, new SimpleFraction(memory, memoryQuota));
        expectedMetrics.put(MetricsSchema.SERVICE_USAGE, serviceCount);
        expectedMetrics.put(MetricsSchema.APPS_RUNNING, appsRunning);
        expectedMetrics.put(MetricsSchema.DOMAINS_USAGE_PERCENT, new SimpleFraction(domainsUsage, domainsQuota));
        expectedMetrics.put(MetricsSchema.APPS_DOWN, appsDown);
        expectedMetrics.put(MetricsSchema.PUBLIC_DATASETS, testPublicDatasetCount());
        expectedMetrics.put(MetricsSchema.DOMAINS_USAGE, domainsUsage);
        expectedMetrics.put(MetricsSchema.PRIVATE_DATASETS, testPrivateDatasetCount());
        expectedMetrics.put(MetricsSchema.TOTAL_USERS, totalUsers);
        return expectedMetrics;
    }

    private static TestMetricsBuilder expectedMetricsBuilder() {
        return new TestMetricsBuilder(allDownloadsOK());
    }

    public static Map<String, Object> withoutDataset() {
        return MetricsTestResources.expectedMetricsBuilder()
            .without(MetricsSchema.DATASET_COUNT)
            .without(MetricsSchema.PUBLIC_DATASETS)
            .without(MetricsSchema.PRIVATE_DATASETS)
            .build();
    }

    public static Map<String, Object> withoutUserList() {
        return MetricsTestResources.expectedMetricsBuilder()
                .without(MetricsSchema.TOTAL_USERS)
                .build();
    }

    public static Map<String, Object> withoutQuotaRelated() {
        return MetricsTestResources.expectedMetricsBuilder()
            .without(MetricsSchema.SERVICE_USAGE_PERCENT)
            .without(MetricsSchema.MEMORY_USAGE)
            .without(MetricsSchema.DOMAINS_USAGE_PERCENT)
            .build();
    }

    public static Map<String, Object> metricsWithoutMemory() {
        return MetricsTestResources.expectedMetricsBuilder()
            .without(MetricsSchema.MEMORY_USAGE_ABSOLUTE)
            .without(MetricsSchema.MEMORY_USAGE)
            .build();
    }

    public static Map<String, Object> metricsWithoutSpaceRelated() {
        return MetricsTestResources.expectedMetricsBuilder()
            .without(MetricsSchema.SERVICE_USAGE_PERCENT)
            .without(MetricsSchema.SERVICE_USAGE)
            .without(MetricsSchema.APPS_RUNNING)
            .without(MetricsSchema.DOMAINS_USAGE_PERCENT)
            .without(MetricsSchema.APPS_DOWN)
            .without(MetricsSchema.DOMAINS_USAGE)
            .build();
    }

    private static class TestMetricsBuilder {
        private Map<String, Object> metrics;

        public TestMetricsBuilder(Map<String, Object> metrics) {
            this.metrics = metrics;
        }

        public TestMetricsBuilder without(String key) {
            metrics.remove(key);
            return this;
        }

        public Map<String, Object> build() {
            return metrics;
        }
    }
}
