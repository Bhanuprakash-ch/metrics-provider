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

import org.trustedanalytics.cloud.auth.AuthTokenRetriever;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestOperations;

import java.util.Map;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class MetricsController {

    public static final String GET_APPS_METRICS_ASYNC = "/rest/orgs/{org}/metrics";

    @Autowired
    private String apiBaseUrl;

    @Autowired
    private String dataCatalogBaseUrl;

    @Autowired
    private String latestEventsServiceBaseUrl;

    @Autowired
    private AuthTokenRetriever tokenRetriever;

    @Autowired
    private AsyncRestOperations asyncTemplate;

    @ApiOperation("Get organization metrics")
    @RequestMapping(value = GET_APPS_METRICS_ASYNC, method = GET, produces = APPLICATION_JSON_VALUE)
    public Map<String, Object> getOrgMetrics(@PathVariable UUID org) {
        MetricsDownloadTasks metricsDownloadTasks =
            new MetricsDownloadTasks(apiBaseUrl, dataCatalogBaseUrl, latestEventsServiceBaseUrl, tokenRetriever, asyncTemplate);
        return new MetricsService(metricsDownloadTasks).collectMetrics(org);
    }
}
