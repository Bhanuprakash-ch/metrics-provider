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
package org.trustedanalytics.metricsprovider.cloudadapter.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CfServiceInstance {

    private UUID guid;

    private String name;

    @JsonProperty("service_plan")
    private CfServicePlan servicePlan;

    @JsonProperty("dashboard_url")
    @JsonInclude(Include.NON_NULL)
    private String dashboardUrl;
    
    public CfServiceInstance() {
    }

    public CfServiceInstance(UUID guid, String name, String servicePlanName, UUID serviceGuid) {
        this.guid = guid;
        this.name = name;

        CfServicePlan plan = new CfServicePlan();
        plan.setName(servicePlanName);
        CfService service = new CfService();
        service.setGuid(serviceGuid);
        plan.setService(service);

        this.servicePlan = plan;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CfServicePlan getServicePlan() {
        return servicePlan;
    }

    public void setServicePlan(CfServicePlan servicePlan) {
        this.servicePlan = servicePlan;
    }

    public String getDashboardUrl() {
        return dashboardUrl;
    }

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    @JsonIgnore
    public UUID getServiceGuid() {
        Optional<CfServiceInstance> serviceInstance = Optional.of(this);
        return serviceInstance.map(CfServiceInstance::getServicePlan).map(CfServicePlan::getService)
            .map(CfService::getGuid).orElse(null);
    }

    @JsonIgnore
    public String getServicePlanName() {
        Optional<CfServiceInstance> serviceInstance = Optional.of(this);
        return serviceInstance.map(CfServiceInstance::getServicePlan).map(CfServicePlan::getName)
            .orElse(null);
    }
}
