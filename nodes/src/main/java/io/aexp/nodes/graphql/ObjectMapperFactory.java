/*
 * Copyright (c) 2018 American Express Travel Related Services Company, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.aexp.nodes.graphql;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMapperFactory {

    public static ObjectMapper newSerializerObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // JDK8+ Time and Date handling / JSR-310
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
        mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

        return mapper;
    }

    public static ObjectMapper newDeserializerObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // JDK8+ Time and Date handling / JSR-310
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS); 

        return mapper;
    }
}
