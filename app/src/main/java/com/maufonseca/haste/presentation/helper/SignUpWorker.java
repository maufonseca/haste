package com.maufonseca.haste.presentation.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maufonseca.haste.R;
import com.maufonseca.haste.presentation.home.Home;

/**
 * Created by mauricio on 01/04/18.
 */

public class SignUpWorker {
  private static SignUpWorker singleton;
  FirebaseAuth firebaseAuth;

  private SignUpWorker() {
    firebaseAuth = FirebaseAuth.getInstance();
  }

  public static synchronized SignUpWorker getInstance() {
    if (singleton == null)
      singleton = new SignUpWorker();
    return singleton;
  }

  public void signInAnonymously(final Home home) {
    if(NetworkWorker.getInstance().isOnline(home)) {
      home.showProgressBar();
      firebaseAuth.signInAnonymously().addOnCompleteListener(home, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          home.hideProgressBar();
          if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            home.showToast(home.getText(R.string.success_silent_auth));
            FirebaseUser user = firebaseAuth.getCurrentUser();
            SharedPreferences.Editor editor = home.getPreferences(Context.MODE_PRIVATE).edit();
            editor.putString(Constants.USER_ID, user.getUid());
            editor.apply();
            home.updateUI();
          } else {
            // If sign in fails, display a message to the user.
            home.showToast(home.getText(R.string.error_silent_auth));
          }
        }
      });
    }
  }

  public FirebaseUser getCurrentUser() {
    return firebaseAuth.getCurrentUser();
  }

  public void signOut(final Home home) {
    if(NetworkWorker.getInstance().isOnline(home)) {
      firebaseAuth.signOut();
    }
  }
}
