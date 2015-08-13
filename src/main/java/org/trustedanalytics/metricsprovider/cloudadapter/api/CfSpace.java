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

import java.util.Optional;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CfSpace {

    private CfMetadata metadata;
    private CfSpaceEntity entity;

    public CfSpace() {
    }

    public CfSpace(UUID guid, String name, UUID orgGuid) {
        CfMetadata meta = new CfMetadata();
        meta.setGuid(guid);
        metadata = meta;
        CfSpaceEntity ent = new CfSpaceEntity();
        ent.setName(name);
        ent.setOrgGuid(orgGuid);
        entity = ent;
    }

    public CfMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(CfMetadata metadata) {
        this.metadata = metadata;
    }

    public CfSpaceEntity getEntity() {
        return entity;
    }

    public void setEntity(CfSpaceEntity entity) {
        this.entity = entity;
    }

    @JsonIgnore
    public UUID getGuid() {
        Optional<CfSpace> space = Optional.of(this);
        return space.map(CfSpace::getMetadata).map(CfMetadata::getGuid).orElse(null);
    }

    @JsonIgnore
    public String getName() {
        Optional<CfSpace> space = Optional.of(this);
        return space.map(CfSpace::getEntity).map(CfSpaceEntity::getName).orElse(null);
    }

    @JsonIgnore
    public UUID getOrgGuid() {
        Optional<CfSpace> space = Optional.of(this);
        return space.map(CfSpace::getEntity).map(CfSpaceEntity::getOrgGuid).orElse(null);
    }
}
