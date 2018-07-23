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

package io.aexp.nodes.graphql.internal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ErrorTest {

    @Test
    public void errorTest() {
        Error error = new Error();
        error.setMessage("error message");
        error.setLocations(null);
        error.setErrorType("DataFetchingException");
        error.setPath(null);
        error.setExtensions(null);
        assertNull(error.getLocations());
        assertEquals("error message", error.getMessage());
        assertEquals("Error{message='error message', locations=null, extensions=null, path=null, errorType='DataFetchingException'}", error.toString());
    }
}
