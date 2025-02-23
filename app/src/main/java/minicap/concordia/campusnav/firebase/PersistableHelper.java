package minicap.concordia.campusnav.firebase;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersistableHelper {
    /**
     * Fetches all the objects of the given type from firebae
     * @param objClass
     * @param onSuccessListener
     * @param <T>
     */
    public static <T extends Persistable> void fetchAll(Class<T> objClass, OnSuccessListener<List<T>> onSuccessListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String collectionName = objClass.getSimpleName();

        List<T> objectList = new ArrayList<>();

        db.collection(collectionName)
                .get()
                .addOnSuccessListener(querySnapshots -> {
                    for (DocumentSnapshot document : querySnapshots.getDocuments()) {
                        Map<String, Object> data = document.getData();
                        if (data != null) {
                            try {
                                T obj = objClass.getDeclaredConstructor().newInstance();
                                obj.populateFieldsFromMap(data);
                                objectList.add(obj);
                            } catch (Exception e) {
                                Log.e("Firestore", "Error creating object of type: " + objClass.getSimpleName(), e);
                            }
                        }
                    }
                    onSuccessListener.onSuccess(objectList);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching collection: " + collectionName, e);
                });
    }
}
