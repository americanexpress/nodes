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
import io.aexp.nodes.graphql.annotations.GraphQLProperty;

@GraphQLProperty(name = "test")
public class TestModelOptionalArguments {
    @GraphQLArguments({
        @GraphQLArgument(name = "first", type = "Integer", optional = true),
        @GraphQLArgument(name = "last", type = "Integer", optional = true)
    })
    private NestedTestModelOptionalArguments nested;

    public NestedTestModelOptionalArguments getNested() {
        return nested;
    }

    public void setNestedTestModelOptionalArguments(NestedTestModelOptionalArguments nested) {
        this.nested = nested;
    }

    @Override
    public String toString() {
        return "TestModelOptionalArguments{" + "nested=" + nested + '}';
    }
}
