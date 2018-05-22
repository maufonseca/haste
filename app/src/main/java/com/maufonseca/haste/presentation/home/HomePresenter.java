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
  private HomeActivity homeActivity;
  private FirebaseUser currentUser;
  private FirebaseFirestore database;
  private CollectionReference rushesRef;
  private FirebaseAnalytics firebaseAnalytics;

  HomePresenter(HomeActivity homeActivity, RushList rushes) {
    this.homeActivity = homeActivity;
    firebaseAnalytics = FirebaseAnalytics.getInstance(homeActivity);
    database = FirebaseFirestore.getInstance();
    this.rushes = rushes;
  }

  void getCurrentUser() {
    currentUser = SignUpWorker.getInstance().getCurrentUser();
    if(currentUser==null) {
      if(NetworkWorker.getInstance().isOnline(homeActivity)) {
        homeActivity.showProgressBar();
        SignUpWorker.getInstance().signInAnonymously(this);
      } else { ///offline
        homeActivity.showEmptyState();
        homeActivity.stopSwipeRefresh(); //maybe it is up
      }
    } else {
      setupDatabase();
      getRushesForUser();
    }
  }

  public void anonymousSignInSucceeded() {
    homeActivity.showToast(homeActivity.getText(R.string.success_silent_auth));
    firebaseAnalytics.logEvent("anonymous_in_success",null);
    setupDatabase();
    getRushesForUser();
  }

  public void anonymousSignInFailed() {
    homeActivity.hideProgressBar();
    homeActivity.showToast(homeActivity.getText(R.string.error_silent_auth));
    firebaseAnalytics.logEvent("anonymous_in_fail",null);
    homeActivity.showEmptyState();
    homeActivity.stopSwipeRefresh();
  }

  private void setupDatabase() {
    rushesRef = database
      .collection("users/"+SignUpWorker.getInstance().getCurrentUser().getUid()+"/rushes");
  }

  void getRushesForUser() {
    homeActivity.showProgressBar();
    StorageWorker.getInstance().getRushes(this, rushesRef);
  }

  public void onRushesArrived(RushList newRushes) {
    homeActivity.hideProgressBar();
    homeActivity.stopSwipeRefresh();
    rushes.replaceAll(newRushes);
    homeActivity.refreshList();
  }

  public void onError(String message, Exception e) {
    Log.w("FB", message, e);
    homeActivity.hideProgressBar();
    homeActivity.stopSwipeRefresh();
  }

  void createRush(final String description) {
    homeActivity.showProgressBar();

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
          homeActivity.clearTextBox();
          homeActivity.refreshList();
          homeActivity.hideProgressBar();
          homeActivity.scrollToEnd();
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          homeActivity.hideProgressBar();
          Log.w("FB", "Error adding document", e);
        }
      });
  }

  void deleteRush(int position) {
    final int rushPosition = position;
    homeActivity.showProgressBar();
    Rush rush = rushes.get(position);
    rushesRef
      .document(rush.getId())
      .delete()
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          Log.d("FB", "DocumentSnapshot deleted with position: " + rushPosition);
          rushes.remove(rushPosition);
          homeActivity.refreshList();
          homeActivity.hideProgressBar();
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          homeActivity.hideProgressBar();
          Log.w("FB", "Error adding document", e);
        }
      });
  }

  void checkRush(final Rush tappedRush) {
    tappedRush.setDone(!tappedRush.getDone());
    homeActivity.refreshList();
    homeActivity.showProgressBar();
    Map<String, Object> rush = new HashMap<>();
    rush.put("done", tappedRush.getDone());
    rushesRef
      .document(tappedRush.getId())
      .set(rush, SetOptions.merge())
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          Log.d("FB", "DocumentSnapshot successfully written!");
          homeActivity.hideProgressBar();
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.w("FB", "Error writing document", e);
          tappedRush.setDone(!tappedRush.getDone());
          homeActivity.refreshList();
          homeActivity.hideProgressBar();
        }
      });
  }

  public AppCompatActivity getActivity() {
    return homeActivity;
  }
  void changePositions(int pos1, int pos2) {
    rushes.changeRushes(pos1, pos2);
    homeActivity.refreshList();
  }
}
