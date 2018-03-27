package com.maufonseca.haste.presentation.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.maufonseca.haste.R;
import com.maufonseca.haste.model.Task;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
  ArrayList<Task> tasks;
  RecyclerView recyclerView;
  TaskAdapter adapter;
  EditText fastCreateEditText;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    fastCreateEditText = findViewById(R.id.new_task_edit_text);
    tasks = new ArrayList<>();
    adapter = new TaskAdapter(this, tasks);
    recyclerView = findViewById(R.id.task_recyclerview);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    getTasks();
  }

  private void getTasks() {
    Task task1 = new Task();
    task1.setDescription(" task bla bla bla bla bla bla bla bla bla bla bla blhassu blashduy");
    tasks.add(task1);
    tasks.add(task1);
    tasks.add(task1);
    tasks.add(task1);
    adapter.notifyDataSetChanged();
  }

  public void fastCreateTask(View v) {
    Task newTask = new Task();
    newTask.setDescription(fastCreateEditText.getText().toString());
    newTask.setDone(false);
    tasks.add(newTask);
    adapter.notifyDataSetChanged();
  }
}
