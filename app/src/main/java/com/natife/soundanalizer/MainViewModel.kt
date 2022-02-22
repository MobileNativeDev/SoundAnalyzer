package com.natife.soundanalizer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.natife.soundanalizer.detection.AudioDetector
import com.natife.soundanalizer.detection.DetectionData
import kotlinx.coroutines.*

private const val DETECTION_INTERVAL_MS = 16L
private const val PITCH_SCALE_FACTOR = 100f
private const val AMPLITUDE_SCALE_FACTOR = 100f

class MainViewModel(
    private val audioDetector: AudioDetector
) : ViewModel() {

    private var detectionJob: Job? = null
    private var detectionDataJob: Job? = null
    private val _detectionData = MutableLiveData<DetectionData>()
    val detectionData: LiveData<DetectionData> = _detectionData
    private val _isDetecting = MutableLiveData<Boolean>()
    val isDetecting: LiveData<Boolean> = _isDetecting

    fun startStopDetection() {
        if (detectionJob != null) {
            stopDetection()
        } else {
            startDetection()
        }
    }

    private fun startDetection() {
        _isDetecting.value = true
        detectionJob = viewModelScope.launch(Dispatchers.IO) {
            audioDetector.start()
        }
        detectionDataJob = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val data = DetectionData(
                    pitch = audioDetector.getLastPitch() * PITCH_SCALE_FACTOR,
                    amplitude = audioDetector.getLastAmplitude() * AMPLITUDE_SCALE_FACTOR
                )
                withContext(Dispatchers.Main) {
                    _detectionData.value = data
                }
                delay(DETECTION_INTERVAL_MS)
            }
        }
    }

    private fun stopDetection() {
        _isDetecting.value = false
        audioDetector.stop()
        detectionJob?.cancel()
        detectionJob = null
        detectionDataJob?.cancel()
        detectionDataJob = null
    }

    override fun onCleared() {
        stopDetection()
        super.onCleared()
    }

}