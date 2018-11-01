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
import io.aexp.nodes.graphql.models.TestModelEnum;
import io.aexp.nodes.graphql.models.TestModelOptionalArguments;
import io.aexp.nodes.graphql.models.TestModelScalar;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GraphQLRequestEntityTest {

    enum STATUS {
        active
    }

    private String EXAMPLE_URL = "https://graphql.example.com";

    @Test
    public void requestWithoutUrl() {
        IllegalStateException exception = null;
        try {
            GraphQLRequestEntity.Builder().request(TestModel.class).build();
        } catch (IllegalStateException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("url must be set", exception.getMessage());
    }

    @Test
    public void requestWithoutRequestBody() throws MalformedURLException {
        IllegalStateException exception = null;
        try {
            GraphQLRequestEntity.Builder().url(EXAMPLE_URL).build();
        } catch (IllegalStateException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("request must be set", exception.getMessage());
    }

    @Test
    public void requestWithOptionalParameter() throws MalformedURLException {
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .request(TestModelOptionalArguments.class)
                .arguments(new Arguments("test.nested", new Argument<Integer>("first", 10)))
                .build();
        requestEntity.setRequestMethod(GraphQLTemplate.GraphQLMethod.QUERY);
        assertEquals(EXAMPLE_URL, requestEntity.getUrl().toString());
        assertEquals("GraphQLRequestEntity{request='query { test { nested (first:10) { string } } } ', url='"+EXAMPLE_URL+"'}", requestEntity.toString());
    }

    @Test
    public void requestWithOtherOptionalParameter() throws MalformedURLException {
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .request(TestModelOptionalArguments.class)
                .arguments(new Arguments("test.nested", new Argument<Integer>("last", 5)))
                .build();
        requestEntity.setRequestMethod(GraphQLTemplate.GraphQLMethod.QUERY);
        assertEquals(EXAMPLE_URL, requestEntity.getUrl().toString());
        assertEquals("GraphQLRequestEntity{request='query { test { nested (last:5) { string } } } ', url='"+EXAMPLE_URL+"'}", requestEntity.toString());
    }

    @Test
    public void requestWithEnumInModel() throws MalformedURLException {
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .request(TestModelEnum.class)
                .build();
        requestEntity.setRequestMethod(GraphQLTemplate.GraphQLMethod.QUERY);
        assertEquals(EXAMPLE_URL, requestEntity.getUrl().toString());
        assertEquals("GraphQLRequestEntity{request='query { number } ', url='"+EXAMPLE_URL+"'}", requestEntity.toString());
    }

    @Test
    public void createSimpleRequest() throws MalformedURLException {
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .request(TestModel.class)
                .build();
        requestEntity.setRequestMethod(GraphQLTemplate.GraphQLMethod.QUERY);
        assertEquals(EXAMPLE_URL, requestEntity.getUrl().toString());
        assertEquals("GraphQLRequestEntity{request='query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:null) { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } } ', url='"+EXAMPLE_URL+"'}", requestEntity.toString());
    }

    @Test
    public void simpleRequestWithUrl() throws MalformedURLException {
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .request(TestModel.class)
                .build();
        assertEquals(EXAMPLE_URL, requestEntity.getUrl().toString());
        assertEquals("GraphQLRequestEntity{request='query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:null) { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } } ', url='"+EXAMPLE_URL+"'}", requestEntity.toString());
    }

    @Test
    public void simpleRequestWithVariables() throws MalformedURLException {
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .variables(new Variable<String>("variableName", "name"), new Variable<Integer>("anothaVariable", 1234), new Variable<STATUS>("andAnothaVariable", STATUS.active))
                .request(TestModel.class)
                .build();
        assertEquals(EXAMPLE_URL, requestEntity.getUrl().toString());
        assertEquals("GraphQLRequestEntity{request='query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:null) { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } } ', url='"+EXAMPLE_URL+"'}", requestEntity.toString());
        assertEquals("{variableName=name, anothaVariable=1234, andAnothaVariable=active}", requestEntity.getVariables().toString());
    }

    @Test
    public void simpleRequestWithScalars() throws MalformedURLException {
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .scalars(BigDecimal.class)
                .request(TestModelScalar.class)
                .build();
        assertEquals(EXAMPLE_URL, requestEntity.getUrl().toString());
        assertEquals(Collections.singletonList(BigDecimal.class), requestEntity.getScalars());
        assertEquals("GraphQLRequestEntity{request='query { monay money } ', url='"+EXAMPLE_URL+"'}", requestEntity.toString());
    }
}
