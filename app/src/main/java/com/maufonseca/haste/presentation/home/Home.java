package com.maufonseca.haste.presentation.home;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maufonseca.haste.R;
import com.maufonseca.haste.model.Rush;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
  ArrayList<Rush> rushes;
  RecyclerView recyclerView;
  TaskAdapter adapter;
  EditText fastCreateEditText;
  FirebaseDatabase database;
  DatabaseReference rushesReference;
  TaskListEventListener taskListEventListener;
  FirebaseAuth firebaseAuth;
  ProgressBar progressBar;
  FirebaseUser currentUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    firebaseAuth = FirebaseAuth.getInstance();
    database = FirebaseDatabase.getInstance();
    fastCreateEditText = findViewById(R.id.new_task_edit_text);
    rushes = new ArrayList<>();
    adapter = new TaskAdapter(this, rushes);
    progressBar = findViewById(R.id.progressbar);
    recyclerView = findViewById(R.id.task_recyclerview);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    taskListEventListener = new TaskListEventListener(this, rushes, adapter);
    currentUser = firebaseAuth.getCurrentUser();
    if(currentUser==null) {
      signInAnonymously();
    } else {
      updateUI(currentUser);
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.

  }

  private void signInAnonymously() {
    progressBar.setVisibility(View.VISIBLE);
    firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
          // Sign in success, update UI with the signed-in user's information
          Log.d("FIREBASE", "signInAnonymously:success");
          FirebaseUser user = firebaseAuth.getCurrentUser();
          updateUI(user);
        } else {
          // If sign in fails, display a message to the user.
          Log.w("FIREBASE", "signInAnonymously:failure", task.getException());
          Toast.makeText(Home.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.GONE);
      }
    });
  }

  private void updateUI(FirebaseUser user) {
    if(user!=null) {
      String uid = user.getUid();
      rushesReference = database.getReference("users/"+uid+"/rushes");
      rushesReference.addChildEventListener(taskListEventListener);
    }
  }
  public void createTask(View v) {
    Rush newRush = new Rush();
    newRush.setDescription(fastCreateEditText.getText().toString());
    newRush.setDone(false);
    rushesReference.push().setValue(newRush);
    fastCreateEditText.setText("");
  }

  public void rushCheckTapped(View v) {
    Rush tappedRush = (Rush)v.getTag();
    tappedRush.setDone(!tappedRush.getDone());
    rushesReference.child(tappedRush.getId()).setValue(tappedRush);
  }
}
