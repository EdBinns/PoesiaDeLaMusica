package com.edbinns.poesiadelamusica.services.firebase

import java.lang.Exception

interface RealtimeDataListener<T> {

    fun onDataChange(updatedData: T)

    fun onError(exception: Exception)

}