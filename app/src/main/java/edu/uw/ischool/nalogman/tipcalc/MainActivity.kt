package edu.uw.ischool.nalogman.tipcalc

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var editTextAmount: EditText
    private lateinit var buttonTip: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextAmount = findViewById(R.id.editTextAmount)
        buttonTip = findViewById(R.id.buttonTip)

        // Apply an InputFilter to allow only numbers and format as USD currency
        val currencyFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val newInput = dest.toString().substring(0, dstart) + source.subSequence(start, end) + dest.toString().substring(dend)
            if (newInput.matches(Regex("^\\d+(\\.\\d{0,2})?$"))) {
                null
            } else {
                ""
            }
        }
        editTextAmount.filters = arrayOf<InputFilter>(currencyFilter)

        // Add a TextWatcher to enable the "Tip" button when data is entered
        editTextAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Enable the "Tip" button when data is entered
                val isDataEntered = s?.isNotEmpty() ?: false
                buttonTip.isEnabled = isDataEntered
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Add an OnClickListener to the "Tip" button to calculate and display the tip
        buttonTip.setOnClickListener {
            val cleanAmount = editTextAmount.text.toString().replace("[^\\d.]".toRegex(), "")
            val serviceCharge = cleanAmount.toDoubleOrNull()

            if (serviceCharge != null) {
                val tipAmount = serviceCharge * 0.15
                val formattedTip = NumberFormat.getCurrencyInstance().format(tipAmount)
                Toast.makeText(this, "Tip: $formattedTip", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid service charge", Toast.LENGTH_SHORT).show()
            }
        }
    }
}