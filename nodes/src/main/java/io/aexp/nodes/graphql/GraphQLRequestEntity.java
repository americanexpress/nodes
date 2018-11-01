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

import io.aexp.nodes.graphql.annotations.GraphQLArgument;
import io.aexp.nodes.graphql.annotations.GraphQLArguments;
import io.aexp.nodes.graphql.annotations.GraphQLIgnore;
import io.aexp.nodes.graphql.annotations.GraphQLProperty;
import io.aexp.nodes.graphql.annotations.GraphQLVariable;
import io.aexp.nodes.graphql.annotations.GraphQLVariables;
import io.aexp.nodes.graphql.exceptions.GraphQLException;

public final class GraphQLRequestEntity {

    public static RequestBuilder Builder() {
        return new RequestBuilder();
    }

    private final URL url;
    private final Map<String, String> headers;
    private final Map<String, Object> variables;
    private final Property property = new Property();
    private final List<Class> scalars;
    private GraphQLTemplate.GraphQLMethod requestMethod = GraphQLTemplate.GraphQLMethod.QUERY;
    private String request;

    GraphQLRequestEntity(RequestBuilder builder) {
        this.url = builder.url;
        this.scalars = Collections.unmodifiableList(builder.scalars);
        this.headers = Collections.unmodifiableMap(builder.headers);
        this.variables = Collections.unmodifiableMap(variableListToMap(builder.variables));
        if (builder.request != null) {
            this.request = builder.request;
        } else {
            setPropertiesFromClass(builder.clazz);
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
        return property.getMessage(null);
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public List<Class> getScalars() {
        return scalars;
    }

    void setRequestMethod(GraphQLTemplate.GraphQLMethod requestMethod) {
        this.requestMethod = requestMethod;
        property.setMethod(requestMethod);
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
        Property argProp = property;
        String[] path = dotPath.split("\\.");
        for (String key: path) {
            argProp = argProp.getChildren().get(key);
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

    /**
     * Builds the request tree from the provided class.
     *
     * @param clazz the class used to construct the request
     */
    private void setPropertiesFromClass(Class clazz) {
        Map<String, Object> propertyVariables = new HashMap<String, Object>();
        Map<String, Property> children = this.getChildren(clazz, propertyVariables);
        GraphQLProperty graphQLProperty = (GraphQLProperty) clazz.getAnnotation(GraphQLProperty.class);
        if (graphQLProperty != null) {
            Property resourceProperty = new Property();
            List<Argument> arguments = null;
            String resourceName = graphQLProperty.name();
            if (graphQLProperty.arguments().length > 0) {
                arguments = new ArrayList<Argument>();
                for (GraphQLArgument graphQLArgument : graphQLProperty.arguments()) {
                    arguments = setArgument(arguments, graphQLArgument);
                }
            }
            resourceProperty.setArguments(arguments);
            resourceProperty.setChildren(children);
            Map<String, Property> resourceChild = new HashMap<String, Property>();
            resourceChild.put(resourceName, resourceProperty);
            children = resourceChild;
        }
        property.setChildren(Collections.unmodifiableMap(children));
        property.setMethod(requestMethod);
        property.setVariables(Collections.unmodifiableMap(propertyVariables));
    }

    private Map<String, Object> variableListToMap(List<Variable> variables) {
        Map<String, Object> variableMap = new HashMap<String, Object>();
        for (Variable variable : variables) {
            variableMap.put(variable.getKey(), variable.getValue());
        }
        return variableMap;
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
     * Adds the arguments into the argument list from the GraphQLArgument annotation to provide the correct type
     *
     * @param arguments list of arguments to add the annotated argument to
     * @param graphQLArgument annotated argument to add to the request construction
     * @return List\<Argument>
     */
    private List<Argument> setArgument(List<Argument> arguments, GraphQLArgument graphQLArgument) {
        String type = graphQLArgument.type();
        boolean optional = graphQLArgument.optional();
        String value = valueOf(graphQLArgument.value());
        if ("Boolean".equalsIgnoreCase(type)) {
            arguments.add(new Argument<Boolean>(graphQLArgument.name(), value == null ? null : Boolean.valueOf(value), optional));
        } else if ("Integer".equalsIgnoreCase(type)) {
            arguments.add(new Argument<Integer>(graphQLArgument.name(), value == null ? null : Integer.valueOf(value), optional));
        } else if ("Float".equalsIgnoreCase(type)) {
            arguments.add(new Argument<Float>(graphQLArgument.name(), value == null ? null : Float.valueOf(value), optional));
        } else {
            arguments.add(new Argument<String>(graphQLArgument.name(), value, optional));
        }
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
            if (field.isSynthetic() || field.isEnumConstant()) continue;

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

    private static String valueOf(String value) {
        if (value == null || "null".equals(value)) {
            return null;
        }
        return value;
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
