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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParameterTest {

    @Test
    public void parameterGetterTest() {
        Parameter<Object> parameter = new Parameter<Object>(null, null);
        assertNull(parameter.getKey());
        assertNull(parameter.getValue());
    }

    @Test
    public void parameterSetterTest() {
        String key = "key";
        Object value = new Object();
        Parameter<Object> parameter = new Parameter<Object>(null, null);
        parameter.setKey(key);
        parameter.setValue(value);
        assertEquals(key, parameter.getKey());
        assertEquals(value, parameter.getValue());
    }

    @Test
    public void parameterStringTest() {
        Parameter<String> parameter = new Parameter<String>("key", "value");
        assertEquals("Parameter{key='key', value=value}", parameter.toString());
    }
}
