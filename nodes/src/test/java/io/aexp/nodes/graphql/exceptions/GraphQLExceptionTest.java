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

package io.aexp.nodes.graphql.exceptions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GraphQLExceptionTest {

    @Test
    public void exceptionTest() {
        GraphQLException exception = new GraphQLException();
        exception.setErrors(null);
        exception.setMessage("test message");
        assertNull(null, exception.getErrors());
        assertEquals("test message", exception.getMessage());
        assertEquals("GraphQLException{message='test message', status='null', description='null', errors=null}", exception.toString());
    }

    @Test
    public void exceptionWithMessageTest() {
        GraphQLException exception = new GraphQLException("test message");
        exception.setErrors(null);
        assertNull(null, exception.getErrors());
        assertEquals("test message", exception.getMessage());
        assertEquals("GraphQLException{message='test message', status='null', description='null', errors=null}", exception.toString());
    }
}
