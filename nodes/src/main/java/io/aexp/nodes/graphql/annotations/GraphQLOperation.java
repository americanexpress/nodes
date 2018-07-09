package io.aexp.nodes.graphql.annotations;

import io.aexp.nodes.graphql.GraphQLTemplate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface GraphQLOperation {

    String name();
    GraphQLTemplate.GraphQLMethod method() default GraphQLTemplate.GraphQLMethod.QUERY;
    GraphQLVariable[] variables() default {};
    GraphQLProperty property();
}
