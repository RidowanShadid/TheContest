package com.example.singleactivitygame

import android.content.res.Configuration
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var score = 0
    private lateinit var winSound: MediaPlayer
    private lateinit var tvScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize MediaPlayer for win sound
        winSound = MediaPlayer.create(this, R.raw.win_sound)

        // Find views
        tvScore = findViewById(R.id.tvScore)
        val btnScore: Button = findViewById(R.id.btnScore)
        val btnSteal: Button = findViewById(R.id.btnSteal)
        val btnReset: Button = findViewById(R.id.btnReset)

        // SCORE Button Listener
        btnScore.setOnClickListener {
            if (score < 15) { // Cap the score at 15
                score++
                Log.d("MainActivity", "Score button clicked. Current score: $score")
                updateScoreDisplay()
                if (score == 15) {
                    Log.d("MainActivity", "Score reached 15. Playing win sound.")
                    winSound.start() // Play win sound when score reaches 15
                }
            } else {
                Log.d("MainActivity", "Score button clicked but score is already 15.")
            }
        }

        // STEAL Button Listener
        btnSteal.setOnClickListener {
            if (score > 0) { // Ensure score doesn't go below 0
                score--
                Log.d("MainActivity", "Steal button clicked. Current score: $score")
                updateScoreDisplay()
            } else {
                Log.d("MainActivity", "Steal button clicked but score is already 0.")
            }
        }

        // RESET Button Listener
        btnReset.setOnClickListener {
            score = 0
            Log.d("MainActivity", "Reset button clicked. Score reset to 0.")
            updateScoreDisplay()
            stopWinSound()
        }
    }

    // Update score display and color based on the score
    private fun updateScoreDisplay() {
        tvScore.text = String.format(Locale.getDefault(), "%d", score)

        // Gradual color change based on the score
        when {
            score == 15 -> {
                tvScore.setTextColor(Color.GREEN) // Green when score is 15
                Log.d("MainActivity", "Score is 15. Text color changed to GREEN.")
            }
            score >= 10 -> {
                tvScore.setTextColor(Color.YELLOW) // Yellow for scores between 10 and 14
                Log.d("MainActivity", "Score is between 10 and 14. Text color changed to YELLOW.")
            }
            else -> {
                tvScore.setTextColor(Color.RED) // Red for scores below 10
                Log.d("MainActivity", "Score is below 10. Text color changed to RED.")
            }
        }
    }

    // Stop and reset win sound
    private fun stopWinSound() {
        if (winSound.isPlaying) {
            winSound.pause()
            winSound.seekTo(0)
            Log.d("MainActivity", "Win sound stopped and reset.")
        }
    }

    // Save the current score during orientation changes
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("score", score)
        Log.d("MainActivity", "Score saved: $score")
    }

    // Restore the score after orientation changes
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        score = savedInstanceState.getInt("score", 0)
        Log.d("MainActivity", "Score restored: $score")
        updateScoreDisplay()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("MainActivity", "Configuration changed. Current score: $score")
        updateScoreDisplay() // Ensure UI updates after configuration change
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::winSound.isInitialized) {
            stopWinSound()
            winSound.release()
            Log.d("MainActivity", "MediaPlayer resources released.")
        }
    }
}
