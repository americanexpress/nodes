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
import io.aexp.nodes.graphql.Variable;
import io.aexp.nodes.graphql.models.TestModel;
import io.aexp.nodes.graphql.models.TestModelExtended;
import io.aexp.nodes.graphql.models.TestModels;
import io.aexp.nodes.graphql.Argument;
import io.aexp.nodes.graphql.Arguments;
import io.aexp.nodes.graphql.exceptions.GraphQLException;
import io.aexp.nodes.graphql.InputObject;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RequestBuilderTest {

    enum STATUS {
        active
    }

    private String EXAMPLE_URL = "https://graphql.example.com";

    @Test
    public void simpleQuery() throws MalformedURLException {
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .request(TestModel.class)
                .build();
        assertEquals("query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:null) { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } } ", requestEntity.getRequest());
        assertTrue(requestEntity.getVariables().isEmpty());
    }

    @Test
    public void stringParameterizedQuery() throws GraphQLException, MalformedURLException {
        List<String> argumentList = new ArrayList<String>();
        argumentList.add("string1");
        argumentList.add("string2");
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .arguments(
                        new Arguments("test", new Argument<String>("id", "1")),
                        new Arguments("test.testString", new Argument<List>("defaultList", argumentList)))
                .request(TestModel.class)
                .build();
        assertEquals("query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:\"1\") { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:[\"string1\",\"string2\"]) testArrayList testFloat testDouble testLong } } ", requestEntity.getRequest());
        assertTrue(requestEntity.getVariables().isEmpty());
    }

    @Test
    public void numberParameterizedQuery() throws GraphQLException, MalformedURLException {
        List<Integer> argumentList = new ArrayList<Integer>();
        argumentList.add(1);
        argumentList.add(2);
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .arguments(
                        new Arguments("test", new Argument<Integer>("id", 1)),
                        new Arguments("test.testString", new Argument<List>("defaultList", argumentList)))
                .request(TestModel.class)
                .build();
        assertEquals("query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:1) { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:[1,2]) testArrayList testFloat testDouble testLong } } ", requestEntity.getRequest());
        assertTrue(requestEntity.getVariables().isEmpty());
    }

    @Test
    public void enumParameterizedQuery() throws GraphQLException, MalformedURLException {
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .arguments(new Arguments("test", new Argument<STATUS>("id", STATUS.active)))
                .request(TestModel.class)
                .build();
        assertEquals("query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:active) { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } } ", requestEntity.getRequest());
        assertTrue(requestEntity.getVariables().isEmpty());
    }

    @Test
    public void extendedModel() throws GraphQLException, MalformedURLException {
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .request(TestModelExtended.class)
                .build();
        assertEquals("query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test { testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } test testByte : testByte testFloat testLong testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testPrimitiveArray testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testDouble } } ", requestEntity.getRequest());
        assertTrue(requestEntity.getVariables().isEmpty());
    }

    @Test
    public void multiModel() throws GraphQLException, MalformedURLException {
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .request(TestModels.class)
                .build();
        assertEquals("query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test2 : test (id:null,test:\"test2\",testBool:false,testInt:2,testFloat:2.2) { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } test1 : test (id:null,test:\"test1\",testBool:true,testInt:1,testFloat:1.1) { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } } ", requestEntity.getRequest());
        assertTrue(requestEntity.getVariables().isEmpty());
    }

    @Test
    public void queryWithArgumentsAndVariables() throws GraphQLException, MalformedURLException {
        List<Argument> argumentList = new ArrayList<Argument>();
        List<Arguments> argumentsList = new ArrayList<Arguments>();
        List<Variable> variables = new ArrayList<Variable>();
        List<String> listVariable = new ArrayList<String>();
        argumentList.add(new Argument<String>("id", "1"));
        Arguments arguments = new Arguments("test", argumentList);
        argumentsList.add(arguments);
        listVariable.add("string1");
        listVariable.add("string2");
        variables.add(new Variable<String>("variableName", "name"));
        variables.add(new Variable<Integer>("anothaVariable", 1234));
        variables.add(new Variable<STATUS>("andAnothaVariable", STATUS.active));
        variables.add(new Variable<List>("andAListVariable", listVariable));
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .arguments(argumentsList)
                .variables(variables)
                .request(TestModel.class)
                .build();
        assertEquals("query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:\"1\") { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } } ", requestEntity.getRequest());
        assertEquals("{variableName=name, anothaVariable=1234, andAnothaVariable=active, andAListVariable=[string1, string2]}", requestEntity.getVariables().toString());
    }

    @Test
    public void queryWithInputObjectArguments() throws GraphQLException, MalformedURLException {
        List<String> stringList = new ArrayList<String>();
        stringList.add("string1");
        stringList.add("string2");
        stringList.add("string3");
        InputObject nestedObj = new InputObject.Builder<Object>()
                .put("input1", 1)
                .put("input2", true)
                .put("input3", stringList)
                .put("input4", "string")
                .build();
        InputObject idObj = new InputObject.Builder<InputObject>()
                .put("nestedObj", nestedObj)
                .build();
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .arguments(new Arguments("test", new Argument("id", idObj)))
                .request(TestModel.class)
                .build();
        assertEquals("query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:{nestedObj:{input4:\"string\",input3:[\"string1\",\"string2\",\"string3\"],input2:true,input1:1}}) { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } } ", requestEntity.getRequest());
    }

    @Test
    public void queryWithAListOfInputObjectArguments() throws GraphQLException, MalformedURLException {
        List<String> stringList = new ArrayList<String>();
        stringList.add("string1");
        stringList.add("string2");
        stringList.add("string3");
        InputObject input = new InputObject.Builder<Object>()
                .put("input1", 1)
                .put("input2", true)
                .put("input3", stringList)
                .put("input4", "string")
                .build();
        List<InputObject> inputs = new ArrayList<InputObject>();
        inputs.add(input);
        inputs.add(input);
        GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
                .url(EXAMPLE_URL)
                .arguments(new Arguments("test", new Argument("id", inputs)))
                .request(TestModel.class)
                .build();
        assertEquals("query ($andAnothaVariable:status,$anothaVariable:Int,$andAListVariable:[String],$variableName:String!){ test (id:[InputObject{map={input4=string, input3=[string1, string2, string3], input2=true, input1=1}},InputObject{map={input4=string, input3=[string1, string2, string3], input2=true, input1=1}}]) { testShort : testShort testCharacter testList { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testInteger testBoolean nestedTest { anotherTestString (variableName:$variableName) andAnothaOne (anothaVariable:$anothaVariable,andAnothaVariable:$andAnothaVariable,andAListVariable:$andAListVariable) } testByte : testByte testString : testString (anotherOne:null,default:\"default\",defaultList:null) testArrayList testFloat testDouble testLong } } ", requestEntity.getRequest());
    }
}
