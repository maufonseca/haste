package com.maufonseca.haste.presentation.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.maufonseca.haste.R;

/**
 * Created by mauricio on 01/04/18.
 */

public class SharedPreferencesWorker {
  private static SharedPreferencesWorker singleton;

  private SharedPreferencesWorker() {
  }

  public static synchronized SharedPreferencesWorker getInstance() {
    if (singleton == null)
      singleton = new SharedPreferencesWorker();
    return singleton;
  }
}
