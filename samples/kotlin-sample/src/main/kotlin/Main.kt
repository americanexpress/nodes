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

import io.aexp.nodes.graphql.GraphQLTemplate
import io.aexp.nodes.graphql.Argument
import io.aexp.nodes.graphql.Arguments
import io.aexp.nodes.graphql.GraphQLRequestEntity
import io.aexp.nodes.graphql.Variable
import models.User
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

// https://developer.github.com/v4/guides/forming-calls/#authenticating-with-graphql
const val YOUR_AUTH_TOKEN = "eeb7987aef9ccd7440a49cae2acc7f58bb415059"

fun main(args: Array<String>) {
    val headers = HashMap<String, String>()
    headers.put("Authorization", "bearer " + YOUR_AUTH_TOKEN)

    val template = GraphQLTemplate()
    val requestEntity = GraphQLRequestEntity.Builder()
            .url("https://api.github.com/graphql")
            .request(User::class.java)
            .headers(headers)
            .arguments(Arguments("user", Argument("login", "chemdrew")))
            .variables(Variable("isFork", false))
            .scalars(BigDecimal::class.java, BigInteger::class.java)
            .build()
    val responseEnitity = template.query(requestEntity, User::class.java)

    println("request: " + requestEntity.request)
    println("response: " + responseEnitity.response)
}
