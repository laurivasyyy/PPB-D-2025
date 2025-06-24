package com.example.starbucksapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.starbucksapp.databinding.ActivityOtpVerificationBinding

class OTPVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpVerificationBinding
    private var countDownTimer: CountDownTimer? = null
    private var canResend = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOTPInputs()
        setupClickListeners()
        startResendTimer()

        val phoneNumber = intent.getStringExtra("phone_number")
        phoneNumber?.let {
            binding.tvPhoneNumber.text = getString(R.string.verification_code_sent_to, formatPhoneNumber(it))
        }
    }

    private fun setupOTPInputs() {
        val otpInputs = arrayOf(
            binding.etOtp1, binding.etOtp2, binding.etOtp3,
            binding.etOtp4, binding.etOtp5, binding.etOtp6
        )

        otpInputs.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && index < otpInputs.size - 1) {
                        otpInputs[index + 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun setupClickListeners() {
        binding.btnVerify.setOnClickListener {
            val otp = getEnteredOTP()
            if (otp.length == 6) {
                Toast.makeText(this, getString(R.string.account_verified_success), Toast.LENGTH_LONG).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finishAffinity()
            } else {
                Toast.makeText(this, getString(R.string.enter_complete_code), Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvResendCode.setOnClickListener {
            if (canResend) {
                Toast.makeText(this, getString(R.string.verification_code_resent), Toast.LENGTH_SHORT).show()
                startResendTimer()
            }
        }

        binding.tvChangeNumber.setOnClickListener {
            finish()
        }
    }

    private fun getEnteredOTP(): String {
        return "${binding.etOtp1.text}${binding.etOtp2.text}${binding.etOtp3.text}" +
                "${binding.etOtp4.text}${binding.etOtp5.text}${binding.etOtp6.text}"
    }

    private fun startResendTimer() {
        canResend = false
        binding.tvResendCode.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))

        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                binding.tvResendCode.text = getString(R.string.resend_code_timer, seconds)
            }

            override fun onFinish() {
                canResend = true
                binding.tvResendCode.text = getString(R.string.resend_code)
                binding.tvResendCode.setTextColor(ContextCompat.getColor(this@OTPVerificationActivity, R.color.starbucks_green))
            }
        }.start()
    }

    private fun formatPhoneNumber(phoneNumber: String): String {
        return if (phoneNumber.length == 10) {
            String.format("(%s) %s-%s",
                phoneNumber.substring(0, 3),
                phoneNumber.substring(3, 6),
                phoneNumber.substring(6))
        } else phoneNumber
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}