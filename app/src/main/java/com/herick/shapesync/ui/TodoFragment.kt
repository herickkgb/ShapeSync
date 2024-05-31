package com.herick.shapesync.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import java.text.SimpleDateFormat
import java.util.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskapp.util.showBottomSheet
import com.herick.shapesync.R
import com.herick.shapesync.data.model.Status
import com.herick.shapesync.data.model.Treino
import com.herick.shapesync.databinding.FragmentTodoBinding
import com.herick.shapesync.ui.adapter.TaskTreinoAdapter

class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskTreinoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initRecyclerView(getTask())

    }

    private fun initListener() {
       binding.fadAdd.setOnClickListener {
           findNavController().navigate(R.id.action_todoFragment_to_formFragment2)
       }
      //  observeViewModel()
    }


    private fun initRecyclerView(taskList: List<Treino>) {
        taskAdapter = TaskTreinoAdapter(requireContext(),taskList) { task, option ->
            optionSelected(task, option)
        }

        with(binding.rvTask) {
            LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter
        }
        binding.rvTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTask.setHasFixedSize(true)
        binding.rvTask.adapter = taskAdapter

    }
    private fun optionSelected(task: Treino, option: Int) {
        when (option) {
            TaskTreinoAdapter.SELECT_REMOVE -> {
                showBottomSheet(titleDialog = R.string.text_title_dialog_delete,
                    message = getString(R.string.text_message_dialog_delete),
                    titleButton = R.string.text_button_dialog_confirm_logout,
                    onClick = {
                     //   deleteTask(task)
                    })
            }

            TaskTreinoAdapter.SELECT_NEXT -> {
                task.status = Status.DOING

               // updateTask(task)
            }

            TaskTreinoAdapter.SELECT_DETAILS -> {
                Toast.makeText(requireContext(), task.descricao, Toast.LENGTH_LONG)
                    .show()
            }

            TaskTreinoAdapter.SELECT_EDIT -> {
             //   val action = HomeFragmentDirections.actionHomeFragmentToFormTaskFragment(task)
              //  findNavController().navigate(action)
            }
        }
    }


    private fun getTask(): List<Treino> {
        val dateString = "21/07/96"
        val dateFormat = SimpleDateFormat("dd/MM/yy")
        val date = dateFormat.parse(dateString) ?: Date()

        return listOf(
            Treino("1", "Treino de abdomen", "Descrição", date, "Perna | coxa | Braço",Status.TODO),
            Treino("2", "Treino de abdomen", "Descrição", date, "Perna | coxa | Braço",Status.TODO),
            Treino("3", "Treino de abdomen", "Descrição", date, "Perna | coxa | Braço",Status.TODO),
            Treino("4", "Treino de abdomen", "Descrição", date, "Perna | coxa | Braço",Status.TODO)
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}