package com.example.mtjobs.utils

sealed class NetworkState<T>(
    val bodyData: T? = null,
    val failureMessage: String? = null
) {


    class OnSuccess<T>(responseBody: T) : NetworkState<T>(bodyData = responseBody)

    class OnLoading<T> : NetworkState<T>()

    class OnFailure<T>(responseFailureMessage: String) :
        NetworkState<T>(failureMessage = responseFailureMessage )
}