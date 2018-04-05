package com.maufonseca.haste.presentation.home;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.maufonseca.haste.BuildConfig;
import com.maufonseca.haste.R;
import com.maufonseca.haste.model.Rush;
import com.maufonseca.haste.model.RushList;
import com.maufonseca.haste.presentation.helper.RushBoxWorker;
import com.maufonseca.haste.presentation.helper.SignUpWorker;

public class Home extends AppCompatActivity {
  RushList rushes;
  RecyclerView recyclerView;
  SwipeRefreshLayout swipeRefreshLayout;
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
    swipeRefreshLayout = findViewById(R.id.swipe_refresh);
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        homePresenter.getCurrentUser();
      }
    });
    homePresenter.getCurrentUser();
  }

  @Override
  protected void onStart() {
    super.onStart();
    setupNewRushBox();
  }

  public void showEmptyState() {

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

  public void stopSwipeRefresh() {
    swipeRefreshLayout.setRefreshing(false);
  }

  public void refreshList() {
    adapter.notifyDataSetChanged();
  }

  public void scrollToEnd(){
    recyclerView.scrollToPosition(adapter.getItemCount()-1);
  }

  public void clearTextBox() {
    fastCreateEditText.setText("");
  }

  public void showToast(CharSequence message) {
    Toast.makeText(Home.this, message, Toast.LENGTH_SHORT).show();
  }

  public void showVersion(View v) {
    showToast(getString(R.string.app_name) +" v"+ BuildConfig.VERSION_NAME);
  }

  public void createRush(View v) {
    if(!fastCreateEditText.getText().toString().trim().isEmpty()) {
      homePresenter.createRush(fastCreateEditText.getText().toString());
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
