package com.herick.shapesync.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Calendar
import java.util.Date

@Parcelize
data class Treino(
    var id: String = "",
    var nome: String = "",
    var descricao: String = "",
    var data: Date? = null,
    var exercicios: String = "",
    var status: Status = Status.TODO
) : Parcelable
