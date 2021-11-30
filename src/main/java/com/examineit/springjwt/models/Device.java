package com.examineit.springjwt.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "devices")
public class Device {

    @Transient
    public static final String SEQUENCE_NAME = "device_sequence";

    @Id
    private long id;

    private String name;

    private String timeZone;

    private Set<String> usersPermitted = new HashSet<>();

    private org.bson.Document data;

    private String token;

    private List<org.bson.Document> comments;

    public List<org.bson.Document> getComments() {
        return comments;
    }

    public void setComments(List<org.bson.Document> comments) {
        this.comments = comments;
    }

    public Device() {
    }

    public Device(long id, String name){
        this.id = id;
        this.name = name;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static String getSequenceName() {
        return SEQUENCE_NAME;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<String> getUsersPermitted() {
        return usersPermitted;
    }

    public void setUsersPermitted(Set<String> usersPermitted) {
        this.usersPermitted = usersPermitted;
    }

    public org.bson.Document getData() {
        return data;
    }

    public void setData(org.bson.Document data) {
        this.data = data;
    }

}
