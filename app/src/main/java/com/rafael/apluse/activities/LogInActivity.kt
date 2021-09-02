package com.rafael.apluse.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.rafael.apluse.R
import com.rafael.apluse.classes.TinyDB

class LogInActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    private lateinit var etEmail: TextInputLayout
    private lateinit var etPassword: TextInputLayout
    private lateinit var rememberMe: CheckBox
    private lateinit var forgotPassword: TextView
    private lateinit var login: Button
    private lateinit var createAccountBTN: TextView
    private lateinit var tinyDB: TinyDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        etEmail = findViewById(R.id.etLoginEmail)
        etPassword= findViewById(R.id.etLoginPassword)
        rememberMe = findViewById(R.id.cbLoginRememberMe)
        forgotPassword = findViewById(R.id.btnLoginForgotPassword)
        login = findViewById(R.id.btnLoginBTN)
        createAccountBTN = findViewById(R.id.btnLoginCreateANewAccount)
        mAuth = FirebaseAuth.getInstance()

        tinyDB= TinyDB(this)
        //Check if the user clicked remember me before and if so logIn automatically
        checkRememberMe()

        login.setOnClickListener {
            when {
                TextUtils.isEmpty(etEmail.toString()) -> {
                    etEmail.editText?.setError("Email can't be empty").toString()
                    etEmail.requestFocus()
                }
                TextUtils.isEmpty(etPassword.toString()) -> {
                    etPassword.editText?.setError("Password can't be empty").toString()
                    etEmail.requestFocus()
                }
                else -> {
                    loginUser()
                }
            }

        }
        createAccountBTN.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        forgotPassword.setOnClickListener {
            //startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        rememberMe.setOnCheckedChangeListener { _, isChecked ->
            // Responds to checkbox being checked/unchecked
            if(isChecked)
            {
                tinyDB.putBoolean("CheckBox",true)

            }
            else
            {
                tinyDB.putBoolean("CheckBox",false)
            }
        }

    }

    private fun checkRememberMe() {
        val isChecked: Boolean = tinyDB.getBoolean("rememberMeChecked")
        if (isChecked)
        {
            Toast.makeText(this,"Logged in successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }


    private fun loginUser() {
        val email:String = etEmail.editText?.text.toString()
        val password:String = etPassword.editText?.text.toString()
        if(TextUtils.isEmpty(etEmail.toString()) || email.isEmpty())
        {
            etEmail.editText?.setError("Email can't be empty").toString()
            etEmail.requestFocus()
        }
        else if(TextUtils.isEmpty(etPassword.toString())|| password.isEmpty())
        {
            etPassword.editText?.setError("Password can't be empty").toString()
            etPassword.requestFocus()
        }
        else
        {
            mAuth.signInWithEmailAndPassword(etEmail.editText?.text.toString(), etPassword.editText?.text.toString())
                .addOnCompleteListener(this,
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this,"Logged in successfully", Toast.LENGTH_SHORT).show()
                            if (tinyDB.getBoolean("CheckBox"))
                            {
                                tinyDB.putBoolean("rememberMeChecked", true)
                            }
                            else
                            {
                                tinyDB.putBoolean("rememberMeChecked", false)
                            }
                            startActivity(Intent(this, HomeActivity::class.java))
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this,"Login Failed "+task.exception?.message,
                                Toast.LENGTH_SHORT).show()
                        }
                    })
        }
    }
}