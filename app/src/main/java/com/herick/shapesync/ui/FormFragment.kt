package com.herick.shapesync.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.herick.shapesync.R
import com.herick.shapesync.databinding.FragmentFormBinding
import com.herick.shapesync.model.TipoDeTreino

class FormFragment : Fragment() {
    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!

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

        val linearLayout: LinearLayout =
            binding.linearCheckBotton // Supondo que você tenha um LinearLayout no seu layout XML

        // Remove todos os CheckBoxes existentes para evitar duplicatas
        linearLayout.removeAllViews()

        for (tipoDeTreino in TipoDeTreino.values()) {
            val checkBox = CheckBox(requireContext())
            checkBox.text = tipoDeTreino.descricao
            checkBox.id = tipoDeTreino.ordinal

            checkBox.apply {
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                setPadding(10, 10, 10, 10)
                val customFont = ResourcesCompat.getFont(context, R.font.open_sans_semibold)
                setTypeface(customFont)
            }

            linearLayout.addView(checkBox)
        }
    }

    fun obterURLPorDescricao(descricao: String): String? {
        return when (descricao) {
            "Treinamento de Força" -> "url_para_treinamento_de_forca"
            "Musculação" -> "url_para_musculacao"
            "Treinamento Funcional" -> "url_para_treinamento_funcional"
            "Treinamento Cardiovascular" -> "url_para_treinamento_cardiovascular"
            "Corrida e Caminhada" -> "url_para_corrida_e_caminhada"
            "Esteira" -> "esteira"
            "Crossfit" -> "url_para_crossfit"
            else -> null // Retorna null se a descrição não corresponder a nenhuma entrada
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
