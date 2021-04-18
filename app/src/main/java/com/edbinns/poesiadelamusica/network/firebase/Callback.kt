package com.edbinns.poesiadelamusica.network.firebase

import java.lang.Exception

interface Callback<T> {
    fun onSuccess(result: T?)

    fun onFailed(exception: Exception)
}