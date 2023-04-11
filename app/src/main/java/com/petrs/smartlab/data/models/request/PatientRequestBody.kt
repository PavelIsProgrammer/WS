package com.petrs.smartlab.data.models.request

import com.google.gson.annotations.SerializedName
import com.petrs.smartlab.data.models.request.CatalogItemRequestBody

data class PatientRequestBody(
    @SerializedName("name")
    val name: String,
    @SerializedName("items")
    val items: List<CatalogItemRequestBody>
)
