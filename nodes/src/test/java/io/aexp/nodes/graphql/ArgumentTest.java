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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArgumentTest {

    enum STATUS {
        active
    }

    @Test
    public void argumentStringTest() {
        Argument argument = new Argument(null, null);
        argument.setKey("test");
        argument.setValue("test");
        assertEquals("test", argument.getKey());
        assertEquals("test", argument.getValue());
        assertEquals("Argument{key='test', value=test}", argument.toString());
    }

    @Test
    public void argumentIntTest() {
        Argument argument = new Argument(null, null);
        argument.setKey("test");
        argument.setValue(1);
        assertEquals("test", argument.getKey());
        assertEquals(1, argument.getValue());
        assertEquals("Argument{key='test', value=1}", argument.toString());
    }

    @Test
    public void argumentEnumTest() {
        Argument argument = new Argument(null, null);
        argument.setKey("test");
        argument.setValue(STATUS.active);
        assertEquals("test", argument.getKey());
        assertEquals(STATUS.active, argument.getValue());
        assertEquals("Argument{key='test', value=active}", argument.toString());
    }
}
