package com.maufonseca.haste.presentation.helper;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maufonseca.haste.presentation.home.Home;
import com.maufonseca.haste.presentation.home.HomePresenter;

/**
 * Created by mauricio on 01/04/18.
 */

public class SignUpWorker {
  private static SignUpWorker singleton;
  private FirebaseAuth firebaseAuth;

  private SignUpWorker() {
    firebaseAuth = FirebaseAuth.getInstance();
  }

  public static synchronized SignUpWorker getInstance() {
    if (singleton == null)
      singleton = new SignUpWorker();
    return singleton;
  }

  public void signInAnonymously(final HomePresenter presenter) {
    firebaseAuth.signInAnonymously().addOnCompleteListener(presenter.getActivity(), new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
          // Sign in success, update UI with the signed-in user's information
          presenter.anonymousSignInSucceeded();
        } else {
          // If sign in fails, display a message to the user.
          presenter.anonymousSignInFailed();
        }
      }
    });

  }

  public FirebaseUser getCurrentUser() {
    return firebaseAuth.getCurrentUser();
  }

  public void signOut(final Home home) {
    if(NetworkWorker.getInstance().isOnline(home)) {
      firebaseAuth.signOut();
      //firebaseAnalytics = FirebaseAnalytics.getInstance(home);
      //firebaseAnalytics.logEvent("anonymous_out",null);
    }
  }
}
