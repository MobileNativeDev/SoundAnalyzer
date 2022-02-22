package com.natife.soundanalizer

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.natife.soundanalizer.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val launcher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    viewModel.startStopDetection()
                }
            }
        binding.buttonAdd.setOnClickListener {
            if (hasRecordingPermission()) {
                viewModel.startStopDetection()
            } else {
                launcher.launch(android.Manifest.permission.RECORD_AUDIO)
            }
        }
        viewModel.detectionData.observe(this) {
            binding.textPitch.text = getString(R.string.frequency, it.pitch)
            binding.graph.addItem(it.amplitude)
        }
        viewModel.isDetecting.observe(this) {
            binding.buttonAdd.text = getString(
                if (it) {
                    R.string.stop
                } else {
                    R.string.start
                }
            )
        }
    }

    private fun hasRecordingPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

}