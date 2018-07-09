package io.aexp.nodes.graphql.models;

import io.aexp.nodes.graphql.GraphQLTemplate;
import io.aexp.nodes.graphql.annotations.*;

@GraphQLOperation(name = "TestOperationName", method = GraphQLTemplate.GraphQLMethod.QUERY,
    variables = {
        @GraphQLVariable(name = "inputTestName", scalar = "String!")
    },
    property = @GraphQLProperty(name = "FindTest", arguments = {
        @GraphQLArgument(name = "testName", type = "String", variable="inputTestName")
    })
)
public class TestOperation {
    public String aStringProperty;
}
