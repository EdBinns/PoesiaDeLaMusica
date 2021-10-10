package com.edbinns.poesiadelamusica.services.firebase

import java.lang.Exception

interface Callback<T> {
    fun onSuccess(result: T?)

    fun onFailed(exception: Exception)
}