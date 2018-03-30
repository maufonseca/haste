package com.maufonseca.haste.model;

/**
 * Created by mauricio on 26/03/18.
 */

public class Rush {
  private String description, id;
  private Boolean done;

  public Rush() {
    description = "";
    done = false;
    id = "";
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getDone() {
    return done;
  }

  public void setDone(Boolean done) {
    this.done = done;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
