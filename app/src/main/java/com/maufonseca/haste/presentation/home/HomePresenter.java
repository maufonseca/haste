package com.maufonseca.haste.presentation.home;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maufonseca.haste.model.Rush;
import com.maufonseca.haste.model.RushList;
import com.maufonseca.haste.presentation.helper.SignUpWorker;

/**
 * Created by mauricio on 30/03/18.
 */

public class HomePresenter {
  private RushList rushes;
  private Home home;
  private FirebaseUser currentUser;
  private SignUpWorker signUpWorker;
  private FirebaseDatabase database;
  private DatabaseReference rushesReference;
  private TaskListEventListener taskListEventListener;

  public HomePresenter(Home home, RushList rushes, SignUpWorker signUpWorker, TaskListEventListener taskListener) {
    this.home = home;
    this.signUpWorker = signUpWorker;
    database = FirebaseDatabase.getInstance();
    this.rushes = rushes;
    this.taskListEventListener = taskListener;
  }

  public void getCurrentUser() {
    currentUser = signUpWorker.getCurrentUser();
    if(currentUser==null) {
      signUpWorker.signInAnonymously(home);
    } else {
     setupFirebaseRealtimeDB(currentUser.getUid());
    }
  }

  public void setupFirebaseRealtimeDB(String uid) {
    rushesReference = database.getReference("users/"+uid+"/rushes");
    rushesReference.addChildEventListener(taskListEventListener);
  }
  public void createRush(String description) {
    Rush newRush = new Rush();
    newRush.setDescription(description);
    newRush.setDone(false);
    newRush.setPosition(rushes.size());
    rushesReference.push().setValue(newRush);
  }

  public void deleteRush(int position) {
    Rush rush = rushes.get(position);
    rushesReference.child(rush.getId()).removeValue();
  }

  public void checkRush(Rush tappedRush) {
    tappedRush.setDone(!tappedRush.getDone());
    rushesReference.child(tappedRush.getId()).setValue(tappedRush);
  }
}
