package com.rafael.apluse.FireStore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.rafael.apluse.activities.SignUpActivity
import com.rafael.apluse.classes.Student


class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, student: Student) {

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        mFireStore.collection("students")
            // Document ID for users fields. Here the document it is the User ID.
            .document(student.uid)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(student, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }
}
