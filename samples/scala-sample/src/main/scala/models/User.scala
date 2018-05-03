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

import io.aexp.nodes.graphql.annotations.{GraphQLArgument, GraphQLProperty, GraphQLVariable}

@GraphQLProperty(name = "user", arguments = Array(
  new GraphQLArgument(name = "login")))
class User {

  var name: String = _
  var location: String = _
  @GraphQLVariable(name = "isFork", scalar = "Boolean!")
  var repositories: RepositoryConnection = _

  def getName: String = {
    name
  }

  def getLocation: String = {
    location
  }

  def getRepositories: RepositoryConnection = {
    repositories
  }

  override def toString = s"User(name=$name, location=$location, repositories=$repositories)"
}
