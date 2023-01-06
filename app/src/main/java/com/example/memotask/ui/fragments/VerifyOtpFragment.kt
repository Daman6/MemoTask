package com.example.memotask.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.memotask.ui.MainActivity
import com.example.memotask.R
import com.example.memotask.databinding.FragmentVerifyOtpBinding
import com.example.memotask.viewModel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider


class VerifyOtpFragment : Fragment() {


    private lateinit var binding: FragmentVerifyOtpBinding
    private lateinit var viewModel : MainViewModel
    private lateinit var auth : FirebaseAuth

    private lateinit var OTP : String
    private lateinit var phoneNum : String
    private lateinit var resendToken : PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            phoneNum = requireArguments().getString("NUMBER").toString()
            OTP = requireArguments().getString("OTP").toString()
            resendToken = requireArguments().getParcelable<PhoneAuthProvider.ForceResendingToken>("resendToken")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerifyOtpBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        init()

        addtextChangeListener()


        binding.mainviewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.verifyBtn.setOnClickListener {

            val typeOtp = binding.optET1.text.toString()+binding.optET2.text.toString()+binding.optET3.text.toString()+binding.optET4.text.toString()+binding.optET5.text.toString()+binding.optET6.text.toString()
            if (typeOtp.isNotEmpty()){
                if (typeOtp.length == 6){
                    val credential:PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        OTP,typeOtp
                    )
                    signInWithPhoneAuthCredential(credential)
                }else{
                    Toast.makeText(requireContext(), "Please Enter Correct OTP", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), "Please Enter OTP", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_verifyOtpFragment_to_homeFragment)
            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                }
            }
        }
    }

    private fun addtextChangeListener(){
        binding.optET1.addTextChangedListener(EditTextWatcher(binding.optET1))
        binding.optET2.addTextChangedListener(EditTextWatcher(binding.optET2))
        binding.optET3.addTextChangedListener(EditTextWatcher(binding.optET3))
        binding.optET4.addTextChangedListener(EditTextWatcher(binding.optET4))
        binding.optET5.addTextChangedListener(EditTextWatcher(binding.optET5))
        binding.optET6.addTextChangedListener(EditTextWatcher(binding.optET6))
    }
    fun init() {
        auth = FirebaseAuth.getInstance()
    }

    inner class EditTextWatcher(val view : View):TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            val text = p0.toString()
            when(view.id){
                R.id.optET1 -> if (text.length == 1) binding.optET2.requestFocus()
                R.id.optET2 -> if (text.length == 1) binding.optET3.requestFocus() else if (text.isEmpty()) binding.optET1.requestFocus()
                R.id.optET3 -> if (text.length == 1) binding.optET4.requestFocus() else if (text.isEmpty()) binding.optET2.requestFocus()
                R.id.optET4 -> if (text.length == 1) binding.optET5.requestFocus() else if (text.isEmpty()) binding.optET3.requestFocus()
                R.id.optET5 -> if (text.length == 1) binding.optET6.requestFocus() else if (text.isEmpty()) binding.optET4.requestFocus()
                R.id.optET6 -> if (text.isEmpty()) binding.optET5.requestFocus()
            }
        }

    }
}
