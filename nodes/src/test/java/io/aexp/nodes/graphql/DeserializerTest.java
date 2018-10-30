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

import io.aexp.nodes.graphql.models.TestModel;
import io.aexp.nodes.graphql.models.TestModelDateTime;
import io.aexp.nodes.graphql.models.TestModels;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class DeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void deserializeSimple() throws IOException {
        String json = (
            "{\n" +
            "  \"test\": {\n" +
            "    \"testString\": \"String\",\n" +
            "    \"testByte\": \"1\",\n" +
            "    \"testShort\": \"1\",\n" +
            "    \"testInteger\": 1,\n" +
            "    \"testLong\": 1,\n" +
            "    \"testCharacter\": \"a\",\n" +
            "    \"testFloat\": 1.5,\n" +
            "    \"testDouble\": 1.5,\n" +
            "    \"testBoolean\": true,\n" +
            "    \"nestedTest\": {\n" +
            "      \"anotherTestString\": \"AnotherString\"\n" +
            "    },\n" +
            "    \"testArrayList\": [\n" +
            "      \"val1\",\n" +
            "      \"val2\"\n" +
            "    ],\n" +
            "    \"testList\": [\n" +
            "      {\n" +
            "        \"anotherTestString\": \"AnotherString\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}"
        );
        InputStream stream = new ByteArrayInputStream(json.getBytes());
        JsonParser parser = mapper.getFactory().createParser(stream);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Deserializer<TestModel> deserializer = new Deserializer<TestModel>(TestModel.class);
        Resource<TestModel> res = deserializer.deserialize(parser, ctxt);
        assertEquals("Resource{resource=TestTO{testString='String', testByte=1, testShort=1, testInteger=1, testLong=1, testCharacter=a, testFloat=1.5, testDouble=1.5, testBoolean=true, nestedTest=NestedTest{anotherTestString='AnotherString', andAnothaOne='null'}, testArrayList=[val1, val2], testList=[NestedTest{anotherTestString='AnotherString', andAnothaOne='null'}], ignoredField='null'}}", res.toString());
    }

    @Test
    public void deserializeMulti() throws IOException {
        String json = (
            "{\n" +
            "  \"test1\": {\n" +
            "    \"testString\": \"String\",\n" +
            "    \"testByte\": \"1\",\n" +
            "    \"testShort\": \"1\",\n" +
            "    \"testInteger\": 1,\n" +
            "    \"testLong\": 1,\n" +
            "    \"testCharacter\": \"a\",\n" +
            "    \"testFloat\": 1.5,\n" +
            "    \"testDouble\": 1.5,\n" +
            "    \"testBoolean\": true,\n" +
            "    \"nestedTest\": {\n" +
            "      \"anotherTestString\": \"AnotherString\"\n" +
            "    },\n" +
            "    \"testArrayList\": [\n" +
            "      \"val1\",\n" +
            "      \"val2\"\n" +
            "    ],\n" +
            "    \"testList\": [\n" +
            "      {\n" +
            "        \"anotherTestString\": \"AnotherString\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"test2\": {\n" +
            "    \"testString\": \"String\",\n" +
            "    \"testByte\": \"1\",\n" +
            "    \"testShort\": \"1\",\n" +
            "    \"testInteger\": 1,\n" +
            "    \"testLong\": 1,\n" +
            "    \"testCharacter\": \"a\",\n" +
            "    \"testFloat\": 1.5,\n" +
            "    \"testDouble\": 1.5,\n" +
            "    \"testBoolean\": true,\n" +
            "    \"nestedTest\": {\n" +
            "      \"anotherTestString\": \"AnotherString\"\n" +
            "    },\n" +
            "    \"testArrayList\": [\n" +
            "      \"val1\",\n" +
            "      \"val2\"\n" +
            "    ],\n" +
            "    \"testList\": [\n" +
            "      {\n" +
            "        \"anotherTestString\": \"AnotherString\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}"
        );
        InputStream stream = new ByteArrayInputStream(json.getBytes());
        JsonParser parser = mapper.getFactory().createParser(stream);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Deserializer<TestModels> deserializer = new Deserializer<TestModels>(TestModels.class);
        Resource<TestModels> res = deserializer.deserialize(parser, ctxt);
        assertEquals("Resource{resource=TestTOs{test1=TestTO{testString='String', testByte=1, testShort=1, testInteger=1, testLong=1, testCharacter=a, testFloat=1.5, testDouble=1.5, testBoolean=true, nestedTest=NestedTest{anotherTestString='AnotherString', andAnothaOne='null'}, testArrayList=[val1, val2], testList=[NestedTest{anotherTestString='AnotherString', andAnothaOne='null'}], ignoredField='null'}, test2=TestTO{testString='String', testByte=1, testShort=1, testInteger=1, testLong=1, testCharacter=a, testFloat=1.5, testDouble=1.5, testBoolean=true, nestedTest=NestedTest{anotherTestString='AnotherString', andAnothaOne='null'}, testArrayList=[val1, val2], testList=[NestedTest{anotherTestString='AnotherString', andAnothaOne='null'}], ignoredField='null'}}}", res.toString());
    }

    @Test
    public void deserializeJsr310() throws IOException {
        String json = (
            "{\n" +
            "  \"dateTime\": \"2018-10-29T22:00:01+00:00\"\n" +
            "}"
        );
        InputStream stream = new ByteArrayInputStream(json.getBytes());
        JsonParser parser = mapper.getFactory().createParser(stream);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Deserializer<TestModelDateTime> deserializer = new Deserializer<TestModelDateTime>(TestModelDateTime.class);
        Resource<TestModelDateTime> res = deserializer.deserialize(parser, ctxt);
        assertEquals("Resource{resource=TestModelDateTime{dateTime='2018-10-29T22:00:01Z'}}", res.toString());
    }
}
