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

import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgQuota;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgSummary;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgUserList;
import org.trustedanalytics.metricsprovider.rest.SpaceMetrics;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TestDownloadTasks {
    public static <T> CompletableFuture<T> failedDownload() {
        return CompletableFuture.supplyAsync(() -> {
            throw new NotImplementedException();
        });
    }

    public static CompletableFuture<Long> correctPrivateDatasetCount() {
        return CompletableFuture.completedFuture(MetricsTestResources.testPrivateDatasetCount());
    }

    public static CompletableFuture<Long> correctPublicDatasetCount() {
        return CompletableFuture.completedFuture(MetricsTestResources.testPublicDatasetCount());
    }

    public static CompletableFuture<CfOrgQuota> correctOrgQuota() {
        return CompletableFuture.completedFuture(MetricsTestResources.testOrgQuota());
    }

    public static CompletableFuture<CfOrgSummary> correctOrgSummary() {
        return CompletableFuture.completedFuture(MetricsTestResources.testOrgSummary());
    }

    public static CompletableFuture<CfOrgUserList> correctOrgUserList() {
        return CompletableFuture.completedFuture(MetricsTestResources.testOrgUserList());
    }

    public static CompletableFuture<Collection<UUID>> correctSpacesGuids(UUID space1, UUID space2) {
        return CompletableFuture.completedFuture(Arrays.asList(space1, space2));
    }

    public static CompletableFuture<SpaceMetrics> correctSpace1Metrics() {
        return CompletableFuture.completedFuture(MetricsTestResources.testSpace1Metrics());
    }

    public static CompletableFuture<SpaceMetrics> correctSpace2Metrics() {
        return CompletableFuture.completedFuture(MetricsTestResources.testSpace2Metrics());
    }
}
