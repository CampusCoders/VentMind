package com.campuscoders.ventmind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myMap = HashMap<String,Any>()
        myMap["id"] = 123123123
        myMap["name"] = "expname"
        myMap["age"] = 15

        FirebaseFirestore.getInstance().collection("Users").add(myMap)
            .addOnSuccessListener {
                println("success + " + it.id)
            }
            .addOnFailureListener {
                println("failure")
                println(it.localizedMessage)
            }

    }
}