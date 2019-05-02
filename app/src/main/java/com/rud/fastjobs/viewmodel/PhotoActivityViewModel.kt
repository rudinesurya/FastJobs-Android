package com.rud.fastjobs.viewmodel

import androidx.lifecycle.ViewModel
import com.rud.fastjobs.data.repository.MyRepository

class PhotoActivityViewModel(
    private val myRepository: MyRepository
) : ViewModel() {
    fun pathToReference(path: String) = myRepository.pathToReference(path)
}