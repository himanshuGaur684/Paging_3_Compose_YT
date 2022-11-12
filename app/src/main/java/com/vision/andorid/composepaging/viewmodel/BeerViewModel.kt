package com.vision.andorid.composepaging.viewmodel

import androidx.lifecycle.ViewModel
import com.vision.andorid.composepaging.repository.BeerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BeerViewModel @Inject constructor(private val beerRepo: BeerRepo) : ViewModel() {

    val pagingData = beerRepo.getBeerStream()

}