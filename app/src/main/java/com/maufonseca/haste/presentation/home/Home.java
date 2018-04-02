package com.maufonseca.haste.presentation.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.maufonseca.haste.R;
import com.maufonseca.haste.model.Rush;
import com.maufonseca.haste.model.RushList;
import com.maufonseca.haste.presentation.helper.RushBoxWorker;
import com.maufonseca.haste.presentation.helper.SignUpWorker;

public class Home extends AppCompatActivity {
  RushList rushes;
  RecyclerView recyclerView;
  TaskAdapter adapter;
  EditText fastCreateEditText;
  SignUpWorker signUpWorker;
  ProgressBar progressBar;
  HomePresenter homePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    signUpWorker = SignUpWorker.getInstance();
    fastCreateEditText = findViewById(R.id.new_task_edit_text);
    rushes = new RushList();
    adapter = new TaskAdapter(this, rushes);
    progressBar = findViewById(R.id.progressbar);
    recyclerView = findViewById(R.id.task_recyclerview);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.RIGHT) {
      private int pos1, pos2;
      @Override
      public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        pos1 = viewHolder.getAdapterPosition();
        pos2 = target.getAdapterPosition();
        return true; //true if assumed target position
      }

      @Override
      public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //awesome code to run when user drops card and completes reorder
        //homePresenter.changePositions(pos1, pos2);
      }

      @Override
      public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        deleteRush(viewHolder.getAdapterPosition());
      }
    };
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);
    homePresenter = new HomePresenter(this, rushes, signUpWorker);
    homePresenter.getCurrentUser();
  }

  @Override
  protected void onStart() {
    super.onStart();
    setupNewRushBox();
  }

  public void getRushesForUser() {
    homePresenter.getRushesForUser();
  }

  private void setupNewRushBox() {
    fastCreateEditText.setHint(RushBoxWorker.getInstance().getBoxHint(this));
  }

  public void showProgressBar() {
    progressBar.setVisibility(View.VISIBLE);
  }

  public void hideProgressBar() {
    progressBar.setVisibility(View.GONE);
  }

  public void refreshList() {
    adapter.notifyDataSetChanged();
  }

  public void showToast(CharSequence message) {
    Toast.makeText(Home.this, message, Toast.LENGTH_SHORT).show();
  }

  public void createRush(View v) {
    if(!fastCreateEditText.getText().toString().trim().isEmpty()) {
      homePresenter.createRush(fastCreateEditText.getText().toString());
      fastCreateEditText.setText("");
    }
  }

  public void deleteRush(int position) {
    homePresenter.deleteRush(position);
  }

  public void rushCheckTapped(View v) {
    Rush tappedRush = (Rush) v.getTag();
    homePresenter.checkRush(tappedRush);
  }
}
