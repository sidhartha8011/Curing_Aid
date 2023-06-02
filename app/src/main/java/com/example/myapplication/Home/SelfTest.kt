package com.example.myapplication.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SelfTest : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_self_test, container, false)
    }


    private lateinit var submitButton: Button
    private val correctAnswers = arrayOf(3, 3, 2, 4, 2, 1, 2, 2, 2, 3)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submitButton = view.findViewById(R.id.submit_selftest)
        submitButton.setOnClickListener {
            submitAnswers()


        }

    }
        private fun submitAnswers() {
            var score = 0
            for (i in 0 until NUM_QUESTIONS) {
                val radioGroupId = resources.getIdentifier("optionRadioGroup${i + 1}", "id", requireContext().packageName)
                val radioGroup = requireView().findViewById<RadioGroup>(radioGroupId)
                val selectedRadioButtonId = radioGroup.checkedRadioButtonId

                if (selectedRadioButtonId != -1) {
                    val selectedRadioButton = requireView().findViewById<RadioButton>(selectedRadioButtonId)
                    val selectedRadioButtonIndex = radioGroup.indexOfChild(selectedRadioButton)

                    val isCorrect = selectedRadioButtonIndex + 1 == correctAnswers[i] // Compare with the correct answer position

                    if (isCorrect) {
                        score++
                    }
                }
            }

            val scorePercentage = score * 100 / NUM_QUESTIONS
            val message = "Score: $scorePercentage%"

            val auth=FirebaseAuth.getInstance().currentUser
            val db=Firebase.firestore

            val ref=db.collection("Users").document(auth!!.uid)

            ref.update("score", scorePercentage)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->

                    Toast.makeText(
                        context,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }



            resetQuiz()
        }

    private fun resetQuiz() {
        for (i in 0 until NUM_QUESTIONS) {
            val radioGroupId = resources.getIdentifier("optionRadioGroup${i + 1}", "id", requireContext().packageName)
            val radioGroup = view?.findViewById<RadioGroup>(radioGroupId)
            radioGroup?.clearCheck()
        }
    }

        companion object {
            private const val NUM_QUESTIONS = 10
        }

}