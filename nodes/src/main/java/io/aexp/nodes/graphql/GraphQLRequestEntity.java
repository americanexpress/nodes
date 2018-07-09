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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.aexp.nodes.graphql.annotations.*;
import io.aexp.nodes.graphql.exceptions.GraphQLException;

public final class GraphQLRequestEntity {

    public static RequestBuilder Builder() {
        return new RequestBuilder();
    }

    private final URL url;
    private final Map<String, String> headers;
    private final Map<String, Object> variables;
    private final List<Class> scalars;
    private final Operation operation = new Operation();
    private String request;

    GraphQLRequestEntity(RequestBuilder builder) {
        this.url = builder.url;
        this.scalars = Collections.unmodifiableList(builder.scalars);
        this.headers = Collections.unmodifiableMap(builder.headers);
        this.variables = Collections.unmodifiableMap(variableListToMap(builder.variables));
        if (builder.request != null) {
            this.request = builder.request;
        } else {
            setOperationFromClass(builder.clazz);
        }
        if (builder.arguments != null) {
            for (Arguments arguments : builder.arguments) {
                setArguments(arguments.getDotPath(), arguments.getArguments());
            }
        }
    }

    public URL getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getRequest() {
        if (this.request != null) {
            return this.request;
        }
        return operation.getMessage();
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public List<Class> getScalars() {
        return scalars;
    }

    void setRequestMethod(GraphQLTemplate.GraphQLMethod requestMethod) {
        operation.setMethod(requestMethod);
    }

    @Override
    public String toString() {
        return "GraphQLRequestEntity{" +
                "request='" + this.getRequest() + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    // Utility

    /**
     * Builds the request tree from the provided class.
     *
     * @param clazz the class used to construct the request
     */
    private void setOperationFromClass(Class clazz) {
        Map<String, Object> propertyVariables = new HashMap<String, Object>();
        Map<String, Property> children = this.getChildren(clazz, propertyVariables);

        GraphQLProperty graphQLProperty = (GraphQLProperty) clazz.getAnnotation(GraphQLProperty.class);
        GraphQLOperation graphQLOperation = (GraphQLOperation) clazz.getAnnotation(GraphQLOperation.class);
        GraphQLProperty operationGraphQLProperty = null;

        if(graphQLOperation != null) {
            operation.setName(graphQLOperation.name());
            operation.setMethod(graphQLOperation.method());

            for(GraphQLVariable graphQLVariable : graphQLOperation.variables()) {
                propertyVariables.put("$" + graphQLVariable.name(), graphQLVariable.scalar());
            }

            operationGraphQLProperty = graphQLOperation.property();
            if(operationGraphQLProperty != null){
                Property classProperty = annotationToProperty(operationGraphQLProperty);
                // fix for RequestBuilderTest tests that expectation top level property has no resource name (alias)
                // TODO - remove when RequestBuilderTests are fixed
                classProperty.setResourceName(null);
                classProperty.setChildren(children);
                operation.getChildren().put(operationGraphQLProperty.name(), classProperty);
            }
        }

        if(operationGraphQLProperty == null && graphQLProperty != null) {
            Property classProperty = annotationToProperty(graphQLProperty);
            // fix for RequestBuilderTest tests that expectation top level property has no resource name (alias)
            // TODO - remove when RequestBuilderTests are fixed
            classProperty.setResourceName(null);
            classProperty.setChildren(children);
            operation.getChildren().put(graphQLProperty.name(), classProperty);
        } else if(operationGraphQLProperty == null){
            operation.setChildren(children);
        }

        operation.setVariables(propertyVariables);
        operation.setChildren(Collections.unmodifiableMap(operation.getChildren()));
    }

    /**
     * Set arguments on a field in the request.
     *
     * @param dotPath is the nested path of classes and fields concatenated together by '.' to where the arguments will
     *                 be set. If any annotations are present on the class or field that change the display name in the
     *                 request, then that new name is to be the one included in the dotPath, otherwise the class or
     *                 field name should be used
     * @param  arguments arguments to be applied to the request
     * @throws GraphQLException is thrown at runtime if the dotPath does not correlate to a field accepting arguments
     */
    private void setArguments(String dotPath, List<Argument> arguments) throws GraphQLException {
        String[] path = dotPath.split("\\.");
        Map<String, Property> children = operation.getChildren();
        Property argProp = null;

        if(path.length == 0) {
            if(children.size() > 1) {
                throw new GraphQLException("Operation contains more than one property, dot path must specify a top level property");
            }

            Map.Entry<String,Property> entry = children.entrySet().iterator().next();
            argProp = entry.getValue();
        }

        int childrenIndex = 0;
        for (String key: path) {
            if(childrenIndex++ == 0) {
                argProp = children.get(key);
            } else if(argProp != null) {
                argProp = argProp.getChildren().get(key);
            }
        }

        if (argProp == null) throw new GraphQLException("'" + dotPath + "' is an invalid property path");
        List<Argument> args = argProp.getArguments();
        if (args == null) {
            throw new GraphQLException("Path '" + dotPath + "' is not expecting any arguments, please set the " +
                    "@GraphQLArguments or @GraphQLProperty annotation on the field you are expecting arguments for");
        }
        for (Argument argument : arguments) {
            Integer index = indexOfArg(args, argument.getKey());
            if (index == -1) throw new GraphQLException("Argument '" + argument + "' doesn't exist on path '" + dotPath + "'");
            Argument propArg = args.get(index);
            propArg.setValue(argument.getValue());
            propArg.setVariable(argument.getVariable());
        }
    }

    private Integer indexOfArg(List<Argument> arguments, String key) {
        Integer index = 0;
        for (Argument argument : arguments) {
            if (key.equalsIgnoreCase(argument.getKey())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private Map<String, Object> variableListToMap(List<Variable> variables) {
        Map<String, Object> variableMap = new HashMap<String, Object>();
        for (Variable variable : variables) {
            variableMap.put(variable.getKey(), variable.getValue());
        }
        return variableMap;
    }

    private Property annotationToProperty(GraphQLProperty annotation) {
        Property property = new Property();
        property.setChildren(new HashMap<String, Property>());
        property.setArguments(new ArrayList<Argument>());

        if( annotation == null) {
            return property;
        }

        List<Argument> arguments = new ArrayList<Argument>();
        String name = annotation.name();
        property.setResourceName(name);

        for (GraphQLArgument graphQLArgument : annotation.arguments()) {
            arguments = setArgument(arguments, graphQLArgument);
        }

        property.setArguments(arguments);
        return property;
    }

    private boolean isList(Field field) {
        String simpleName = field.getType().getSimpleName();
        return simpleName.equalsIgnoreCase("ArrayList") ||
                simpleName.equalsIgnoreCase("List") ||
                field.getType().isArray();
    }

    private boolean isProperty(Field field) {
        return isProperty(field.getType());
    }

    private boolean isProperty(Class clazz) {
        if (scalars != null) {
            for (Class scalar : scalars) {
                if (scalar.equals(clazz)) return true;
            }
        }
        return clazz.isPrimitive() || clazz.getPackage().getName().equalsIgnoreCase("java.lang");
    }

    /**
     * Adds the arguments into the argument list from the GraphQLArgument annotation to provide the correct method
     *
     * @param arguments list of arguments to add the annotated argument to
     * @param graphQLArgument annotated argument to add to the request construction
     * @return List\<Argument>
     */
    private List<Argument> setArgument(List<Argument> arguments, GraphQLArgument graphQLArgument) {
        String type = graphQLArgument.type();
        Argument argument;
        if ("Boolean".equalsIgnoreCase(type)) {
            argument = new Argument<Boolean>(graphQLArgument.name(), Boolean.valueOf(graphQLArgument.value()));
        } else if ("Integer".equalsIgnoreCase(type)) {
            argument = new Argument<Integer>(graphQLArgument.name(), Integer.valueOf(graphQLArgument.value()));
        } else if ("Float".equalsIgnoreCase(type)) {
            argument = new Argument<Float>(graphQLArgument.name(), Float.valueOf(graphQLArgument.value()));
        } else {
            argument = new Argument<String>(graphQLArgument.name(), graphQLArgument.value());
        }

        argument.setVariable(graphQLArgument.variable());

        arguments.add(argument);
        return arguments;
    }

    /**
     * Recursively iterates over nested classes building the request tree while checking for any annotations to apply to
     * the fields.
     *
     * @param clazz the class used to construct the request
     * @param propertyVariables set of variables set on a GraphQL property or field defined in the class
     * @return Map\<String, Property>
     */
    private Map<String, Property> getChildren(Class clazz, Map<String, Object> propertyVariables) {
        Field[] declaredFields = clazz.getDeclaredFields();
        ArrayList<Field> fields = new ArrayList<Field>(Arrays.asList(declaredFields));
        Class<?> superClass = clazz.getSuperclass();
        while (!superClass.getPackage().getName().equalsIgnoreCase("java.lang")) {
            Field[] superClassFields = superClass.getDeclaredFields();
            fields.addAll(Arrays.asList(superClassFields));
            superClass = superClass.getSuperclass();
        }

        Map<String, Property> children = new HashMap<String, Property>();
        for (Field field : fields) {
            if (field.isSynthetic()) continue;

            GraphQLIgnore ignoreAnnotation = field.getAnnotation(GraphQLIgnore.class);
            if (ignoreAnnotation != null) {
                continue;
            }

            String propertyKey = field.getName();
            List<Argument> arguments = null;
            Property property = new Property();

            GraphQLProperty propertyAnnotation = field.getAnnotation(GraphQLProperty.class);
            if (propertyAnnotation != null) {
                arguments = new ArrayList<Argument>();
                String name = propertyAnnotation.name();
                property.setResourceName(name);
                propertyKey = field.getName();
                for (GraphQLArgument graphQLArgument : propertyAnnotation.arguments()) {
                    arguments = setArgument(arguments, graphQLArgument);
                }
            }
            GraphQLArguments argumentsAnnotation = field.getAnnotation(GraphQLArguments.class);
            if (argumentsAnnotation != null) {
                if (arguments == null) arguments = new ArrayList<Argument>();
                for (GraphQLArgument graphQLArgument : argumentsAnnotation.value()) {
                    arguments = setArgument(arguments, graphQLArgument);
                }
            }
            GraphQLArgument graphQLArgument = field.getAnnotation(GraphQLArgument.class);
            if (graphQLArgument != null) {
                if (arguments == null) arguments = new ArrayList<Argument>();
                arguments = setArgument(arguments, graphQLArgument);
            }

            GraphQLVariables variablesAnnotation = field.getAnnotation(GraphQLVariables.class);
            if (variablesAnnotation != null) {
                if (arguments == null) arguments = new ArrayList<Argument>();
                for (GraphQLVariable graphQLVariable : variablesAnnotation.value()) {
                    arguments.add(new Argument<String>(graphQLVariable.name(), "$" + graphQLVariable.name()));
                    propertyVariables.put("$" + graphQLVariable.name(), graphQLVariable.scalar());
                }
            }

            GraphQLVariable graphQLVariable = field.getAnnotation(GraphQLVariable.class);
            if (graphQLVariable != null) {
                if (arguments == null) arguments = new ArrayList<Argument>();
                arguments.add(new Argument<String>(graphQLVariable.name(), "$" + graphQLVariable.name()));
                propertyVariables.put("$" + graphQLVariable.name(), graphQLVariable.scalar());
            }

            property.setArguments(arguments);

            if (isList(field)) {
                Type type = field.getGenericType();
                if (type instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) type;
                    Class containedClass = (Class) pType.getActualTypeArguments()[0];
                    if (!isProperty(containedClass)) {
                        property.setChildren(getChildren(containedClass, propertyVariables));
                    }
                }
            } else if (!isProperty(field)) {
                property.setChildren(getChildren(field.getType(), propertyVariables));
            }
            children.put(propertyKey, property);
        }
        return children;
    }

    public static class RequestBuilder {

        URL url = null;
        String request = null;
        Class clazz = null;
        Map<String, String> headers = new HashMap<String, String>();
        List<Arguments> arguments = null;
        List<Variable> variables = new ArrayList<Variable>();
        List<Class> scalars = new ArrayList<Class>();

        RequestBuilder() { }

        public RequestBuilder url(String url) throws MalformedURLException {
            this.url = new URL(url);
            return this;
        }

        public RequestBuilder request(Class clazz) {
            this.clazz = clazz;
            return this;
        }

        public RequestBuilder request(String request) {
            this.request = request;
            return this;
        }

        public RequestBuilder arguments(List<Arguments> arguments) {
            this.arguments = arguments;
            return this;
        }

        public RequestBuilder arguments(Arguments... arguments) {
            this.arguments = Arrays.asList(arguments);
            return this;
        }

        public RequestBuilder variables(List<Variable> variables) {
            this.variables = variables;
            return this;
        }

        public RequestBuilder variables(Variable... variables) {
            this.variables = Arrays.asList(variables);
            return this;
        }

        public RequestBuilder scalars(List<Class> scalars) {
            this.scalars = scalars;
            return this;
        }

        public RequestBuilder scalars(Class... scalars) {
            this.scalars = Arrays.asList(scalars);
            return this;
        }

        public RequestBuilder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public GraphQLRequestEntity build() throws IllegalStateException {
            if (url == null) throw new IllegalStateException("url must be set");
            if (this.clazz == null && this.request == null) throw new IllegalStateException("request must be set");
            return new GraphQLRequestEntity(this);
        }
    }
}
