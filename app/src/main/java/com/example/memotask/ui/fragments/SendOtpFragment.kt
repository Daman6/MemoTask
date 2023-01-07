package com.example.memotask.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.memotask.ui.MainActivity
import com.example.memotask.R
import com.example.memotask.databinding.FragmentSendOtpBinding
import com.example.memotask.viewModel.MainViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit


class SendOtpFragment : Fragment() {

    private lateinit var binding: FragmentSendOtpBinding
    private lateinit var viewModel : MainViewModel
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSendOtpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        init()


        binding.liveData = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        binding.sendOtpBtn.setOnClickListener {
            binding.progressCircular.visibility = View.VISIBLE
            if (viewModel.checkUserPhone()) {
                Log.e("sjbdjbd","+"+binding.countryCodePicker.selectedCountryCode.toString() + viewModel._phoneNum.value.toString())
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber("+"+binding.countryCodePicker.selectedCountryCode.toString() + viewModel._phoneNum.value.toString())
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(requireActivity())
                    .setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }
    }

    fun init() {
        auth = FirebaseAuth.getInstance()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    binding.progressCircular.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), "Authentication Successful", Toast.LENGTH_SHORT).show()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        binding.progressCircular.visibility = View.INVISIBLE

                    }
                }
            }
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {


            if (e is FirebaseAuthInvalidCredentialsException) {
            } else if (e is FirebaseTooManyRequestsException) {
            }

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            binding.progressCircular.visibility = View.INVISIBLE
            val bundle = Bundle()
            bundle.putString("OTP",verificationId)
            bundle.putString("NUMBER",viewModel._phoneNum.value.toString())
            bundle.putParcelable("resendToken",token)
            findNavController().navigate(R.id.action_sendOtpFragment_to_verifyOtpFragment,bundle)
        }
    }

}