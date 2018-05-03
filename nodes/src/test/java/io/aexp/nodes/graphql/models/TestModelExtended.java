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

import io.aexp.nodes.graphql.annotations.GraphQLProperty;

import java.util.Arrays;

@GraphQLProperty(name = "test")
public class TestModelExtended extends TestModel {
    String test;
    boolean[] testPrimitiveArray;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public boolean[] getTestPrimitiveArray() {
        return testPrimitiveArray;
    }

    public void setTestPrimitiveArray(boolean[] testPrimitiveArray) {
        this.testPrimitiveArray = testPrimitiveArray;
    }

    @Override
    public String toString() {
        return "TestTOExtended{" + "test='" + test + '\'' + ", testPrimitiveArray=" + Arrays.toString(testPrimitiveArray) + '}';
    }
}
