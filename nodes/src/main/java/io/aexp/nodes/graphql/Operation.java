package io.aexp.nodes.graphql;

import io.aexp.nodes.graphql.exceptions.GraphQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Operation {
    private String name;
    private GraphQLTemplate.GraphQLMethod method;
    private Map<String, Object> variables;
    private Map<String, Property> children;
    private StringBuilder message = new StringBuilder();

    public Operation() {
        this.name = null;
        this.variables = null;
        this.children = new HashMap<String, Property>();
        this.method = GraphQLTemplate.GraphQLMethod.QUERY;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public void setChildren(Map<String, Property> children) {
        this.children = children;
    }

    public void setMethod(GraphQLTemplate.GraphQLMethod method) {
        this.method = method;
    }

    public Map<String, Property> getChildren() {
        return children;
    }

    protected String getMessage() {
        // fail early
        if(children == null) {
            throw new GraphQLException("Operation must specify one or more properties");
        }

        if(method != null) {
            message.append(method.getValue()).append(" ");
        }

        if(name != null) {
            message.append(name).append(" ");
        }

        if (variables != null && !variables.isEmpty()) {
            message.append("(");
            List<String> variableList = new ArrayList<String>();
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                variableList.add(entry.getKey() + ":" + entry.getValue());
            }
            message.append(StringUtil.joinStringArray(",", variableList));
            message.append(")");
        }

        // process children
        message.append("{ ");
        for (Map.Entry<String, Property> entry : children.entrySet()) {
            message.append(entry.getValue().getMessage(entry.getKey()));
        }
        message.append("} ");

        // return copy of message & rest
        String res = message.toString();
        message.setLength(0);
        return res;
    }

    @Override
    public String toString() {
        return String.format("Operations{" +
                "name='%s', " +
                "method='%s', " +
                "variables='%s', " +
                "children='%s', " +
                "message='%s'" +
            "}",
            name,
            method,
            variables,
            children,
            getMessage()
            );
    }
}
