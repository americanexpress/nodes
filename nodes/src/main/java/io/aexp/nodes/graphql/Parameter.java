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

class Parameter<T> {
    private String key;
    private T value;

    Parameter(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return toString("Parameter", null);
    }

    String toString(String name, String extraFields) {
        StringBuilder builder = new StringBuilder(name);
        builder.append('{');
        builder.append("key='").append(key).append('\'');
        builder.append(", value=").append(value);
        if (extraFields != null) {
            builder.append(", ").append(extraFields);
        }
        builder.append('}');
        return builder.toString();
    }
}
