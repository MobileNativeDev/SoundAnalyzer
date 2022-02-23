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

    private val dispatcher by lazy {
        AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)
    }

    private val processor = PitchProcessor(
        PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
        22050f,
        1024,
        detectionDataHandler
    )

    init {
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
        dispatcher.addAudioProcessor(processor)
        dispatcher.run()
    }

    override fun stop() {
        dispatcher.removeAudioProcessor(processor)
    }

}