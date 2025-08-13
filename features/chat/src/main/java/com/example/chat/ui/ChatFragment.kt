package com.example.chat.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.R
import com.example.chat.adapter.MessageAdapter
import com.example.chat.databinding.FragmentChatBinding
import com.example.chat.viewmodels.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognizerIntent: Intent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("chatId")?.let { chatId ->
            viewModel.setChatId(chatId)
        }
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
        setupSpeechRecognizer()
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(emptyList())
        binding.recyclerView.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true
            }
        }
    }

    private fun setupClickListeners() {
        binding.sendBtn.setOnClickListener {
            val message = binding.messageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message)
                binding.messageEditText.setText("")
                binding.welcomeText.visibility = View.GONE
            }
        }

        binding.microBtn.setOnClickListener {
            checkPermissionAndStartListening()
        }
    }

    private fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                Log.e("SpeechRecognizer", "Error: $error")
                when (error){
                    SpeechRecognizer.ERROR_AUDIO -> Log.e("SpeechRecognizer","Audio Recording Error")
                    SpeechRecognizer.ERROR_CLIENT -> Log.e("SpeechRecognizer", "Client Error")
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->Log.e("SpeechRecognizer", "Permission Error")
                    SpeechRecognizer.ERROR_NETWORK -> Log.e("SpeechRecognizer", "Network Error")
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> Log.e("SpeechRecognizer", "Network Timeout")
                    SpeechRecognizer.ERROR_NO_MATCH -> Log.e("SpeechRecognizer", "No Match Found")
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> Log.e("SpeechRecognizer", "Recognizer Busy")
                    SpeechRecognizer.ERROR_SERVER -> Log.e("SpeechRecognizer", "Server Error")
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> Log.e("SpeechRecognizer", "Speech Timeout")
                    else -> Log.e("SpeechRecognizer", "Error with code: $error")
                }
            }
            override fun onResults(results: Bundle?) {
                val spokenText = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)
                binding.messageEditText.setText(spokenText)
                if (spokenText != null) {
                    binding.messageEditText.setSelection(binding.messageEditText.text.length)
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }
    private fun checkPermissionAndStartListening() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO_PERMISSION
            )
        } else {
            startListening()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_RECORD_AUDIO_PERMISSION -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startListening()
                }else{
                    Toast.makeText(requireContext(), "Micro permission required", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    private fun startListening() {
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messages.collect { messages ->
                messageAdapter = MessageAdapter(messages)
                binding.recyclerView.adapter = messageAdapter
                binding.recyclerView.smoothScrollToPosition(messages.size)
                if (messages.isNotEmpty()) binding.welcomeText.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        speechRecognizer.destroy()
    }
    private  companion object{
        const val REQUEST_RECORD_AUDIO_PERMISSION = 123
    }
}