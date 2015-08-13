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
package org.trustedanalytics.metricsprovider.integrationtests.tests;

import static org.trustedanalytics.metricsprovider.unit.resources.MetricsTestResources.testDatasetCount;
import static org.trustedanalytics.metricsprovider.unit.resources.MetricsTestResources.testOrg;
import static org.trustedanalytics.metricsprovider.unit.resources.MetricsTestResources.testOrgSummary;
import static org.trustedanalytics.metricsprovider.unit.resources.MetricsTestResources.testOrgUserList;
import static org.trustedanalytics.metricsprovider.unit.resources.MetricsTestResources.testSpacesList;
import static org.trustedanalytics.metricsprovider.unit.resources.MetricsTestResources.testSummary;
import static org.trustedanalytics.metricsprovider.unit.resources.MetricsTestResources.testEvents;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.trustedanalytics.cloud.auth.AuthTokenRetriever;
import org.trustedanalytics.metricsprovider.Application;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrg;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgSummary;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfOrgUserList;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfSpacesList;
import org.trustedanalytics.metricsprovider.cloudadapter.api.CfSummary;
import org.trustedanalytics.metricsprovider.rest.EventSummary;
import org.trustedanalytics.metricsprovider.rest.MetricsController;
import org.trustedanalytics.metricsprovider.rest.MetricsSchema;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.AsyncRestOperations;
import org.trustedanalytics.metricsprovider.integrationtests.utils.RestMockBuilder;
import org.trustedanalytics.metricsprovider.integrationtests.utils.RestOperationsHelpers;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("integration-test")
public class MetricsIT {

    @Value("http://localhost:${local.server.port}")
    private String BASE_URL;

    private final String TOKEN = "jhksdf8723kjhdfsh4i187y91hkajl";

    @Autowired
    private AuthTokenRetriever tokenRetriever;

    @Autowired
    private AsyncRestOperations asyncRestOperations;

    @Before
    public void setUp() {
        when(tokenRetriever.getAuthToken(any(Authentication.class))).thenReturn(TOKEN);

        RestMockBuilder.whenAsk(asyncRestOperations)
            .forA(CfSpacesList.class)
            .thenReturn(testSpacesList());

        RestMockBuilder.whenAsk(asyncRestOperations)
            .forA(CfSummary.class)
            .thenReturn(testSummary());

        RestMockBuilder.whenAsk(asyncRestOperations)
            .forA(Long.class)
            .thenReturn(testDatasetCount());

        RestMockBuilder.whenAsk(asyncRestOperations)
            .forA(CfOrgSummary.class)
            .thenReturn(testOrgSummary());

        RestMockBuilder.whenAsk(asyncRestOperations)
                .forA(CfOrgUserList.class)
                .thenReturn(testOrgUserList());

        RestMockBuilder.whenAsk(asyncRestOperations)
            .forA(CfOrg.class)
            .thenReturn(testOrg());

        RestMockBuilder.whenAsk(asyncRestOperations)
            .forA(EventSummary.class)
            .thenReturn(testEvents());
    }

    @Test
    public void callMetricsEndpoint_orgSpecified_shouldReturnOrgMetrics() {

        String URL = BASE_URL + MetricsController.GET_APPS_METRICS_ASYNC;

        String ORG = "290f0c0e-3d2e-4222-b7aa-dc1b4870faec";
        ImmutableMap<String, Object> pathVars = ImmutableMap.of("org", ORG);

        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<String> response =
            RestOperationsHelpers.getForEntityWithToken(testRestTemplate, TOKEN, URL, String.class, pathVars);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(
            // @formatter:off
            "{" +
                "\"" + MetricsSchema.PRIVATE_DATASETS + "\":12," +
                "\"" + MetricsSchema.DATASET_COUNT + "\":24," +
                "\"" + MetricsSchema.SERVICE_USAGE_PERCENT + "\":{" +
                    "\"numerator\":5," +
                    "\"denominator\":50" +
                "}," +
                "\"" + MetricsSchema.MEMORY_USAGE_ABSOLUTE + "\":120," +
                "\"" + MetricsSchema.MEMORY_USAGE + "\":{" +
                    "\"numerator\":120," +
                    "\"denominator\":1200" +
                "}," +
                "\"" + MetricsSchema.TOTAL_USERS + "\":17," +
                "\"" + MetricsSchema.SERVICE_USAGE + "\":5," +
                "\"" + MetricsSchema.APPS_RUNNING + "\":2," +
                "\"" + MetricsSchema.LATEST_EVENTS + "\":[{\"id\":\"id\",\"sourceId\":\"sourceId\"," +
                    "\"sourceName\":\"sourceName\",\"timestamp\":123,\"category\":\"category\"," +
                    "\"message\":\"message\"}]," +
                "\"" + MetricsSchema.DOMAINS_USAGE_PERCENT + "\":{" +
                    "\"numerator\":4," +
                    "\"denominator\":100" +
                "}," +
                "\"" + MetricsSchema.APPS_DOWN + "\":1," +
                "\"" + MetricsSchema.PUBLIC_DATASETS + "\":12," +
                "\"" + MetricsSchema.DOMAINS_USAGE + "\":4" +
            "}"
            ));
            // @formatter:on
    }
}
