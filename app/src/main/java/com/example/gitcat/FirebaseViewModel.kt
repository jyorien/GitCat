package com.example.gitcat

import android.app.Application
import android.content.Context
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

    private val _credits = MutableLiveData<Int>()
    val credits: LiveData<Int>
            get() = _credits

    private val _pats = MutableLiveData<Int>()
    val pats: LiveData<Int>
        get() = _pats

    private val _food = MutableLiveData<Int>()
    val food: LiveData<Int>
        get() = _food

    init {
        firestore.collection("users").document(uid).addSnapshotListener { value, error ->
            _credits.value = value?.data?.get(CREDITS).toString().split(".")[0].toInt()
            _pats.value = value?.data?.get(PATS).toString().split(".")[0].toInt()
            _food.value = value?.data?.get(FOOD).toString().split(".")[0].toInt()
        }
    }
    fun decreaseField(field: String,quantity: Int) {
        firestore.collection("users").document(uid).update(field, FieldValue.increment(quantity * -1.0))
    }

}