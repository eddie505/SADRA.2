package com.example.sadra2

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val createBtn: Button = findViewById(R.id.create_btn)
        val emailEditText: EditText = findViewById(R.id.email_et)
        val passwordEditText: EditText = findViewById(R.id.password_et)
        val nameEditText: EditText = findViewById(R.id.name_et)

        createBtn.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    val profile = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    it.user!!.updateProfile(profile)
                        .addOnSuccessListener {
                            AlertDialog.Builder(this).apply {
                                setTitle("Cuenta creada")
                                setMessage("Tu cuenta ha sido creada con éxito; Bienvenido a SADRA")
                                setPositiveButton("Aceptar") { dialogInterface: DialogInterface, i: Int ->
                                    finish()
                                }
                            }.show()
                        }
                        .addOnFailureListener {
                            Utils.showError(this, it.message.toString())
                        }
                }
                .addOnFailureListener {
                    Utils.showError(this, it.message.toString())
                }

        }
    }
}