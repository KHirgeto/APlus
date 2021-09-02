package com.rafael.apluse.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.rafael.apluse.FireStore.FirestoreClass
import com.rafael.apluse.R
import com.rafael.apluse.classes.Student
import com.rafael.apluse.classes.TinyDB

class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var etEmail: TextInputLayout
    private lateinit var etPassword: TextInputLayout
    private lateinit var etName: TextInputLayout
    private lateinit var SignUp: TextView
    private lateinit var signIn: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()
        etName = findViewById(R.id.etSignUpName)
        etEmail = findViewById(R.id.etSignUpEmail)
        etPassword = findViewById(R.id.etSignUpPasswordET)
        SignUp = findViewById(R.id.btnSignUpBTN)
        signIn = findViewById(R.id.btnSignUpAlreadyHaveAccount)

        signIn.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }
        SignUp.setOnClickListener {
            SignUp.isClickable = false
            createUser()
        }
    }

    private fun createUser() {
        val email:String = etEmail.editText?.text.toString()
        val name:String = etName.editText?.text.toString()
        val password:String = etPassword.editText?.text.toString()
        if(TextUtils.isEmpty(etEmail.toString()) || email.isEmpty())
        {
            etEmail.editText?.setError("Email can't be empty").toString()
            etEmail.requestFocus()
        }
        else if(TextUtils.isEmpty(etPassword.toString())||password.isEmpty())
        {
            etPassword.editText?.setError("Password can't be empty").toString()
            etPassword.requestFocus()
        }
        else if(TextUtils.isEmpty(etName.toString())||password.isEmpty())
        {
            etName.editText?.setError("Name can't be empty").toString()
            etName.requestFocus()
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            val tinyDB: TinyDB = TinyDB(this)
                            tinyDB.putString("UID",mAuth.uid)
                            val student = Student(name,email,mAuth.uid,null,null)
                            FirestoreClass().registerUser(this,student)

                           // Toast.makeText(this,"User registered successfully",Toast.LENGTH_SHORT).show()

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this,"Registration Failed "+task.exception?.message,Toast.LENGTH_SHORT).show()
                        }
                    })
        }


    }

    fun userRegistrationSuccess() {
        Toast.makeText(this,"User Created",Toast.LENGTH_SHORT).show()
        mAuth.signOut()
        finish()
    }
}