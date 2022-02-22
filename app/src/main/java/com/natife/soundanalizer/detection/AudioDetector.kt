package com.natife.soundanalizer.detection

import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchProcessor

interface AudioDetector {

    fun getLastPitch(): Float
    fun getLastAmplitude(): Float
    suspend fun start()
    fun stop()

}

class AudioDetectorImpl(
    detectionDataHandler: DetectionDataHandler
) : AudioDetector {

    private var _lastPitch = 0f
    private var _lastAmplitude = 0f

    private val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)
//    private val pitchDetectionHandler = PitchDetectionHandler { detectionResult, audioEvent ->
//        val amplitudes = FloatArray(audioEvent.bufferSize)
//        val transformBuffer = FloatArray(audioEvent.bufferSize * 2)
//        val fft = FFT(audioEvent.bufferSize)
//        System.arraycopy(audioEvent.floatBuffer, 0, transformBuffer, 0, audioEvent.floatBuffer.size)
//        fft.forwardTransform(transformBuffer)
//        fft.modulus(transformBuffer, amplitudes)
//        _lastPitch = detectionResult.pitch.takeIf { it > 0 } ?: _lastAmplitude
//        _lastAmplitude = amplitudes.maxOf { it }
//    }

    private val processor = PitchProcessor(
        PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
        22050f,
        1024,
        detectionDataHandler
    )

    init {
        dispatcher.addAudioProcessor(processor)
        detectionDataHandler.onPitchChanged {
            if (it > 0) {
                _lastPitch = it
            }
        }
        detectionDataHandler.onAmplitudeChanged {
            _lastAmplitude = it
        }
    }

    override fun getLastPitch(): Float {
        return _lastPitch
    }

    override fun getLastAmplitude(): Float {
        return _lastAmplitude
    }

    override suspend fun start() {
        dispatcher.run()
    }

    override fun stop() {
    }

}