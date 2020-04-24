package com.dev_app.todoapp;

public class Contacts {
    private	int	id;
    private	String name;
    private	String phno;

    public Contacts(String name, String phno) {
        this.name = name;
        this.phno = phno;
    }

    public Contacts(int id, String name, String phno) {
        this.id = id;
        this.name = name;
        this.phno = phno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }
}