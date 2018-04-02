package com.maufonseca.haste.model;

import java.util.ArrayList;

/**
 * Created by mauricio on 01/04/18.
 */

public class RushList {
  public ArrayList<Rush> rushes;

  public RushList() {
    rushes = new ArrayList<>();
  }

  public void changeRushes(int position1, int position2) {
    rushes.get(position1).setPosition(position2);
    rushes.get(position2).setPosition(position1);
    reorder();
  }

  public void reorder() {
    ArrayList<Rush> orderedArray = new ArrayList<>();
    for(Rush rush : rushes) {
      orderedArray.add(rush.getPosition(), rush);
    }
    rushes.clear();
    rushes.addAll(orderedArray);
  }

  public void set(int index, Rush element) {
    rushes.set(index, element);
  }

  public Rush get(int position) {
    return rushes.get(position);
  }

  public void add(Rush element) {
    rushes.add(element);
  }

  public void add(int index, Rush element) {
    rushes.add(element);
  }

  public void remove(int index) {
    rushes.remove(index);
  }

  public int size() {
    return rushes.size();
  }
}
