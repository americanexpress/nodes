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
import io.aexp.nodes.graphql.models.TestModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GraphQLResponseEntityTest {

    @Test
    public void responseEntityTest() {
        GraphQLResponseEntity<TestModel> responseEntity = new GraphQLResponseEntity<TestModel>();
        TestModel test = new TestModel();
        responseEntity.setErrors(null);
        responseEntity.setResponse(test);
        assertNull(responseEntity.getErrors());
        assertEquals(test, responseEntity.getResponse());
        assertEquals("GraphQLResponseEntity{errors=null, response=TestTO{testString='null', testByte=null, testShort=null, testInteger=null, testLong=null, testCharacter=\u0000, testFloat=null, testDouble=null, testBoolean=null, nestedTest=null, testArrayList=null, testList=null, ignoredField='null'}}", responseEntity.toString());
    }
}
