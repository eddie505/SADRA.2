package com.example.sadra2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()


        if (auth.currentUser != null){
            startApp()
            return
        }

        auth = FirebaseAuth.getInstance()

        val loginBtn: Button = findViewById(R.id.login_btn)
        val emailEditText: EditText = findViewById(R.id.email_et)
        val passwordEditText: EditText = findViewById(R.id.password_et)
        val createAccountTextView: TextView = findViewById(R.id.createAccount_tv)

        loginBtn.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Utils.showError(this, it.message.toString())
                }
        }

        createAccountTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startApp(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
