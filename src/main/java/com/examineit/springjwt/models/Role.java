package com.examineit.springjwt.models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "roles")
public class Role {

	@Id
	private long id;

	private ERole name;

	public Role() {
	}

	public Role(ERole name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ERole getName() {
		return name;
	}

	public void setName(ERole name) {
		this.name = name;
	}
}