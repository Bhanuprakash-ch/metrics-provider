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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CfApp {

    private UUID guid;

    private List<String> urls;

    @JsonProperty("running_instances")
    private int runningInstances;

    @JsonProperty("service_names")
    private List<String> serviceNames;

    private String name;

    private CfAppState state;

    public CfApp() {
    }

    public CfApp(UUID guid, List<String> urls, int runningInstances, List<String> serviceNames,
        String name, CfAppState state) {
        this.guid = guid;
        this.urls = urls;
        this.runningInstances = runningInstances;
        this.serviceNames = serviceNames;
        this.name = name;
        this.state = state;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public int getRunningInstances() {
        return runningInstances;
    }

    public void setRunningInstances(int runningInstances) {
        this.runningInstances = runningInstances;
    }

    public List<String> getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(List<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CfAppState getState() {
        return state;
    }

    public void setState(CfAppState state) {
        this.state = state;
    }

    public boolean isStarted() {
        return state == CfAppState.STARTED;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CfApp cfApp = (CfApp) o;

        return interStateEquals(cfApp);
    }

    @Override
    public int hashCode() {
        int result = guid != null ? guid.hashCode() : 0;
        result = 31 * result + (urls != null ? urls.hashCode() : 0);
        result = 31 * result + runningInstances;
        result = 31 * result + (serviceNames != null ? serviceNames.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    private boolean interStateEquals(CfApp cfApp) {
        if (!fieldEquals(runningInstances, cfApp.runningInstances) ||
            !fieldEquals(guid, cfApp.guid) ||
            !fieldEquals(name, cfApp.name)) {
            return false;
        }

        if(!fieldEquals(serviceNames, cfApp.serviceNames) ||
            !fieldEquals(state, cfApp.state) ||
            !fieldEquals(urls, cfApp.urls)) {
            return false;
        }
        return true;
    }

    private boolean fieldEquals(Object one, Object two) {
        if(one != null) {
            return one.equals(two);
        }
        return two == null;
    }

}
