package com.maufonseca.haste.infrastructure;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.maufonseca.haste.model.Rush;
import com.maufonseca.haste.model.RushList;
import com.maufonseca.haste.presentation.home.HomePresenter;

/**
 * Created by mauricio on 05/04/18.
 */

public class StorageWorker {
  private static StorageWorker singleton;

  private StorageWorker() {
  }

  public static synchronized StorageWorker getInstance() {
    if (singleton == null)
      singleton = new StorageWorker();
    return singleton;
  }

  public void getRushes(final HomePresenter presenter, CollectionReference rushesRef){
    rushesRef
      .orderBy("position")
      .get()
      .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {

          if (task.isSuccessful()) {
            RushList rushes = new RushList();
            for (DocumentSnapshot document : task.getResult()) {
              Log.d("FB", document.getId() + " => " + document.getData());
              Rush rush = new Rush();
              rush.setDescription((String) document.getData().get("description"));
              rush.setDone((boolean)document.getData().get("done"));
              rush.setPosition((long)document.getData().get("position"));
              rush.setId(document.getId());
              rushes.add(rush.getPosition(), rush);
            }
            presenter.onRushesArrived(rushes);
          } else {
            presenter.onError("Error getting documents.", task.getException());
          }
        }
      });
  }
}
