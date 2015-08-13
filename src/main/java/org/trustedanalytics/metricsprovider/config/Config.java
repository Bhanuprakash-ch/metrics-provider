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
package org.trustedanalytics.metricsprovider.config;

import static java.util.Collections.singletonList;

import org.trustedanalytics.cloud.auth.HeaderAddingHttpInterceptor;
import org.trustedanalytics.cloud.auth.OAuth2TokenRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
    @Value("${cf.resource:/}")
    private String apiBaseUrl;

    @Value("${datacatalog.url:/}")
    private String dataCatalogBaseUrl;

    @Value("${latest-events-service.url:/}")
    private String latestEventsServiceBaseUrl;

    @Autowired
    private OAuth2TokenRetriever tokenRetriever;

    @Value("${cf.connectTimeout}")
    int connectTimeout;

    @Value("${cf.readTimeout}")
    int readTimeout;

    @Bean
    protected String apiBaseUrl() {
        return apiBaseUrl;
    }

    @Bean
    protected String dataCatalogBaseUrl() {
        return dataCatalogBaseUrl;
    }

    @Bean
    protected String latestEventsServiceBaseUrl() {
        return latestEventsServiceBaseUrl;
    }

    @Bean
    protected int connectTimeout() {
        return connectTimeout;
    }

    @Bean
    protected int readTimeout() {
        return readTimeout;
    }

    protected RestOperations restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        OAuth2Authentication authentication = getAuthentication();
        String token = tokenRetriever.getAuthToken(authentication);
        ClientHttpRequestInterceptor interceptor =
            new HeaderAddingHttpInterceptor("Authorization", "bearer " + token);
        restTemplate.setInterceptors(singletonList(interceptor));
        return restTemplate;
    }

    protected OAuth2Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }

        return (OAuth2Authentication) context.getAuthentication();
    }
}
