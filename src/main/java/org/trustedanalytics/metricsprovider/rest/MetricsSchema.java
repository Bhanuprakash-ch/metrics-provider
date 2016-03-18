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

/**
 * Contains all the JSON fields returned by Metrics Service.
 */
public final class MetricsSchema {

    // DatasetMetricsCollector
    public static final String DATASET_COUNT = "datasetCount";
    public static final String PUBLIC_DATASETS = "publicDatasets";
    public static final String PRIVATE_DATASETS = "privateDatasets";

    // OrgMetricsCollector
    public static final String MEMORY_USAGE_ABSOLUTE = "memoryUsageAbsolute";
    public static final String MEMORY_USAGE = "memoryUsage";

    // OrgUserMetricsCollector
    public static final String TOTAL_USERS = "totalUsers";

    // SpaceMEtricsCollector
    public static final String APPS_RUNNING = "appsRunning";
    public static final String APPS_DOWN = "appsDown";
    public static final String DOMAINS_USAGE = "domainsUsage";
    public static final String SERVICE_USAGE = "serviceUsage";
    public static final String DOMAINS_USAGE_PERCENT = "domainsUsagePercent";
    public static final String SERVICE_USAGE_PERCENT = "serviceUsagePercent";

    // Events
    public static final String LATEST_EVENTS = "latestEvents";

    private MetricsSchema() {
    }
}
