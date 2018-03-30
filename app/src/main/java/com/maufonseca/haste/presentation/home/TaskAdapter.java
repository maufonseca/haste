package com.maufonseca.haste.presentation.home;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maufonseca.haste.R;
import com.maufonseca.haste.model.Rush;

import java.util.ArrayList;

/**
 * Created by mauricio on 26/03/18.
 */

public class TaskAdapter extends RecyclerView.Adapter {

  private ArrayList<Rush> rushes;
  private Context context;

  public TaskAdapter(Context context, ArrayList<Rush> rushes) {
    this.rushes = rushes;
    this.context = context;
  }

  @Override
  public int getItemCount() {
    return rushes.size();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_task, parent, false);
    return new Holder(view);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    Rush currentRush = rushes.get(position);
    Holder vh = (Holder) holder;
    vh.description.setText(currentRush.getDescription());
    if(currentRush.getDone()) {
      vh.description.setPaintFlags(vh.description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      vh.checkmark.setVisibility(View.VISIBLE);
      vh.layout.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple_cell_done));
    } else {
      vh.description.setPaintFlags(vh.description.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
      vh.checkmark.setVisibility(View.INVISIBLE);
      vh.layout.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple_cell_undone));
    }
    vh.layout.setTag(currentRush);
  }

  private class Holder extends RecyclerView.ViewHolder {
    ImageView checkmark;
    TextView description;
    LinearLayout layout;

    public Holder(View itemView) {
      super(itemView);
      layout = itemView.findViewById(R.id.cell_layout);
      checkmark = itemView.findViewById(R.id.check_image);
      description = itemView.findViewById(R.id.description_textview);

    }
  }
}
