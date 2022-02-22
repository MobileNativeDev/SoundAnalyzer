package com.natife.soundanalizer.di

import com.natife.soundanalizer.MainViewModel
import com.natife.soundanalizer.detection.AudioDetector
import com.natife.soundanalizer.detection.AudioDetectorImpl
import com.natife.soundanalizer.detection.DetectionDataHandler
import com.natife.soundanalizer.detection.DetectionDataHandlerImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val dataModule = module {

    factory<AudioDetector> {
        AudioDetectorImpl(get())
    }

    factory<DetectionDataHandler> {
        DetectionDataHandlerImpl()
    }

}

private val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}

val modules = listOf(dataModule, viewModelModule)