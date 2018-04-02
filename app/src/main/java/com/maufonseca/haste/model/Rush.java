package com.maufonseca.haste.model;

/**
 * Created by mauricio on 26/03/18.
 */

public class Rush {
  private String description, id;
  private Boolean done;
  private int position;

  public Rush() {
    description = "";
    done = false;
    id = "";
    position = 0;
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

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }
  public void setPosition(long position) {
    this.position = (int)position;
  }
}
