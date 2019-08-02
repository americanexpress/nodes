# Nodes
> A GraphQL JVM Client - Java, Kotlin, Scala, etc.

[![Build Status](https://travis-ci.org/americanexpress/nodes.svg)](https://travis-ci.org/americanexpress/nodes)
[![Coverage Status](https://img.shields.io/coveralls/americanexpress/nodes.svg)](https://coveralls.io/github/americanexpress/nodes)
[![Download](https://api.bintray.com/packages/americanexpress/maven/io.aexp.nodes.graphql/images/download.svg)](https://bintray.com/americanexpress/maven/io.aexp.nodes.graphql/_latestVersion)

Nodes is a GraphQL client designed for constructing queries from standard model definitions. Making
this library suitable for any JVM application that wishes to interface with a GraphQL service in a
familiar way - a simple, flexible, compatible, adoptable, understandable library for everyone!

The Nodes library is intended to be used in a similar fashion to other popular API interfaces so 
that application architecture can remain unchanged whether interfacing with a REST, SOAP, GraphQL, 
or any other API specification. A request entity is built for handling request specific parameters; 
a template is used for executing that request and mapping the response to a response entity; a 
response entity is used to gather the results.

## Installing

Currently the library is hosted on bintray. This can be added to your installation repositories as demonstrated below.

_Maven_
```xml
<repositories>
    <repository>
        <id>bintray-americanexpress-maven</id>
        <url>https://dl.bintray.com/americanexpress/maven</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
      <groupId>io.aexp.nodes.graphql</groupId>
      <artifactId>nodes</artifactId>
      <version>latest</version>
    </dependency>
</dependencies>
```

_Gradle_
```
repositories {
    maven {
        url 'https://dl.bintray.com/americanexpress/maven/'
    }
}

dependencies {
    compile 'io.aexp.nodes.graphql:nodes:latest'
}
```

Replace _latest_ with the desired version to install. The versions available for installing can be found in the git tags, using semantic versioning.

## Usage

```Java
GraphQLTemplate graphQLTemplate = new GraphQLTemplate();

GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
    .url("http://graphql.example.com/graphql")
    .variables(new Variable<>("timeFormat", "MM/dd/yyyy"))
    .arguments(new Arguments("path.to.argument.property",
        new Argument<>("id", "d070633a9f9")))
    .scalars(BigDecimal.class)
    .request(SampleModel.class)
    .build();
GraphQLResponseEntity<SampleModel> responseEntity = graphQLTemplate.query(requestEntity, SampleModel.class);
```

### Annotations to configure fields

#### `@GraphQLArgument(name="name", value="defaultVal", type="String", optional=false)`
> Used above property fields<br><b>name (required)</b>: GraphQL argument name<br><b>value (optional)</b>: default value to set the argument to<br><b>type (optional)</b>: how to parse the optional value. Accepts an enum of "String", "Boolean", "Integer", or "Float" - defaults to be parsed as a string.<br><b>optional (optional)</b>: set to true if the value is optional and should be left out if value is null - defaults to false.<br>You can specify fields arguments directly inline using this. This is good for static field arguments such as result display settings, for instance the format of a date or the locale of a message.

*example:*
```java
    ...
    @GraphQLArgument(name="isPublic", value="true", type="Boolean")
    private User user;
    ...
```

*result:*
```
query {
    ...
    user(isPublic: true) {
        ...
```

#### `@GraphQLArguments({@GraphQLArgument})`
> Used above property fields<br>Annotation for allowing mutliple default argument descriptions on one field

*example:*
```java
    ...
    @GraphQLArguments({
        @GraphQLArgument(name="isPublic", value="true", type="Boolean"),
        @GraphQLArgument(name="username")
    })
    private User user;
    ...
```

*result:*
```
query {
    ...
    user(isPublic: true, username: null) {
        ...
```

#### `@GraphQLIgnore`
> Used above property fields<br>Annotation to ignore the field when constructing the schema

*example:*
```java
    ...
    @GraphQLIgnore
    private User user;
    ...
```

*result:*
```
query {
    ...
```

#### `@GraphQLProperty(name="name", arguments={@GraphQLArgument(...)})`
> Used above property and class fields<br><b>name (required)</b>: GraphQL schema field name, the property's field name will be used as the alias<br><b>arguments (optional)</b>: arguments for the specified graphQL schema.<br>When used above property fields the annotation simply aliases that field, but when used above class fields it will replace the defined class name.

*example:*
```java
    ...
    @GraphQLProperty(name="myFavoriteUser", arguments={
        @GraphQLArgument(name="username", value="amex")
    })
    private User user;
    ...
```

*result:*
```
query {
    ...
    myFavoriteUser: user(username: "amex") {
        ...
```

#### `@GraphQLVariable(name="name", scalar="Float!")`
> Used above property fields<br><b>name (required)</b>: GraphQL variable name<br><b>type (required)</b>: GraphQL scalar type. Including optional and required parsing (!)<br>This is good for sharing the same variables across multiple input parameters in a query.

*example:*
```java
    ...
    @GraphQLVariable(name="isPublic", scalar="Boolean")
    private User user;
    ...
```

*result:*
```
query($isPublic: Boolean) {
    ...
    user(isPublic: $isPublic) {
        ...
```

#### `@GraphQLVariables({@GraphQLVariable})`
> Used above property fields<br>Annotation for allowing mutliple variables for a given field.

*example:*
```java
    ...
    @GraphQLVariables({
        @GraphQLVariable(name="isPublic", scalar="Boolean"),
        @GraphQLVariable(name="username", scalar="String!")
    })
    private User user;
    ...
```

*result:*
```
query($isPublic: Boolean, $username: String!) {
    ...
    user(isPublic: $isPublic, username: $username) {
        ...
```

## Terminology

All language found in this library is aimed to align with the language used in the GraphQL
specification. An example of this is GraphQL vs GraphQl, where in this library the GraphQL
specification is favored over the Java standard.

----------

## Contributing
We welcome Your interest in the American Express Open Source Community on Github.
Any Contributor to any Open Source Project managed by the American Express Open
Source Community must accept and sign an Agreement indicating agreement to the
terms below. Except for the rights granted in this Agreement to American Express
and to recipients of software distributed by American Express, You reserve all
right, title, and interest, if any, in and to Your Contributions. Please [fill
out the Agreement](https://cla-assistant.io/americanexpress/nodes).

Please feel free to open pull requests and see [CONTRIBUTING.md](./CONTRIBUTING.md) for commit formatting details.

## License
Any contributions made under this project will be governed by the [Apache License 2.0](./LICENSE.txt).

## Code of Conduct
This project adheres to the [American Express Community Guidelines](./CODE_OF_CONDUCT.md). By participating, you are
expected to honor these guidelines.
