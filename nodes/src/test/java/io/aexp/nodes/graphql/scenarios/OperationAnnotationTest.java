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

import io.aexp.nodes.graphql.GraphQLRequestEntity;
import io.aexp.nodes.graphql.exceptions.GraphQLException;
import io.aexp.nodes.graphql.models.TestOperation;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import java.net.MalformedURLException;


public class OperationAnnotationTest {

    private String EXAMPLE_URL = "https://graphql.example.com";

    @Test
    public void operationWithVariablesAndProperties() throws GraphQLException, MalformedURLException {
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .variables()
                .request(TestOperation.class)
                .build();
        System.out.println("Request Entity:" + requestEntity.toString());
        assertThat(requestEntity.toString(), containsString("'query TestOperationName ($inputTestName:String!){ FindTest (testName:$inputTestName) {"));
    }

}
