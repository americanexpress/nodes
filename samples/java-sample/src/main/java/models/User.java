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

package models;

import io.aexp.nodes.graphql.annotations.GraphQLArgument;
import io.aexp.nodes.graphql.annotations.GraphQLProperty;
import io.aexp.nodes.graphql.annotations.GraphQLVariable;

@GraphQLProperty(name = "user", arguments = { @GraphQLArgument(name = "login") })
public class User {
	private String name;
	private String location;
	@GraphQLVariable(name = "isFork", scalar = "Boolean!")
	private RepositoryConnection repositories;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public RepositoryConnection getRepositories() {
		return repositories;
	}

	public void setRepositories(RepositoryConnection repositories) {
		this.repositories = repositories;
	}

	@Override
	public String toString() {
		return String.format("name=%s, location=%s, repositories=%s", name, location, repositories);
	}

}
