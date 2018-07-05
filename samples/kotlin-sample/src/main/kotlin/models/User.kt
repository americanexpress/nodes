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

package models

import io.aexp.nodes.graphql.annotations.GraphQLArgument
import io.aexp.nodes.graphql.annotations.GraphQLProperty
import io.aexp.nodes.graphql.annotations.GraphQLVariable

@GraphQLProperty(name = "user", arguments = arrayOf(
        GraphQLArgument(name = "login")))
class User(val name: String? = null, val location: String? = null, @GraphQLVariable(name = "isFork", scalar = "Boolean!") val repositories: RepositoryConnection? = null) {

    override fun toString(): String {
        return "User(name=$name, location=$location, repositories=$repositories)"
    }
}
