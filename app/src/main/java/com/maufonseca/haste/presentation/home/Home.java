package com.maufonseca.haste.presentation.home;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.maufonseca.haste.model.RushList;
import com.maufonseca.haste.presentation.helper.NetworkWorker;
import com.maufonseca.haste.presentation.helper.RushBoxWorker;

public class Home extends AppCompatActivity {
  RushList rushes;
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
    rushes = new RushList();
    adapter = new TaskAdapter(this, rushes);
    progressBar = findViewById(R.id.progressbar);
    recyclerView = findViewById(R.id.task_recyclerview);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,  ItemTouchHelper.RIGHT) {
      @Override
      public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //awesome code when user grabs recycler card to reorder
        return true; //true if assumed target position
      }

      @Override
      public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //awesome code to run when user drops card and completes reorder
      }

      @Override
      public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Rush swipedRush = rushes.get(viewHolder.getAdapterPosition());
        deleteRush(swipedRush);
      }
    };
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);

    taskListEventListener = new TaskListEventListener(this, rushes, adapter);
    currentUser = firebaseAuth.getCurrentUser();
    if(currentUser==null) {
      signInAnonymously();
    } else {
      updateUI(currentUser);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    setupNewRushBox();
  }

  private void setupNewRushBox() {
    fastCreateEditText.setHint(RushBoxWorker.getInstance().getBoxHint(this));
  }

  private void signInAnonymously() {
    if(NetworkWorker.getInstance().isOnline(this)) {
      progressBar.setVisibility(View.VISIBLE);
      firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          progressBar.setVisibility(View.GONE);
          if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Toast.makeText(Home.this, getText(R.string.success_silent_auth), Toast.LENGTH_SHORT).show();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            updateUI(user);
          } else {
            // If sign in fails, display a message to the user.
            Toast.makeText(Home.this, getText(R.string.error_silent_auth), Toast.LENGTH_SHORT).show();
          }
        }
      });
    }
  }

  private void updateUI(FirebaseUser user) {
    if(user!=null) {
      String uid = user.getUid();
      rushesReference = database.getReference("users/"+uid+"/rushes");
      rushesReference.addChildEventListener(taskListEventListener);
    }
  }
  public void createRush(View v) {
    if(!fastCreateEditText.getText().toString().isEmpty()) {
      Rush newRush = new Rush();
      newRush.setDescription(fastCreateEditText.getText().toString());
      newRush.setDone(false);
      rushesReference.push().setValue(newRush);
      fastCreateEditText.setText("");
    }
  }

  public void deleteRush(Rush rush) {
    rushesReference.child(rush.getId()).removeValue();
  }

  public void rushCheckTapped(View v) {
    Rush tappedRush = (Rush)v.getTag();
    tappedRush.setDone(!tappedRush.getDone());
    rushesReference.child(tappedRush.getId()).setValue(tappedRush);
  }
}
