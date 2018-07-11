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

package io.aexp.nodes.graphql.api;

import io.aexp.nodes.graphql.Argument;
import io.aexp.nodes.graphql.Arguments;
import io.aexp.nodes.graphql.GraphQLRequestEntity;
import io.aexp.nodes.graphql.GraphQLResponseEntity;
import io.aexp.nodes.graphql.GraphQLTemplate;
import io.aexp.nodes.graphql.InputObject;
import io.aexp.nodes.graphql.Variable;
import io.aexp.nodes.graphql.models.TestModel;
import io.aexp.nodes.graphql.internal.Error;
import io.aexp.nodes.graphql.internal.Location;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Confirm that the public API has not changed.
 * Adding new tests here = minor version update
 * Changing existing tests here = major version update
 */
public class PublicUsage {

    private GraphQLTemplate graphQLTemplate = new GraphQLTemplate();

    private MockWebServer server;
    private HttpUrl mockServerUrl;

    @Before
    public void setupMockServer() throws Exception {
        server = new MockWebServer();
        server.start();
        server.enqueue(new MockResponse().setBody("{\n" +
                "  \"data\": {\n" +
                "    \"test\": {\n" +
                "      \"testString\": \"String\"\n" +
                "    }\n" +
                "  }," +
                "  \"errors\": [\n" +
                "    {\n" +
                "      \"message\": \"Cannot query field \\\"invalid\\\" on type \\\"TestTO\\\".\",\n" +
                "      \"locations\": [\n" +
                "        {\n" +
                "          \"line\": 1,\n" +
                "          \"column\": 1\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}"
        ));
        mockServerUrl = server.url("/test");
    }

    @After
    public void tearDownMockServer() throws Exception {
        server.shutdown();
    }

    @Test
    public void queryVarArgs() throws MalformedURLException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "some token");

        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .request(TestModel.class)
                .url(mockServerUrl.toString())
                .arguments(new Arguments("test", new Argument<String>("id", "someId")))
                .variables(new Variable<String>("someVariableKey", "someVariableValue"))
                .scalars(BigDecimal.class, BigInteger.class)
                .headers(headers)
                .build();
        GraphQLResponseEntity response = graphQLTemplate.query(requestEntity, Object.class);

        Assert.assertEquals(requestQuery(GraphQLTemplate.GraphQLMethod.QUERY), requestEntity.getRequest());
        assertRequestEntityMatches(requestEntity);
        Assert.assertEquals("{test={testString=String}}", response.getResponse().toString());
        assertErrorsMatches(response.getErrors());
    }

    @Test
    public void queryNotVarArgs() throws MalformedURLException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "some token");

        List<Class> scalars = new ArrayList<Class>();
        scalars.add(BigDecimal.class);
        scalars.add(BigInteger.class);

        List<Variable> variables = new ArrayList<Variable>();
        variables.add(new Variable<String>("someVariableKey", "someVariableValue"));

        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .request(TestModel.class)
                .url(mockServerUrl.toString())
                .arguments(new Arguments("test", new Argument<String>("id", "someId")))
                .variables(variables)
                .scalars(scalars)
                .headers(headers)
                .build();
        GraphQLResponseEntity response = graphQLTemplate.query(requestEntity, Object.class);

        Assert.assertEquals(requestQuery(GraphQLTemplate.GraphQLMethod.QUERY), requestEntity.getRequest());
        assertRequestEntityMatches(requestEntity);
        Assert.assertEquals("{test={testString=String}}", response.getResponse().toString());
        assertErrorsMatches(response.getErrors());
    }

    @Test
    public void mutate() throws MalformedURLException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "some token");

        List<Class> scalars = new ArrayList<Class>();
        scalars.add(BigDecimal.class);
        scalars.add(BigInteger.class);

        List<Variable> variables = new ArrayList<Variable>();
        variables.add(new Variable<String>("someVariableKey", "someVariableValue"));

        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .request(TestModel.class)
                .url(mockServerUrl.toString())
                .arguments(new Arguments("test", new Argument<String>("id", "someId")))
                .variables(variables)
                .scalars(scalars)
                .headers(headers)
                .build();
        GraphQLResponseEntity response = graphQLTemplate.mutate(requestEntity, Object.class);

        Assert.assertEquals(requestQuery(GraphQLTemplate.GraphQLMethod.MUTATE), requestEntity.getRequest());
        assertRequestEntityMatches(requestEntity);
        Assert.assertEquals("{test={testString=String}}", response.getResponse().toString());
        assertErrorsMatches(response.getErrors());
    }

    @Test
    public void execute() throws MalformedURLException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "some token");

        List<Class> scalars = new ArrayList<Class>();
        scalars.add(BigDecimal.class);
        scalars.add(BigInteger.class);
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .request(requestQuery(GraphQLTemplate.GraphQLMethod.QUERY))
                .url(mockServerUrl.toString())
                .scalars(scalars)
                .headers(headers)
                .build();

        GraphQLResponseEntity response = graphQLTemplate.execute(requestEntity, Object.class);

        Assert.assertEquals(requestQuery(GraphQLTemplate.GraphQLMethod.QUERY), requestEntity.getRequest());
        Assert.assertEquals("{test={testString=String}}", response.getResponse().toString());
        assertErrorsMatches(response.getErrors());
    }

    private void assertRequestEntityMatches(GraphQLRequestEntity requestEntity) {
        Assert.assertEquals("someVariableValue", requestEntity.getVariables().get("someVariableKey"));
        Assert.assertEquals("/test", requestEntity.getUrl().getPath());
        Assert.assertEquals("some token", requestEntity.getHeaders().get("Authorization"));
        Assert.assertEquals(Arrays.asList(BigDecimal.class, BigInteger.class), requestEntity.getScalars());
    }

    private void assertErrorsMatches(Error[] errors) {
        Assert.assertEquals(1, errors.length);
        for (Error error : errors) {
            String message = error.getMessage();
            Assert.assertEquals("Cannot query field \"invalid\" on type \"TestTO\".", message);
            Location[] locations = error.getLocations();
            Assert.assertEquals(1, locations.length);
            for (Location location : locations) {
                Assert.assertEquals("1", location.getLine());
                Assert.assertEquals("1", location.getColumn());
            }
        }
    }

    private String requestQuery(GraphQLTemplate.GraphQLMethod method) {
        return method.getValue() + " ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:\"someId\") { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } } ";
    }
}
