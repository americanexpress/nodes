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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Arguments {

    private String dotPath;
    private List<Argument> arguments;

    public Arguments(String dotPath, List<Argument> arguments) {
        this.arguments = Collections.unmodifiableList(arguments);
        this.dotPath = dotPath;
    }

    public Arguments(String dotPath, Argument... arguments) {
        this(dotPath, Arrays.asList(arguments));
    }

    public String getDotPath() {
        return dotPath;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "Arguments{" + "dotPath='" + dotPath + '\'' + ", arguments=" + arguments + '}';
    }
}
