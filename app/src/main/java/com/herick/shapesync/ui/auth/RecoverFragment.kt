package com.herick.shapesync.ui.auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.taskapp.util.initToolbar
import com.example.taskapp.util.showBottomSheet
import com.herick.shapesync.R
import com.herick.shapesync.databinding.FragmentRecoverBinding
import com.herick.shapesync.databinding.FragmentRegisterBinding

class RecoverFragment : Fragment() {
    private var _binding: FragmentRecoverBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        initListener()

    }

    private fun initListener() {
        binding.btnEnviar.setOnClickListener {
            validadeData()
        }
    }

    private fun validadeData() {
        var email = binding.editEmail.text.toString().trim()

        if (email.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            hideKeyboard(binding.root)

            recoverAccoutUser(email)
        } else {
            showBottomSheet(message = getString(R.string.email_empty_register_fragment))
        }
    }

    private fun recoverAccoutUser(email: String) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(email).addOnCompleteListener { task ->
            binding.progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                showBottomSheet(
                    message = getString(R.string.text_message_recover_accout_fragment)
                )
            } else {
                showBottomSheet(
                    message = getString(FirebaseHelper.validError(task.exception?.message.toString()))
                )
            }
        }

    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}