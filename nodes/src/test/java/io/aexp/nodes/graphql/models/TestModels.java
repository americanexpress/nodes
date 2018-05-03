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

import io.aexp.nodes.graphql.annotations.GraphQLArguments;
import io.aexp.nodes.graphql.annotations.GraphQLArgument;
import io.aexp.nodes.graphql.annotations.GraphQLProperty;

public class TestModels {
    @GraphQLProperty(name = "test")
    @GraphQLArguments({
            @GraphQLArgument(name = "id"),
            @GraphQLArgument(name = "test", value = "test1"),
            @GraphQLArgument(name = "testBool", value = "true", type = "Boolean"),
            @GraphQLArgument(name = "testInt", value = "1", type = "Integer"),
            @GraphQLArgument(name = "testFloat", value = "1.1", type = "Float")
    })
    private TestModel test1;
    @GraphQLProperty(name = "test")
    @GraphQLArguments({
            @GraphQLArgument(name = "id"),
            @GraphQLArgument(name = "test", value = "test2"),
            @GraphQLArgument(name = "testBool", value = "false", type = "Boolean"),
            @GraphQLArgument(name = "testInt", value = "2", type = "Integer"),
            @GraphQLArgument(name = "testFloat", value = "2.2", type = "Float")
    })
    private TestModel test2;

    public TestModel getTest1() {
        return test1;
    }

    public void setTest1(TestModel test1) {
        this.test1 = test1;
    }

    public TestModel getTest2() {
        return test2;
    }

    public void setTest2(TestModel test2) {
        this.test2 = test2;
    }

    @Override
    public String toString() {
        return "TestTOs{" +
                "test1=" + test1 +
                ", test2=" + test2 +
                '}';
    }
}
