package com.mitrokhin.nick.dialer.infrastructure;


public class ContactItem {
    private String id;
    private String name;

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

