package com.example.kotlinflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.kotlinflow.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initObserver()
        initClick()
    }

    private fun initObserver() {
        viewModel.liveData.observe(this) {
            binding.liveDataTV.text = it
        }
        lifecycleScope.launchWhenStarted {
            viewModel.sharedFlow.collectLatest {
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initClick() {
        with(binding) {
            liveDataMB.setOnClickListener {
                viewModel.triggerLiveData()
            }
            stateFlowMB.setOnClickListener {
                viewModel.triggerStateFlow()
            }
            flowMB.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.triggerFlow().collectLatest {
                        binding.flowTV.text = it
                    }
                }
            }
            sharedFlowMB.setOnClickListener {
                viewModel.triggerSharedFlow()
            }
        }
    }
}