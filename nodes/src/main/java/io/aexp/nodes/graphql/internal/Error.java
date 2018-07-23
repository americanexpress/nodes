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

package io.aexp.nodes.graphql.internal;

import java.util.Arrays;
import java.util.Map;
import java.util.List;

public final class Error {

    private Map<String, Object> extensions;
    private Location[] locations;
    private String errorType;
    private String message;
    private List<Object> path;

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    public Location[] getLocations() {
        return locations;
    }

    void setLocations(Location[] locations) {
        this.locations = locations;
    }

    public String getErrorType() {
        return errorType;
    }

    void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    public List<Object> getPath() {
        return path;
    }

    void setPath(List<Object> path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Error{" +
                "message='" + message + '\'' +
                ", locations=" + Arrays.toString(locations) +
                ", extensions=" + extensions +
                ", path=" + path +
                ", errorType='" + errorType + '\'' +
                '}';
    }
}
