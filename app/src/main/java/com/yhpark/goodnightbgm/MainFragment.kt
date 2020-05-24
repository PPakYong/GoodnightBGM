package com.yhpark.goodnightbgm

import android.annotation.SuppressLint
import android.content.Context.AUDIO_SERVICE
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.SimpleDateFormat

class MainFragment : Fragment() {
    lateinit var mediaPlayer: MediaPlayer
    var preVolume: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, null)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val audioManager = activity!!.getSystemService(AUDIO_SERVICE) as AudioManager
        preVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mediaPlayer = MediaPlayer.create(activity, getBgm())

        mediaPlayer.setOnPreparedListener {
            tvRemainTime.text = convertDuration(mediaPlayer.duration)
            progressBar.max = mediaPlayer.duration / 1000

            val timer =
                object : CountDownTimer(1000 * 60 * 60 * 2/*mediaPlayer.duration.toLong()*/, 100) {
                    override fun onFinish() {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, preVolume, FLAG_REMOVE_SOUND_AND_VIBRATE)
                        activity!!.finishAffinity()

                        mediaPlayer.stop()
                        mediaPlayer.release()
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        tvCurrentTime.text =
                            SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())
                        tvRemainTime.text =
                            convertDuration(mediaPlayer.duration - mediaPlayer.currentPosition)

                        progressBar.progress = mediaPlayer.currentPosition / 1000
                    }
                }

            imageButton.setOnClickListener {
                if (mediaPlayer.isPlaying) {
                    activity!!.finishAffinity()
                } else {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 2, FLAG_REMOVE_SOUND_AND_VIBRATE)

                    timer.start()
                    mediaPlayer.start()
                    imageButton.setImageResource(R.drawable.ic_stop_black_24dp);
                }
            }
        }
    }

    private fun getBgm(): Int {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(activity /* Activity context */)
        val name = sharedPreferences.getString("bgm", "healing")

        var bgmResource: Int
        when (name) {
            "wave" -> {
                bgmResource = R.raw.wave
            }

            "rain" -> {
                bgmResource = R.raw.rain
            }

            "healing" -> {
                bgmResource = R.raw.healing
            }
            else -> {
                bgmResource = R.raw.healing
            }
        }
        return bgmResource
    }

    private fun convertDuration(duration: Int): CharSequence? {
        val hours = (duration / (1000 * 60 * 60)).toString()
        val minutes = (duration % (1000 * 60 * 60) / (1000 * 60)).toString()
        val seconds = (duration % (1000 * 60 * 60) % (1000 * 60) / 1000).toString()

        return "$hours:$minutes:$seconds"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val audioManager = requireContext().getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, preVolume, FLAG_REMOVE_SOUND_AND_VIBRATE)
    }
}