package com.example.mtjobs.data.models


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mtjobs.utils.Constants
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class JobsResponse : ArrayList<JobsResponseItem>(){

}
@Parcelize
@Entity(tableName = Constants.JOBS_TABLE_NAME)
data class JobsResponseItem(
    @PrimaryKey val id: String,
    val type: String,
    val url: String,
    @SerializedName("created_at")
    val createdAt: String,
    val company: String,
    @SerializedName("company_url")
    val companyUrl: String?,
    val location: String,
    val title: String,
    val description: String,
    @SerializedName("how_to_apply")
    val howToApply: String,
    @SerializedName("company_logo")
    val companyLogo: String?
) : Parcelable