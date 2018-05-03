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

public final class Location {

    private String line;
    private String column;

    public String getLine() {
        return line;
    }

    void setLine(String line) {
        this.line = line;
    }

    public String getColumn() {
        return column;
    }

    void setColumn(String column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "Location{" +
                "line='" + line + '\'' +
                ", column='" + column + '\'' +
                '}';
    }
}
