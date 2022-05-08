package com.kenshi.kotlinflowcompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    //It is cold flow
    //it doesn't do anything when there is no collectors(subscribers)

    //Hot Flow on the other hand, emit values even if where is no collectors(subscribers)
    val countDownFLow = flow<Int> {
        //inside the block, can execute suspend functions
        val startingValue = 10
        var currentValue = startingValue
        emit(startingValue)
        while(currentValue > 0) {
            delay(1000L)
            currentValue--
            //can emit integer value here
            //our ui (or anything else) will receive that change
            emit(currentValue)
        }
    }

    init {
        collectFlow()
    }

    private fun collectFlow() {
        viewModelScope.launch {
            countDownFLow.collect { time ->
                println("The current time is $time")
            }
        }

        // collect delay is longer than emit delay
        // collectLatest cancel emissions except the last one
        // finally terminal print only "The current time is 0 " (0 is not cancelled because is last emission)
        // ui state always want to latest state (collectLatest is good choice)
//        viewModelScope.launch {
//            countDownFLow.collectLatest { time ->
//                delay(1500L)
//                println("The current time is $time")
//            }
//        }
    }
}