package com.herick.shapesync.model

import java.time.LocalDateTime

data class Treino(
    var nome: String = "",
    var descricao: String = "",
    var data: LocalDateTime? = null,
    var exercicios: List<Exercicio> = listOf()
)
