package com.petrs.smartlab.data.models.request

import com.google.gson.annotations.SerializedName
import com.petrs.smartlab.data.models.request.PatientRequestBody

data class CreateOrderRequestBody(
    @SerializedName("address")
    var address: String,
    @SerializedName("date_time")
    var dateTime: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("comment")
    var comment: String = "",
    @SerializedName("audio_comment")
    var audioComment: String = "",
    @SerializedName("patients")
    var patients: List<PatientRequestBody>
) {
}