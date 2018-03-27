package com.maufonseca.haste.model;

/**
 * Created by mauricio on 26/03/18.
 */

public class Task {
    String description;
    Boolean isDone;
    int id;

    public Task() {
        description = "";
        isDone = false;
        id = 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
