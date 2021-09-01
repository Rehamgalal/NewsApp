package com.example.newsapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(
        val id: String?,
        val name: String?
) : Parcelable {
    companion object {
        operator fun invoke(
                id: Int? = null,
                name: String? = null,
        ): Source {
            return Source(
                    id ?: 0,
                    name ?: "",
            )
        }
    }

}