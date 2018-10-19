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

import io.aexp.nodes.graphql.exceptions.GraphQLException;
import io.aexp.nodes.graphql.models.TestModel;
import io.aexp.nodes.graphql.models.TestModelSimple;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GraphQLTemplateTest {

    private GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
    private MockWebServer server;

    @Before
    public void setupMockServer() throws Exception {
        server = new MockWebServer();
        server.start();
    }

    @After
    public void tearDownMockServer() throws Exception {
        server.shutdown();
    }

    @Test
    public void successfulMutationWithErrors() throws GraphQLException, MalformedURLException {
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
        HttpUrl serviceUrl = server.url("/successfulQueryWithErrors");
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(serviceUrl.toString())
                .request(TestModel.class)
                .build();
        GraphQLResponseEntity<TestModel> response = graphQLTemplate.mutate(requestEntity, TestModel.class);
        assertEquals("mutation ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:null) { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } } ", requestEntity.getRequest().toString());
        assertEquals("GraphQLResponseEntity{errors=[Error{message='Cannot query field \"invalid\" on type \"TestTO\".', locations=[Location{line='1', column='1'}]}], headers=[null:HTTP/1.1 200 OK][Content-Length:261], response=TestTO{testString='String', testByte=null, testShort=null, testInteger=null, testLong=null, testCharacter=\u0000, testFloat=null, testDouble=null, testBoolean=null, nestedTest=null, testArrayList=null, testList=null, ignoredField='null'}}", response.toString());
    }

    @Test
    public void successfulSubsequentQueriesWithAuth() throws GraphQLException, InterruptedException, MalformedURLException {
        server.enqueue(new MockResponse().setBody("{\n" +
                "  \"data\": {\n" +
                "    \"test\": {\n" +
                "      \"testString\": \"String\"\n" +
                "    }\n" +
                "  }," +
                "  \"errors\": null" +
                "}"
        ));
        server.enqueue(new MockResponse().setBody("{\n" +
                "  \"data\": {\n" +
                "    \"simpleString\": \"simple string\"\n" +
                "  }," +
                "  \"errors\": null" +
                "}"
        ));
        String authToken = "my token";
        HttpUrl serviceUrl = server.url("/successfulQueryWithAuth");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", authToken);
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(serviceUrl.toString())
                .request(TestModel.class)
                .headers(headers)
                .build();
        graphQLTemplate.mutate(requestEntity, TestModel.class);
        Headers reqHeaders = server.takeRequest().getHeaders();
        assertEquals(authToken, reqHeaders.get("Authorization"));

        HttpUrl secondServiceUrl = server.url("/successfulQueryWithAuth");
        GraphQLRequestEntity secondRequestEntity = GraphQLRequestEntity.Builder()
                .url(secondServiceUrl.toString())
                .request(TestModelSimple.class)
                .build();
        GraphQLResponseEntity<TestModelSimple> secondResponseEntity = graphQLTemplate.mutate(secondRequestEntity, TestModelSimple.class);
        assertEquals("mutation { simpleString } ", secondRequestEntity.getRequest().toString());
        assertEquals("GraphQLResponseEntity{errors=null, headers=[null:HTTP/1.1 200 OK][Content-Length:71], response=TestTOSimple{simpleString='simple string'}}", secondResponseEntity.toString());
    }

    @Test
    public void executeWithoutMethod() throws GraphQLException, MalformedURLException {
        server.enqueue(new MockResponse().setBody("{\n" +
            "  \"data\": {\n" +
            "    \"test\": {\n" +
            "      \"testString\": \"String\"\n" +
            "    }\n" +
            "  }" +
            "}"
        ));
        HttpUrl serviceUrl = server.url("/executeWithoutMethod");
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(serviceUrl.toString())
                .request("my graphql query")
                .build();
        GraphQLResponseEntity<TestModel> response = graphQLTemplate.execute(requestEntity, TestModel.class);
        assertEquals("my graphql query", requestEntity.getRequest().toString());
        assertEquals("GraphQLResponseEntity{errors=null, headers=[null:HTTP/1.1 200 OK][Content-Length:67], response=TestTO{testString='String', testByte=null, testShort=null, testInteger=null, testLong=null, testCharacter=\u0000, testFloat=null, testDouble=null, testBoolean=null, nestedTest=null, testArrayList=null, testList=null, ignoredField='null'}}", response.toString());
    }

    @Test
    public void badRequestErrorWithStatusCode() throws GraphQLException, MalformedURLException {
        server.enqueue(new MockResponse().setResponseCode(400).setBody("{\n" +
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
        HttpUrl serviceUrl = server.url("/badRequestError");
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(serviceUrl.toString())
                .request(TestModel.class)
                .build();
        GraphQLException exception = null;
        try {
            graphQLTemplate.query(requestEntity, TestModel.class);
        } catch (GraphQLException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:null) { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } } ", requestEntity.getRequest().toString());
        assertEquals("GraphQLException{message='Client Error', status='400', description='null', errors=[Error{message='Cannot query field \"invalid\" on type \"TestTO\".', locations=[Location{line='1', column='1'}]}]}", exception.toString());
    }

    @Test
    public void badRequestErrorWithSuccessStatusCode() throws GraphQLException, MalformedURLException {
        server.enqueue(new MockResponse().setBody("{\n" +
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
        HttpUrl serviceUrl = server.url("/badRequestError");
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(serviceUrl.toString())
                .request(TestModel.class)
                .build();
        GraphQLException exception = null;
        GraphQLResponseEntity response = graphQLTemplate.query(requestEntity, TestModel.class);
        assertEquals("query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:null) { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } } ", requestEntity.getRequest().toString());
        assertEquals("GraphQLResponseEntity{errors=[Error{message='Cannot query field \"invalid\" on type \"TestTO\".', locations=[Location{line='1', column='1'}]}], headers=null, response=null}", response.toString());
    }

    @Test
    public void serverRequestError() throws GraphQLException, MalformedURLException {
        server.enqueue(new MockResponse().setResponseCode(500).setBody("{\n" +
            "  \"unxpectedKey\": \"Unexpected value\",\n" +
            "}"
        ));
        HttpUrl serviceUrl = server.url("/serverRequestError");
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(serviceUrl.toString())
                .request(TestModel.class)
                .build();
        GraphQLException exception = null;
        try {
            graphQLTemplate.query(requestEntity, TestModel.class);
        } catch (GraphQLException e) {
            exception = e;
        }
        assertEquals("query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:null) { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } } ", requestEntity.getRequest().toString());
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Server Error");
        assertEquals(exception.getStatus(), "500");
        assertNotNull(exception.getDescription());
        assertNull(exception.getErrors());
    }

    @Test
    public void nullParametersThrow() {
        GraphQLException exception = null;
        try {
            graphQLTemplate.query(null, null);
        } catch (GraphQLException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "requestEntity must not be null");
    }
}
