package com.natife.soundanalizer.detection

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.util.fft.FFT

interface DetectionDataHandler : PitchDetectionHandler {

    fun onPitchChanged(block: (Float) -> Unit)
    fun onAmplitudeChanged(block: (Float) -> Unit)

}

class DetectionDataHandlerImpl : DetectionDataHandler {

    private var _onPitchChanged: (Float) -> Unit = {}
    private var _onAmplitudeChanged: (Float) -> Unit = {}

    override fun handlePitch(detectionResult: PitchDetectionResult?, audioEvent: AudioEvent?) {
        detectionResult?.also(::detectPitch)
        audioEvent?.also(::detectAmplitude)
    }

    override fun onPitchChanged(block: (Float) -> Unit) {
        _onPitchChanged = block
    }

    override fun onAmplitudeChanged(block: (Float) -> Unit) {
        _onAmplitudeChanged = block
    }

    private fun detectPitch(detectionResult: PitchDetectionResult) {
        val pitch = detectionResult.pitch.takeIf { it > 0 } ?: 0f
        _onPitchChanged(pitch)
    }

    private fun detectAmplitude(audioEvent: AudioEvent) {
        val amplitudes = FloatArray(audioEvent.bufferSize)
        val transformBuffer = FloatArray(audioEvent.bufferSize * 2)
        val fft = FFT(audioEvent.bufferSize)
        System.arraycopy(audioEvent.floatBuffer, 0, transformBuffer, 0, audioEvent.floatBuffer.size)
        fft.forwardTransform(transformBuffer)
        fft.modulus(transformBuffer, amplitudes)
        val amplitude = amplitudes.maxOf { it }
        _onAmplitudeChanged(amplitude)
    }

}