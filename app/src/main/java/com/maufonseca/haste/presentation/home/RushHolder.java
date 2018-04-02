package com.maufonseca.haste.presentation.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maufonseca.haste.R;

/**
 * Created by mauricio on 01/04/18.
 */

public class RushHolder extends RecyclerView.ViewHolder {
  ImageView checkmark;
  TextView description;
  LinearLayout layout;

  public RushHolder(View itemView) {
    super(itemView);
    layout = itemView.findViewById(R.id.cell_layout);
    checkmark = itemView.findViewById(R.id.check_image);
    description = itemView.findViewById(R.id.description_textview);
  }
}
