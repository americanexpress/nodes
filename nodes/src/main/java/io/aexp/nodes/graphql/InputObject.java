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

import java.util.HashMap;
import java.util.Map;

import static io.aexp.nodes.graphql.StringUtil.formatGraphQLParameter;

public final class InputObject<T> {

    private Map<String, T> map;

    InputObject(Builder<T> builder) {
        map = builder.map;
    }

    protected String getMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (Map.Entry<String, T> entry : map.entrySet()) {
            if (stringBuilder.length() != 1) stringBuilder.append(",");
            stringBuilder.append(formatGraphQLParameter(entry.getKey(), entry.getValue()));
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static class Builder<T> {

        private final Map<String, T> map = new HashMap<String, T>();

        public Builder<T> put(String key, T value) {
            map.put(key, value);
            return this;
        }

        public InputObject<T> build() {
            return new InputObject<T>(this);
        }
    }

    public Map getMap() {
        return map;
    }
}
