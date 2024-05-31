package com.herick.shapesync.ui

import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskapp.util.initToolbar
import com.example.taskapp.util.showBottomSheet
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.herick.shapesync.R
import com.herick.shapesync.data.model.Status
import com.herick.shapesync.databinding.FragmentFormBinding
import com.herick.shapesync.data.model.TipoTreino
import com.herick.shapesync.data.model.Treino
import java.lang.ref.Reference
import java.util.Calendar
import java.util.Date

class FormFragment : Fragment() {
    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!
    private lateinit var task: Treino
    private var newTask: Boolean = true
    private var status: Status = Status.TODO
    private var selectedCheckboxNames = mutableListOf<String>()

    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        initToolbar(binding.toolbar)

        reference = Firebase.database.reference

        initListener()

        setupCheckboxes()

    }


    private fun initListener() {
        binding.btnSave.setOnClickListener {
            validadeData()
        }

        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            status = when (id) {
                R.id.rd_Todo -> Status.TODO
                R.id.rd_Doing -> Status.DOING
                else -> Status.DONE
            }
        }
    }

    private fun validadeData() {
        var nomeTreinamento = binding.nomeTreinamento.text.toString().trim()
        var descricao = binding.descricao.text.toString().trim()

        val day = binding.dataPicker.dayOfMonth
        val month = binding.dataPicker.month
        val year = binding.dataPicker.year

        if (nomeTreinamento.isNotEmpty()) {
            if (descricao.isNotEmpty()) {
                if (binding.dataPicker.isNotEmpty()) {
                    binding.progressCircular.visibility = View.VISIBLE

                    if (newTask) task = Treino()
                    task.id = reference.database.reference.push().key ?: ""

                    task.nome = nomeTreinamento
                    task.descricao = descricao
                    task.data = converterData(day, month, year)
                    task.exercicios = selectedCheckboxNames.toString()
                    task.status = status

                    saveTaskTreinamento()
                }else {
                    showBottomSheet(message = getString(R.string.data_empty_register_fragment))
                }

            } else {
                showBottomSheet(message = getString(R.string.descricao_empty_register_fragment))
            }
        } else {
            showBottomSheet(message = getString(R.string.nomeTreinamento_empty_register_fragment))
        }
    }


    private fun converterData(day: Int, month: Int, year: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(
            year,
            month - 1,
            day
        ) // O mês em Calendar é baseado em zero, então subtraímos 1

        return calendar.time
    }

    private fun setupCheckboxes() {
        val linearLayout: LinearLayout =
            binding.linearCheckBotton // Supondo que você tenha um LinearLayout no seu layout XML

        // Remove todos os CheckBoxes existentes para evitar duplicatas
        linearLayout.removeAllViews()

        for (tipoDeTreino in TipoTreino.values()) {
            val checkBox = CheckBox(requireContext())
            checkBox.text = tipoDeTreino.descricao
            checkBox.id = tipoDeTreino.ordinal

            checkBox.apply {
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                setPadding(10, 10, 10, 10)
                val customFont = ResourcesCompat.getFont(context, R.font.open_sans_semibold)
                setTypeface(customFont)

                // Adiciona um Listener para cada CheckBox para verificar se foi selecionado
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedCheckboxNames.add(text.toString()) // Adiciona o nome do CheckBox selecionado à lista
                    } else {
                        selectedCheckboxNames.remove(text.toString()) // Remove o nome do CheckBox desmarcado da lista
                    }
                }
            }

            linearLayout.addView(checkBox)
        }
    }

    private fun saveTaskTreinamento() {
        reference
            .child("tasks")
            .child(auth.currentUser?.uid ?: "")
            .child(task.id)
            .setValue(task).addOnCompleteListener {result ->
                if (result.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        R.string.text_save_sucess_form_task_fragment,
                        Toast.LENGTH_SHORT
                    ).show()

                    if (newTask) {
                        findNavController().popBackStack()
                    } else {
                       // viewModelStore.setUpdateTask(task)

                        binding.progressCircular.visibility = View.GONE
                    }

                } else {
                    showBottomSheet(message = getString(R.string.erro_generic))
                    binding.progressCircular.visibility = View.GONE
                }
            }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
