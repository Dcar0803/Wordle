package com.example.wordle

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var targetWord: String
    private lateinit var guessEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var resetButton: Button
    private lateinit var sportTermsButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var streakTextView: TextView
    private var remainingGuesses = 3
    private var streakCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        guessEditText = findViewById(R.id.guessEditText)
        submitButton = findViewById(R.id.submitGuessButton)
        resetButton = findViewById(R.id.resetButton)
        sportTermsButton = findViewById(R.id.sportTermsButton)
        resultTextView = findViewById(R.id.resultTextView)
        streakTextView = findViewById(R.id.streakTextView)

        // Generate target word
        targetWord = FourLetterWordList.getRandomFourLetterWord()

        // Submit button click listener
        submitButton.setOnClickListener {
            val guess = guessEditText.text.toString().uppercase()

            if (isValidGuess(guess)) {
                val correctness = checkGuess(guess)
                displayResult(guess, correctness)

                if (correctness == "OOOO") {
                    handleCorrectGuess()
                }

                remainingGuesses--

                if (remainingGuesses == 0) {
                    showTargetWord()
                    submitButton.isEnabled = false
                    resetButton.visibility = View.VISIBLE // Show reset button
                }
            } else {
                Toast.makeText(this, "Invalid guess!", Toast.LENGTH_SHORT).show()
            }
        }

        // Reset button click listener
        resetButton.setOnClickListener {
            resetGame()
        }

        // Sport terms button click listener
        sportTermsButton.setOnClickListener {
            // Implement logic for toggling between different word lists
        }
    }

    private fun isValidGuess(guess: String): Boolean {
        return guess.length == 4 && guess.all { it.isLetter() }
    }

    private fun checkGuess(guess: String): String {
        var result = ""
        val targetLength = targetWord.length
        val guessLength = guess.length
        val minLength = minOf(targetLength, guessLength)

        for (i in 0 until minLength) {
            result += if (guess[i] == targetWord[i]) {
                "O"
            } else if (guess[i] in targetWord) {
                "+"
            } else {
                "X"
            }
        }

        if (guessLength > targetLength) {
            result += "+".repeat(guessLength - targetLength)
        }
        return result
    }

    private fun displayResult(guess: String, correctness: String) {
        val currentText = resultTextView.text.toString()
        val newText = "$currentText\nGuess: $guess\nCorrectness: $correctness\n"
        resultTextView.text = newText
    }

    @SuppressLint("SetTextI18n")
    private fun handleCorrectGuess() {
        streakCount++
        streakTextView.text = "Streak: $streakCount"
    }

    private fun resetGame() {
        targetWord = FourLetterWordList.getRandomFourLetterWord()
        remainingGuesses = 3
        guessEditText.text.clear()
        resultTextView.text = ""
        submitButton.isEnabled = true
        resetButton.visibility = View.GONE // Hide reset button
    }

    @SuppressLint("SetTextI18n")
    private fun showTargetWord() {
        resultTextView.text = "Out of guesses. The target word was: $targetWord"
    }
}
