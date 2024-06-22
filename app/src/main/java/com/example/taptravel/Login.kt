package com.example.taptravel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taptravel.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.textfield.TextInputLayout


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        enableEdgeToEdge()
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            this.finish()
        }




        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val verified= firebaseAuth.currentUser?.isEmailVerified
                        if (verified==false) {
                            firebaseAuth.currentUser?.sendEmailVerification()
                                ?.addOnSuccessListener {
                                    Toast.makeText(this, "Please verify your Email", Toast.LENGTH_SHORT).show()
                                }
                                ?.addOnFailureListener {
                                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                                }
                        }
                        else{
                            Toast.makeText(this, "Email verified", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, Home_Page::class.java)
                            intent.flags =Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                            this.finish()

                        }
                    }
                    else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                    }
                }
            }
            else if(email.isEmpty() ){
                binding.emailEt.error = "Please enter email"
            }
            else if(pass.isEmpty()){
                binding.passET.error = "Please enter password"
            }
        }
    }
}