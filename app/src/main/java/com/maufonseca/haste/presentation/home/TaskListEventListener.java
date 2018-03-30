package com.maufonseca.haste.presentation.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.maufonseca.haste.model.Rush;

import java.util.ArrayList;

/**
 * Created by mauricio on 27/03/18.
 */

public class TaskListEventListener implements ChildEventListener {

  String TAG = "FirebaseTaskListener";
  ArrayList<Rush> rushes;
  TaskAdapter adapter;
  Context context;

  public TaskListEventListener(Context context, ArrayList<Rush> rushes, TaskAdapter adapter) {
    this.rushes = rushes;
    this.adapter = adapter;
    this.context = context;
  }

  @Override
  public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
    Rush newRush = dataSnapshot.getValue(Rush.class);
    newRush.setId(dataSnapshot.getKey());
    rushes.add(newRush);
    adapter.notifyDataSetChanged();
  }

  @Override
  public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
    Rush editedRush = dataSnapshot.getValue(Rush.class);
    int index = 0;
    for(int i=0; i<rushes.size(); i++) {
      if(dataSnapshot.getKey().equals(rushes.get(i).getId())) {
        index = i;
        break;
      }
    }
    rushes.set(index,editedRush);
    adapter.notifyDataSetChanged();
  }

  @Override
  public void onChildRemoved(DataSnapshot dataSnapshot) {
    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
    Rush deletedRush = dataSnapshot.getValue(Rush.class);
    int index = 0;
    for(int i=0; i<rushes.size(); i++) {
      if(dataSnapshot.getKey().equals(rushes.get(i).getId())) {
        index = i;
        break;
      }
    }
    rushes.remove(index);
    adapter.notifyDataSetChanged();

  }

  @Override
  public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
    Rush movedRush = dataSnapshot.getValue(Rush.class);
    String taskKey = dataSnapshot.getKey();

  }

  @Override
  public void onCancelled(DatabaseError databaseError) {
    Log.w(TAG, "postTasks:onCancelled", databaseError.toException());
    Toast.makeText(context, "Failed to load task" + "s.",Toast.LENGTH_SHORT).show();
  }
}
