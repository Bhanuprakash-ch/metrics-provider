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

import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgUserList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class OrgUserMetricsCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrgUserMetricsCollector.class);

    public static void collect(final Map<String, Object> metricsMap, final CompletableFuture<CfOrgUserList> orgUserCountFuture) {
        try {
            metricsMap.put(MetricsSchema.TOTAL_USERS, orgUserCountFuture.get().getTotalUsers());
        } catch (Exception ex) {
            LOGGER.error("Error: ", ex);
        }
    }
}
