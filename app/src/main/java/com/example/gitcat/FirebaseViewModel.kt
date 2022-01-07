package com.example.gitcat

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

const val CREDITS = "credits"
const val PATS = "pats"
const val FOOD = "food"
class FirebaseViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val uid = context.getSharedPreferences(ID, Context.MODE_PRIVATE).getInt(
        ID,-1).toString()
    private val firestore = FirebaseFirestore.getInstance()

    private val _credits = MutableLiveData<Int>(0)
    val credits: LiveData<Int>
            get() = _credits

    private val _pats = MutableLiveData<Int>(0)
    val pats: LiveData<Int>
        get() = _pats

    private val _food = MutableLiveData<Int>(0)
    val food: LiveData<Int>
        get() = _food

    init {
        firestore.collection("users").document(uid).addSnapshotListener { value, error ->
            if (error?.message != null)   {
                Log.d("hello","error ${error.message}")
            }
            val values = value?.data
            if (values?.get(CREDITS) == null) {
                _credits.value = 0
            }
            if (values?.get(PATS) == null) {
                _pats.value = 0
            }
            if (values?.get(FOOD) == null) {
                _food.value = 0
            }
            values?.get(CREDITS)?.let { cred ->
                _credits.value =cred.toString().split(".")[0].toInt()
            }
            values?.get(PATS)?.let { pat ->
                _pats.value = pat.toString().split(".")[0].toInt()
            }
            values?.get(FOOD)?.let { food ->
                _food.value = food.toString().split(".")[0].toInt()
            }


        }
    }
    fun decreaseField(field: String,quantity: Int) {
        firestore.collection("users").document(uid).update(field, FieldValue.increment(quantity * -1.0))
    }
    fun increaseField(field: String, quantity: Int) {
        firestore.collection("users").document(uid).update(field, FieldValue.increment(quantity * 1.0))
    }

}