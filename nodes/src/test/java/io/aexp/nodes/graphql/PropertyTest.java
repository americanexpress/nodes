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

import io.aexp.nodes.graphql.GraphQLTemplate.GraphQLMethod;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyTest {

    @Test
    public void propertyTest() {
        Property property = new Property();
        property.setMethod(GraphQLMethod.QUERY);
        property.setArguments(null);
        property.setVariables(null);
        property.setChildren(null);
        property.setResourceName("resourceName");
        assertEquals(GraphQLMethod.QUERY, property.getMethod());
        assertNull(property.getArguments());
        assertNull(property.getVariables());
        assertNull(property.getChildren());
        assertEquals("resourceName", property.getResourceName());
        assertEquals("query : resourceName ", property.getMessage(null));
        assertEquals("query fieldName : resourceName ", property.getMessage("fieldName"));
        assertEquals("Property{resourceName='resourceName', method='QUERY', arguments=null, variables=null, children=null, message=}", property.toString());
    }
}
