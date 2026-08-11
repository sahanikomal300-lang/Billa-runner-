package com.example.game.engine

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

object GameAudio {

    private val scope = CoroutineScope(Dispatchers.Default)

    fun playJumpSound() {
        scope.launch {
            generateToneSweep(startFreq = 300f, endFreq = 750f, durationMs = 90)
        }
    }

    fun playDeathSound() {
        scope.launch {
            generateToneSweep(startFreq = 450f, endFreq = 90f, durationMs = 180)
        }
    }

    fun playKeyCollectSound() {
        scope.launch {
            generateTwoNoteChime(freq1 = 587.33f, freq2 = 880f, durationPerNoteMs = 70)
        }
    }

    fun playCheckpointSound() {
        scope.launch {
            generateTwoNoteChime(freq1 = 523.25f, freq2 = 659.25f, durationPerNoteMs = 80)
        }
    }

    fun playWinSound() {
        scope.launch {
            generateTwoNoteChime(freq1 = 659.25f, freq2 = 1046.50f, durationPerNoteMs = 120)
        }
    }

    private fun generateToneSweep(startFreq: Float, endFreq: Float, durationMs: Int) {
        try {
            val sampleRate = 22050
            val numSamples = (sampleRate * (durationMs / 1000f)).toInt()
            val sample = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val progress = i.toFloat() / numSamples
                val currentFreq = startFreq + (endFreq - startFreq) * progress
                val angle = 2.0 * Math.PI * i * currentFreq / sampleRate
                val envelope = 1f - progress // Decay envelope
                sample[i] = (sin(angle) * 32767 * envelope * 0.4f).toInt().toShort()
            }

            playBuffer(sample, sampleRate)
        } catch (_: Exception) {
            // Safe fallback if audio hardware is unavailable
        }
    }

    private fun generateTwoNoteChime(freq1: Float, freq2: Float, durationPerNoteMs: Int) {
        try {
            val sampleRate = 22050
            val numSamplesPerNote = (sampleRate * (durationPerNoteMs / 1000f)).toInt()
            val sample = ShortArray(numSamplesPerNote * 2)

            for (i in 0 until numSamplesPerNote) {
                val progress = i.toFloat() / numSamplesPerNote
                val angle = 2.0 * Math.PI * i * freq1 / sampleRate
                sample[i] = (sin(angle) * 32767 * (1f - progress * 0.5f) * 0.4f).toInt().toShort()
            }

            for (i in 0 until numSamplesPerNote) {
                val progress = i.toFloat() / numSamplesPerNote
                val angle = 2.0 * Math.PI * i * freq2 / sampleRate
                sample[numSamplesPerNote + i] = (sin(angle) * 32767 * (1f - progress) * 0.4f).toInt().toShort()
            }

            playBuffer(sample, sampleRate)
        } catch (_: Exception) {
            // Safe fallback
        }
    }

    private fun playBuffer(sample: ShortArray, sampleRate: Int) {
        val minBufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            maxOf(minBufferSize, sample.size * 2),
            AudioTrack.MODE_STATIC
        )
        audioTrack.write(sample, 0, sample.size)
        audioTrack.play()

        // Release resources after tone finishes
        scope.launch {
            kotlinx.coroutines.delay(300)
            try {
                audioTrack.stop()
                audioTrack.release()
            } catch (_: Exception) {}
        }
    }
}
