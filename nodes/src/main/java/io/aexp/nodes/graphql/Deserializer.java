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

import io.aexp.nodes.graphql.annotations.GraphQLProperty;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

final class Deserializer<T> extends JsonDeserializer<Resource<T>> {

    private final Class<T> t;
    private final ObjectMapperFactory objectMapperFactory;

    Deserializer(Class<T> type, ObjectMapperFactory objectMapperFactory) {
        t = type;
        this.objectMapperFactory = objectMapperFactory;
    }

    @Override
    public Resource<T> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        Resource<T> resourceModel = new Resource<T>();

        ObjectMapper mapper = objectMapperFactory.newDeserializerMapper();
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        T resource;

        GraphQLProperty graphQLProperty = t.getAnnotation(GraphQLProperty.class);
        if (graphQLProperty != null) {
            String resourceName = graphQLProperty.name();
            JsonNode innerNode = node.get(resourceName);
            resource = mapper.treeToValue(innerNode, t);
        } else {
            resource = mapper.readValue(node.toString(), t);
        }

        resourceModel.setResource(resource);

        return resourceModel;
    }
}
