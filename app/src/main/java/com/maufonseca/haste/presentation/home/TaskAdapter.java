package com.maufonseca.haste.presentation.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.maufonseca.haste.R;
import com.maufonseca.haste.model.Task;

import java.util.ArrayList;

/**
 * Created by mauricio on 26/03/18.
 */

public class TaskAdapter extends RecyclerView.Adapter {

  private ArrayList<Task> tasks;
  private Context context;

  public TaskAdapter(Context context, ArrayList<Task> tasks) {
    this.tasks = tasks;
    this.context = context;
  }

  @Override
  public int getItemCount() {
    return tasks.size();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_task, parent, false);
    return new Holder(view);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    Task currentTask = tasks.get(position);
    Holder vh = (Holder) holder;
    vh.description.setText(currentTask.getDescription());
    vh.box.setChecked(currentTask.getDone());
  }

  private class Holder extends RecyclerView.ViewHolder {
    CheckBox box;
    TextView description;

    public Holder(View itemView) {
      super(itemView);
      box = itemView.findViewById(R.id.checkbox);
      description = itemView.findViewById(R.id.description_textview);

    }
  }
}
