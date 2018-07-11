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

package io.aexp.nodes.graphql.scenarios;

import io.aexp.nodes.graphql.Argument;
import io.aexp.nodes.graphql.Arguments;
import io.aexp.nodes.graphql.exceptions.GraphQLException;
import io.aexp.nodes.graphql.GraphQLRequestEntity;
import org.junit.Test;
import io.aexp.nodes.graphql.models.TestModel;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ArgumentSettingTest {

    private String EXAMPLE_URL = "https://graphql.example.com";

    @Test
    public void argumentPathError() throws GraphQLException, MalformedURLException {
        GraphQLException exception = null;
        try {
            GraphQLRequestEntity.Builder()
                    .url(EXAMPLE_URL)
                    .arguments(new Arguments("test.testNope", new Argument("id", "1")))
                    .request(TestModel.class)
                    .build();
        } catch (GraphQLException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("'test.testNope' is an invalid property path", exception.getMessage());
        assertNull(exception.getErrors());
        assertEquals("GraphQLException{message=''test.testNope' is an invalid property path', status='null', description='null', errors=null}", exception.toString());
    }

    @Test
    public void noArgumentError() throws GraphQLException, MalformedURLException {
        GraphQLException exception = null;
        try {
            GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .arguments(new Arguments("test.testLong", new Argument("id", "1")))
                .request(TestModel.class)
                .build();
        } catch (GraphQLException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Path 'test.testLong' is not expecting any arguments, please set the @GraphQLArguments or @GraphQLProperty annotation on the field you are expecting arguments for", exception.getMessage());
        assertNull(exception.getErrors());
        assertEquals("GraphQLException{message='Path 'test.testLong' is not expecting any arguments, please set the @GraphQLArguments or @GraphQLProperty annotation on the field you are expecting arguments for', status='null', description='null', errors=null}", exception.toString());
    }

    @Test
    public void invalidArgumentError() throws GraphQLException, MalformedURLException {
        GraphQLException exception = null;
        try {
            GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .arguments(new Arguments("test", new Argument("test", "1")))
                .request(TestModel.class)
                .build();
        } catch (GraphQLException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Argument 'Argument{key='test', value=1}' doesn't exist on path 'test'", exception.getMessage());
        assertNull(exception.getErrors());
        assertEquals("GraphQLException{message='Argument 'Argument{key='test', value=1}' doesn't exist on path 'test'', status='null', description='null', errors=null}", exception.toString());
    }
}
