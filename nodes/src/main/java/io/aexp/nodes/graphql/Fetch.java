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
import io.aexp.nodes.graphql.internal.ObjectMapperFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

final class Fetch {

    private final ObjectMapperFactory objectMapperFactory;
    private ObjectMapper mapper;
    private SimpleModule module;
    private static final int STATUS_CODE_THRESHOLD = 400;

    public Fetch(ObjectMapperFactory objectMapperFactory) {
        this.objectMapperFactory = objectMapperFactory;
    }

    <T> GraphQLResponseEntity<T> send(GraphQLRequestEntity requestEntity, Class<T> responseClass) throws GraphQLException {
        mapper = objectMapperFactory.newSerializerMapper();
        module = new SimpleModule();

        Request request = new Request();
        request.setQuery(requestEntity.getRequest());
        request.setVariables(requestEntity.getVariables());

        String responseMessage = null;
        String responseStatus = null;

        try {
            String requestParams = mapper.writeValueAsString(request);
            byte[] postData = requestParams.getBytes();
            HttpURLConnection connection = createConnection(requestEntity.getUrl(), postData, requestEntity.getHeaders());

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(postData);
            dataOutputStream.close();

            Map<String, List<String>> responseHeaders = connection.getHeaderFields();
            responseMessage = connection.getResponseMessage();
            responseStatus = Integer.toString(connection.getResponseCode());

            InputStream inputStream;
            try {
                inputStream = connection.getInputStream();
            } catch(IOException exception) {
                inputStream = connection.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            Wrapper<T> wrapper = deserializeResponse(bufferedReader, responseClass);

            if (connection.getResponseCode() >= STATUS_CODE_THRESHOLD) {
                GraphQLException graphQLException = new GraphQLException(responseMessage);
                graphQLException.setStatus(responseStatus);
                graphQLException.setErrors(wrapper.getErrors());
                throw graphQLException;
            }

            GraphQLResponseEntity<T> graphQLResponseEntity = new GraphQLResponseEntity<T>();
            graphQLResponseEntity.setErrors(wrapper.getErrors());
            if (wrapper.getData() != null) {
                graphQLResponseEntity.setHeaders(responseHeaders);
                graphQLResponseEntity.setResponse(wrapper.getData().getResource());
            }
            return graphQLResponseEntity;
        } catch (Exception exception) {
            if (exception instanceof GraphQLException) throw (GraphQLException) exception;
            GraphQLException err = new GraphQLException();
            err.setStatus(responseStatus);
            err.setMessage(responseMessage);
            err.setDescription(exception.getMessage());
            throw err;
        }
    }

    private HttpURLConnection createConnection(URL requestUrl, byte[] postData, Map<String, String> headers) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        int postDataLength = postData.length;
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        connection.setUseCaches(false);
        return connection;
    }

    private <T> Wrapper<T> deserializeResponse(BufferedReader bufferedReader, Class<T> responseClass) throws IOException {
        Deserializer<T> deserializer = new Deserializer<T>(responseClass, objectMapperFactory);
        module.addDeserializer(Resource.class, deserializer);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(module);
        return mapper.readValue(bufferedReader, Wrapper.class);
    }

}
