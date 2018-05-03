
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

import java.util

import io.aexp.nodes.graphql.{Argument, Arguments, GraphQLRequestEntity, GraphQLTemplate, Variable}
import models.User

object Main extends App {
  val YOUR_AUTH_TOKEN = "eeb7987aef9ccd7440a49cae2acc7f58bb415059"

  val headers = new util.HashMap[String, String]()
  headers.put("Authorization", "bearer " + YOUR_AUTH_TOKEN)

  val template = new GraphQLTemplate()
  val requestEntity: GraphQLRequestEntity = GraphQLRequestEntity.Builder()
      .url("https://api.github.com/graphql")
      .request(classOf[User])
      .headers(headers)
      .arguments(new Arguments("user", new Argument("login", "chemdrew")))
      .variables(new Variable("isFork", false))
      .scalars(classOf[BigDecimal], classOf[BigInt])
      .build()
  val responseEnitity = template.query(requestEntity, classOf[User])

  println("request: " + requestEntity.getRequest)
  println("response: " + responseEnitity.getResponse)
}