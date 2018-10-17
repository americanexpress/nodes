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

import io.aexp.nodes.graphql.internal.Error;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class GraphQLResponseEntity<T> {

    private Error[] errors;
    private Map<String, List<String>> headers;
    private T response;

    public Error[] getErrors() {
        return errors;
    }

    void setErrors(Error[] errors) {
        this.errors = errors;
    }

    public Map<String, List<String>> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    void setHeaders(Map<String, List<String>> headers) {
        this.headers = Collections.unmodifiableMap(headers);
    }

    public T getResponse() {
        return response;
    }

    void setResponse(T response) {
        this.response = response;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (headers == null) {
            builder.append("null");
        } else {
            for (Map.Entry entry : headers.entrySet()) {
                builder.append("[").append(entry.getKey()).append(":");
                for (String value : (List<String>) entry.getValue()) {
                    builder.append(value);
                }
                builder.append("]");
            }
        }

        return "GraphQLResponseEntity{" +
                "errors=" + Arrays.toString(errors) +
                ", headers=" + builder.toString() +
                ", response=" + response +
                '}';
    }
}
