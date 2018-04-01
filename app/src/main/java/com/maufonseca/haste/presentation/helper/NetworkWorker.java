package com.maufonseca.haste.presentation.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.maufonseca.haste.R;

/**
 * Created by mauricio on 01/04/18.
 */

public class NetworkWorker {
  private static NetworkWorker singleton;

  private NetworkWorker() {
  }

  public static synchronized NetworkWorker getInstance() {
    if (singleton == null)
      singleton = new NetworkWorker();
    return singleton;
  }

  public boolean isOnline(Context context) {
    ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

    if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED )
      return true;
    else {
      Toast.makeText(context, context.getText(R.string.project_id), Toast.LENGTH_SHORT).show();
      return false;
    }
  }
}
