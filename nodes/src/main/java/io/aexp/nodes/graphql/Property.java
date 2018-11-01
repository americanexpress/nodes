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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class Property {

    private String resourceName;
    private GraphQLTemplate.GraphQLMethod method;
    private List<Argument> arguments;
    private Map<String, Object> variables;
    private Map<String, Property> children;
    private StringBuilder message = new StringBuilder();

    Property() {
        this.arguments = null;
        this.children = null;
        this.method = null;
    }

    /**
     * Builds the individual property in the request based on the property's details
     *
     * @param field field's name
     * @return String
     */
    protected String getMessage(String field) {
        if (method != null)  message.append(method.getValue()).append(" ");
        if (field != null) message.append(field).append(" ");
        if (variables != null && !variables.isEmpty()) {
            message.append("(");
            List<String> variableList = new ArrayList<String>();
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                variableList.add(entry.getKey() + ":" + entry.getValue());
            }
            message.append(StringUtil.joinStringArray(",", variableList));
            message.append(")");
        }
        if (this.resourceName != null) message.append(": ").append(resourceName).append(" ");
        if (this.arguments != null && !this.arguments.isEmpty()) {
            List<String> argumentList = new ArrayList<String>();
            for (Argument argument: arguments) {
                if (!argument.isOptional() || argument.getValue() != null) {
                    argumentList.add(StringUtil.formatGraphQLParameter(argument.getKey(), argument.getValue()));
                }
            }
            if (!argumentList.isEmpty()) {
                message.append("(");
                message.append(StringUtil.joinStringArray(",", argumentList));
                message.append(") ");
            }
        }
        if (children != null && !children.isEmpty()) {
            message.append("{ ");
            for (Map.Entry<String, Property> entry : children.entrySet()) {
                message.append(entry.getValue().getMessage(entry.getKey()));
            }
            message.append("} ");
        }
        String res = message.toString();
        message.setLength(0);
        return res;
    }

    String getResourceName() {
        return resourceName;
    }

    void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    List<Argument> getArguments() {
        return arguments;
    }

    void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }

    Map<String, Object> getVariables() {
        return variables;
    }

    void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    Map<String, Property> getChildren() {
        return children;
    }

    void setChildren(Map<String, Property> children) {
        this.children = children;
    }

    GraphQLTemplate.GraphQLMethod getMethod() {
        return method;
    }

    void setMethod(GraphQLTemplate.GraphQLMethod method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "Property{" +
                "resourceName='" + resourceName + '\'' +
                ", method='" + method + '\'' +
                ", arguments=" + arguments +
                ", variables=" + variables +
                ", children=" + children +
                ", message=" + message.toString() +
                '}';
    }
}
