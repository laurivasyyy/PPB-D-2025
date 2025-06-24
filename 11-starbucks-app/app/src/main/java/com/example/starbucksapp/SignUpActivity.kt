package com.example.starbucksapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.starbucksapp.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnCreateAccount.setOnClickListener {
            if (validateForm()) {
                Toast.makeText(this, getString(R.string.verification_code_sent_toast), Toast.LENGTH_LONG).show()

                val intent = Intent(this, OTPVerificationActivity::class.java)
                intent.putExtra("phone_number", binding.etPhone.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun validateForm(): Boolean {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        when {
            firstName.isEmpty() -> {
                binding.etFirstName.error = getString(R.string.first_name_required)
                binding.etFirstName.requestFocus()
                return false
            }
            lastName.isEmpty() -> {
                binding.etLastName.error = getString(R.string.last_name_required)
                binding.etLastName.requestFocus()
                return false
            }
            email.isEmpty() -> {
                binding.etEmail.error = getString(R.string.email_required)
                binding.etEmail.requestFocus()
                return false
            }
            phone.isEmpty() -> {
                binding.etPhone.error = getString(R.string.phone_required)
                binding.etPhone.requestFocus()
                return false
            }
            password.length < 8 -> {
                binding.etPassword.error = getString(R.string.password_length_error)
                binding.etPassword.requestFocus()
                return false
            }
            !binding.cbTerms.isChecked -> {
                Toast.makeText(this, getString(R.string.terms_required), Toast.LENGTH_SHORT).show()
                return false
            }
        }

        return true
    }
}