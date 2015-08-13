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

public class SpaceMetrics {
    long appsRunning;
    long appsDown;
    int routeCount;
    int serviceCount;

    public SpaceMetrics(long appsRunning, long appsDown, int routeCount, int serviceCount) {
        this.appsRunning = appsRunning;
        this.appsDown = appsDown;
        this.routeCount = routeCount;
        this.serviceCount = serviceCount;
    }

    public void setAppsRunning(long appsRunning) {
        this.appsRunning = appsRunning;
    }

    public void setAppsDown(long appsDown) {
        this.appsDown = appsDown;
    }

    public void setRouteCount(int routeCount) {
        this.routeCount = routeCount;
    }

    public void setServiceCount(int serviceCount) {
        this.serviceCount = serviceCount;
    }

    public long getAppsRunning() {
        return appsRunning;
    }

    public long getAppsDown() {
        return appsDown;
    }

    public int getRouteCount() {
        return routeCount;
    }

    public int getServiceCount() {
        return serviceCount;
    }
}
