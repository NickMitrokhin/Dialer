package com.mitrokhin.nick.dialer.models;


public class ContactItem {
    private final String id;
    private final String name;

    public ContactItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

