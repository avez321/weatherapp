package com.example.weatherapp.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ErrorResponse(
  @SerializedName("statusMessage")
  @Expose
  val statusMessage: String,
  @SerializedName("status_code")
  @Expose
  val statusCode: String,
  @SerializedName("success")
  @Expose
  val success: Boolean
)
