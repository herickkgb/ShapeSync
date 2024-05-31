package com.herick.shapesync.ui.adapter

import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.herick.shapesync.data.model.Status
import com.herick.shapesync.data.model.Treino
import com.herick.shapesync.databinding.ItemTreinamentoTaskBinding
import java.text.SimpleDateFormat

import java.util.Date
import java.util.Locale

import com.herick.shapesync.R


class TaskTreinoAdapter(
    private val context: Context,
    private val taskList: List<Treino>,
    private val taskSelected: (Treino, Int) -> Unit


) : RecyclerView.Adapter<TaskTreinoAdapter.MyViewHolder>() {

    companion object {
        val SELECT_BACK: Int = 1
        val SELECT_REMOVE: Int = 2
        val SELECT_EDIT: Int = 3
        val SELECT_DETAILS: Int = 4
        val SELECT_NEXT: Int = 5

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Treino>() {
            override fun areItemsTheSame(oldItem: Treino, newItem: Treino): Boolean {
                return oldItem.id == newItem.id && oldItem.descricao == newItem.descricao
            }

            override fun areContentsTheSame(oldItem: Treino, newItem: Treino): Boolean {
                return oldItem == newItem && oldItem.descricao == newItem.descricao
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemTreinamentoTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = taskList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = taskList[position]

        holder.binding.TextDescricao.text =
            Editable.Factory.getInstance().newEditable(task.descricao)

        holder.binding.TextTipoTreinamento.text =
            Editable.Factory.getInstance().newEditable(task.exercicios)
        holder.binding.textNome.text = task.nome
        val calendar: Date? = task.data

        calendar?.let {
            // Format the Calendar object to a String
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedDate = format.format(calendar.time)

            // Convert the String to an Editable
            val editableText: Editable = SpannableStringBuilder(formattedDate)

            // Set the Editable to TextData
            holder.binding.TextData.text = editableText
        }

        setIndicators(task, holder)

        holder.binding.btnEdit.setOnClickListener { taskSelected(task, SELECT_EDIT) }
        holder.binding.btnDelete.setOnClickListener { taskSelected(task, SELECT_REMOVE) }
        holder.binding.btnDetails.setOnClickListener { taskSelected(task, SELECT_DETAILS) }
    }

    private fun setIndicators(task: Treino, holder: MyViewHolder) {
        when (task.status) {
            Status.TODO -> {
                holder.binding.btnBack.visibility = View.INVISIBLE


                holder.binding.btnNext.setOnClickListener { taskSelected(task, SELECT_NEXT) }
            }

            Status.DOING -> {
                holder.binding.btnBack.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.color_status_todo
                    )
                )
                holder.binding.btnNext.setColorFilter(
                    ContextCompat.getColor(
                        context, R.color.color_status_done
                    )
                )

                holder.binding.btnNext.setOnClickListener { taskSelected(task, SELECT_NEXT) }
                holder.binding.btnBack.setOnClickListener { taskSelected(task, SELECT_BACK) }

            }

            Status.DONE -> {
                holder.binding.btnNext.visibility = View.INVISIBLE

                holder.binding.btnBack.setOnClickListener { taskSelected(task, SELECT_BACK) }
            }
        }
    }

    inner class MyViewHolder(val binding: ItemTreinamentoTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

}