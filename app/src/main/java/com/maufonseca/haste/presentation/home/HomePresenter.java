package com.maufonseca.haste.presentation.home;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.maufonseca.haste.R;
import com.maufonseca.haste.infrastructure.StorageWorker;
import com.maufonseca.haste.model.Rush;
import com.maufonseca.haste.model.RushList;
import com.maufonseca.haste.presentation.helper.NetworkWorker;
import com.maufonseca.haste.presentation.helper.SignUpWorker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mauricio on 30/03/18.
 */

public class HomePresenter {
  private RushList rushes;
  private Home home;
  private FirebaseUser currentUser;
  private FirebaseFirestore database;
  private CollectionReference rushesRef;
  private FirebaseAnalytics firebaseAnalytics;

  HomePresenter(Home home, RushList rushes) {
    this.home = home;
    firebaseAnalytics = FirebaseAnalytics.getInstance(home);
    database = FirebaseFirestore.getInstance();
    this.rushes = rushes;
  }

  void getCurrentUser() {
    currentUser = SignUpWorker.getInstance().getCurrentUser();
    if(currentUser==null) {
      if(NetworkWorker.getInstance().isOnline(home)) {
        home.showProgressBar();
        SignUpWorker.getInstance().signInAnonymously(this);
      } else { ///offline
        home.showEmptyState();
        home.stopSwipeRefresh(); //maybe it is up
      }
    } else {
      setupDatabase();
      getRushesForUser();
    }
  }

  public void anonymousSignInSucceeded() {
    home.showToast(home.getText(R.string.success_silent_auth));
    firebaseAnalytics.logEvent("anonymous_in_success",null);
    setupDatabase();
    getRushesForUser();
  }

  public void anonymousSignInFailed() {
    home.hideProgressBar();
    home.showToast(home.getText(R.string.error_silent_auth));
    firebaseAnalytics.logEvent("anonymous_in_fail",null);
    home.showEmptyState();
    home.stopSwipeRefresh();
  }

  private void setupDatabase() {
    rushesRef = database
      .collection("users/"+SignUpWorker.getInstance().getCurrentUser().getUid()+"/rushes");
  }

  void getRushesForUser() {
    home.showProgressBar();
    StorageWorker.getInstance().getRushes(this, rushesRef);
  }

  public void onRushesArrived(RushList newRushes) {
    home.hideProgressBar();
    home.stopSwipeRefresh();
    rushes.replaceAll(newRushes);
    home.refreshList();
  }

  public void onError(String message, Exception e) {
    Log.w("FB", message, e);
    home.hideProgressBar();
    home.stopSwipeRefresh();
  }

  void createRush(final String description) {
    home.showProgressBar();

    Map<String, Object> rush = new HashMap<>();
    rush.put("description", description);
    rush.put("done", false);
    rush.put("position", rushes.size());

    // Add a new document with a generated ID
    rushesRef.add(rush)
      .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
          Log.d("FB", "DocumentSnapshot added with ID: " + documentReference.getId());
          Rush newRush = new Rush();
          newRush.setDescription(description);
          newRush.setDone(false);
          newRush.setPosition(rushes.size());
          newRush.setId(documentReference.getId());
          rushes.add(newRush);
          home.clearTextBox();
          home.refreshList();
          home.hideProgressBar();
          home.scrollToEnd();
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          home.hideProgressBar();
          Log.w("FB", "Error adding document", e);
        }
      });
  }

  void deleteRush(int position) {
    final int rushPosition = position;
    home.showProgressBar();
    Rush rush = rushes.get(position);
    rushesRef
      .document(rush.getId())
      .delete()
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          Log.d("FB", "DocumentSnapshot deleted with position: " + rushPosition);
          rushes.remove(rushPosition);
          home.refreshList();
          home.hideProgressBar();
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          home.hideProgressBar();
          Log.w("FB", "Error adding document", e);
        }
      });
  }

  void checkRush(final Rush tappedRush) {
    tappedRush.setDone(!tappedRush.getDone());
    home.refreshList();
    home.showProgressBar();
    Map<String, Object> rush = new HashMap<>();
    rush.put("done", tappedRush.getDone());
    rushesRef
      .document(tappedRush.getId())
      .set(rush, SetOptions.merge())
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          Log.d("FB", "DocumentSnapshot successfully written!");
          home.hideProgressBar();
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.w("FB", "Error writing document", e);
          tappedRush.setDone(!tappedRush.getDone());
          home.refreshList();
          home.hideProgressBar();
        }
      });
  }

  public AppCompatActivity getActivity() {
    return home;
  }
  void changePositions(int pos1, int pos2) {
    rushes.changeRushes(pos1, pos2);
    home.refreshList();
  }
}
