package com.maufonseca.haste.presentation.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.maufonseca.haste.R;

/**
 * Created by mauricio on 01/04/18.
 */

public class RushBoxWorker {
  private static RushBoxWorker singleton;

  private RushBoxWorker() {
  }

  public static synchronized RushBoxWorker getInstance() {
    if (singleton == null)
      singleton = new RushBoxWorker();
    return singleton;
  }

  public String getBoxHint(Context context) {
    int aleatory = (int)(System.currentTimeMillis()%10);
    switch (aleatory) {
      case 0:
        return context.getString(R.string.new_rush0);
      case 1:
        return context.getString(R.string.new_rush1);
      case 2:
        return context.getString(R.string.new_rush2);
      case 3:
        return context.getString(R.string.new_rush3);
      case 4:
        return context.getString(R.string.new_rush4);
      case 5:
        return context.getString(R.string.new_rush5);
      case 6:
        return context.getString(R.string.new_rush6);
      case 7:
        return context.getString(R.string.new_rush7);
      case 8:
        return context.getString(R.string.new_rush8);
      default:
        return context.getString(R.string.new_rush9);
    }
  }
}
