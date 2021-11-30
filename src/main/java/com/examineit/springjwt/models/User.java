package com.examineit.springjwt.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
public class User {

	private Set<Role> roles = new HashSet<>();

//	@Transient
	public static final String SEQUENCE_NAME = "users_sequence";

	@Id
	private long id;

	private String username;

	private String email;

	private String password;

	public User(long id, String username, String email, String password) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public static String getSequenceName() {
		return SEQUENCE_NAME;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
