package com.herick.shapesync.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}