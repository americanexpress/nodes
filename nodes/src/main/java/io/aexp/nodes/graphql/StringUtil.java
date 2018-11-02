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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class StringUtil {

    private StringUtil() {
    }

    /**
     * Joins and array to a delimited string
     *
     * @param deliminator the deliminator that the string should be concatenated with
     * @param array the array to be joined together
     * @return String
     */
    static String joinStringArray(String deliminator, List<String> array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : array) {
            if (stringBuilder.length() != 0) stringBuilder.append(deliminator);
            stringBuilder.append(item);
        }
        return stringBuilder.toString();
    }

    /**
     * Formats arguments to confirm with GraphQL specification based on the type
     *
     * @param key the key the parameter is assigned to
     * @param value the value to assign to the key
     * @return String
     */
    static <T> String formatGraphQLParameter(String key, T value) {
        StringBuilder stringBuilder = new StringBuilder();
        Pattern pattern = Pattern.compile("^\\$");
        Matcher matcher = pattern.matcher("" + value);
        if (value instanceof String && !matcher.find()) {
            stringBuilder.append(key).append(":\"").append(value).append("\"");
        } else if (value instanceof List) {
            stringBuilder.append(key).append(":").append(formatGraphQLArgumentList((List) value));
        } else if (value instanceof InputObject) {
            stringBuilder.append(key).append(":").append(((InputObject) value).getMessage());
        } else {
            stringBuilder.append(key).append(":").append(value);
        }
        return stringBuilder.toString();
    }

    /**
     * Formats a list of values to confirm with GraphQL specification based on the type of each value
     *
     * @param values the array of values to format
     * @return String
     */
    private static String formatGraphQLArgumentList(List values) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        Pattern p = Pattern.compile("^\\$");
        for (Object value: values) {
            if (stringBuilder.length() != 1) stringBuilder.append(",");
            Matcher m = p.matcher("" + value);
            if (value instanceof String && !m.find()) {
                stringBuilder.append("\"").append(value).append("\"");
            } else if (value instanceof InputObject) {
                stringBuilder.append(((InputObject) value).getMessage());
            } else {
                stringBuilder.append(value);
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
