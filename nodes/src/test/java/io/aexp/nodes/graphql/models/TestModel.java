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

package io.aexp.nodes.graphql.models;

import io.aexp.nodes.graphql.annotations.GraphQLArgument;
import io.aexp.nodes.graphql.annotations.GraphQLArguments;
import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import io.aexp.nodes.graphql.annotations.GraphQLProperty;

import java.util.ArrayList;
import java.util.List;

@GraphQLProperty(name = "test", arguments = {@GraphQLArgument(name = "id")})
public class TestModel {
    @GraphQLArguments({
        @GraphQLArgument(name = "default", value = "default"),
        @GraphQLArgument(name = "defaultList")
    })
    @GraphQLProperty(name = "testString", arguments = {@GraphQLArgument(name = "anotherOne")})
    private String testString;
    private Byte testByte;
    private Short testShort;
    private Integer testInteger;
    private Long testLong;
    private char testCharacter;
    private Float testFloat;
    private Double testDouble;
    private Boolean testBoolean;
    private NestedTestModel nestedTest;
    private ArrayList<String> testArrayList;
    private List<NestedTestModel> testList;
    @GraphQLIgnore
    private String ignoredField;

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public Byte getTestByte() {
        return testByte;
    }

    public void setTestByte(Byte testByte) {
        this.testByte = testByte;
    }

    public Short getTestShort() {
        return testShort;
    }

    public void setTestShort(Short testShort) {
        this.testShort = testShort;
    }

    public Integer getTestInteger() {
        return testInteger;
    }

    public void setTestInteger(Integer testInteger) {
        this.testInteger = testInteger;
    }

    public Long getTestLong() {
        return testLong;
    }

    public void setTestLong(Long testLong) {
        this.testLong = testLong;
    }

    public char getTestCharacter() {
        return testCharacter;
    }

    public void setTestCharacter(char testCharacter) {
        this.testCharacter = testCharacter;
    }

    public Float getTestFloat() {
        return testFloat;
    }

    public void setTestFloat(Float testFloat) {
        this.testFloat = testFloat;
    }

    public Double getTestDouble() {
        return testDouble;
    }

    public void setTestDouble(Double testDouble) {
        this.testDouble = testDouble;
    }

    public Boolean getTestBoolean() {
        return testBoolean;
    }

    public void setTestBoolean(Boolean testBoolean) {
        this.testBoolean = testBoolean;
    }

    public NestedTestModel getNestedTest() {
        return nestedTest;
    }

    public void setNestedTest(NestedTestModel nestedTest) {
        this.nestedTest = nestedTest;
    }

    public ArrayList<String> getTestArrayList() {
        return testArrayList;
    }

    public void setTestArrayList(ArrayList<String> testArrayList) {
        this.testArrayList = testArrayList;
    }

    public List<NestedTestModel> getTestList() {
        return testList;
    }

    public void setTestList(List<NestedTestModel> testList) {
        this.testList = testList;
    }

    public String getIgnoredField() {
        return ignoredField;
    }

    public void setIgnoredField(String ignoredField) {
        this.ignoredField = ignoredField;
    }

    @Override
    public String toString() {
        return "TestTO{" + "testString='" + testString + '\'' + ", testByte=" + testByte + ", testShort=" + testShort + ", testInteger=" + testInteger + ", testLong=" + testLong + ", testCharacter=" + testCharacter + ", testFloat=" + testFloat + ", testDouble=" + testDouble + ", testBoolean=" + testBoolean + ", nestedTest=" + nestedTest + ", testArrayList=" + testArrayList + ", testList=" + testList + ", ignoredField='" + ignoredField + '\'' + '}';
    }
}
