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

import io.aexp.nodes.graphql.exceptions.GraphQLException;
import io.aexp.nodes.graphql.internal.DefaultObjectMapperFactory;

public class GraphQLTemplate {

    private Fetcher fetch;

    public enum GraphQLMethod {
        QUERY("query"),
        MUTATE("mutation");

        private String value;
        GraphQLMethod(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    /**
     * Constructs a new GraphQL template instance using the default ObjectMapper factory.
     */
    public GraphQLTemplate() {
        this(new DefaultObjectMapperFactory());
    }

    /**
     * Constructs a new GraphQL template instance using the specified ObjectMapper factory.
     * @param objectMapperFactory factory class used for creating ObjectMapper instances
     */
    public GraphQLTemplate(final ObjectMapperFactory objectMapperFactory) {
        this(new Fetch(objectMapperFactory));
    }

    /**
     * Constructs a new GraphQL template instance using the specified ObjectMapper factory.
     * @param fetcher custom fetch provider
     */
    public GraphQLTemplate(final Fetcher fetcher) {
        fetch = fetcher;
    }

    /**
     * Constructs a new GraphQL template instance using the default ObjectMapper factory,
     * connect and read timeout for the underlying HttpURLConnection.
     * @param connectTimeout Sets a specified timeout value, in milliseconds, to be used
     *     when opening a communications link
     * @param readTimeout Sets the read timeout to a specified timeout, in milliseconds
     */
    public GraphQLTemplate(int connectTimeout, int readTimeout) {
        this(new DefaultObjectMapperFactory(), connectTimeout, readTimeout);
    }

    /**
     * Constructs a new GraphQL template instance using the specified ObjectMapper factory,
     * connect and read timeout for the underlying HttpURLConnection.
     * @param objectMapperFactory factory class used for creating ObjectMapper instances
     * @param connectTimeout Sets a specified timeout value, in milliseconds, to be used
     *      when opening a communications link
     * @param readTimeout Sets the read timeout to a specified timeout, in milliseconds
     */
    public GraphQLTemplate(final ObjectMapperFactory objectMapperFactory, int connectTimeout, int readTimeout) {
        fetch = new Fetch(objectMapperFactory, connectTimeout, readTimeout);
    }

    /**
     * Execute a GraphQL query request.
     * @param requestEntity request entity to be executed upon
     * @param responseClass response from the execution
     * @return GraphQLResponseEntity\<T>
     */
    public <T> GraphQLResponseEntity<T> query(GraphQLRequestEntity requestEntity, Class<T> responseClass) throws GraphQLException {
        return execute(GraphQLMethod.QUERY, requestEntity, responseClass);
    }

    /**
     * Execute a GraphQL mutation request.
     * @param requestEntity request entity to be executed upon
     * @param responseClass response from the execution
     * @return GraphQLResponseEntity\<T>
     */
    public <T> GraphQLResponseEntity<T> mutate(GraphQLRequestEntity requestEntity, Class<T> responseClass) throws GraphQLException {
        return execute(GraphQLMethod.MUTATE, requestEntity, responseClass);
    }

    /**
     * Execute any static GraphQL request.
     * <p>
     * This method is intended for making requests using plain text input instead of dynamically building the request
     * from a class.
     *
     * @param requestEntity request entity to be executed upon
     * @param responseClass response from the execution
     * @return GraphQLResponseEntity\<T>
     */
    public <T> GraphQLResponseEntity<T> execute(GraphQLRequestEntity requestEntity, Class<T> responseClass) throws GraphQLException {
        return execute(null, requestEntity, responseClass);
    }

    /**
     * Execute any GraphQL request.
     *
     * @param method sets the request execution verb, optionally null when sending a fully built static request query
     * @param requestEntity request entity to be executed upon
     * @param responseClass response from the execution
     * @return GraphQLResponseEntity\<T>
     */
    public <T> GraphQLResponseEntity<T> execute(GraphQLMethod method, GraphQLRequestEntity requestEntity, Class<T> responseClass)
            throws GraphQLException {
        if (null == requestEntity) {
            throw new GraphQLException("requestEntity must not be null");
        }
        if (null != method) requestEntity.setRequestMethod(method);
        return fetch.send(requestEntity, responseClass);
    }
}
