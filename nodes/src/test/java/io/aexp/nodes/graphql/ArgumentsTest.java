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

public class ArgumentsTest {

    @Test
    public void argumentsTest() {
        Arguments arguments = new Arguments("somePath", new Argument<String>("someKey", "someValue"));
        assertEquals("somePath", arguments.getDotPath());
        assertEquals("Arguments{dotPath='somePath', arguments=[Argument{key='someKey', value=someValue, optional=false}]}", arguments.toString());
    }
}
