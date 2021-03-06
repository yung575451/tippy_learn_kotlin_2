package com.hungphamcom.tippylearnkotlin2


import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.util.*

private const val TAG="MainActivity"
private const val INITIAL_TIP_PERCENT=15
private const val INITIAL_NUMBER_SPIT=1
class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount:EditText
    private lateinit var seekBarTip:SeekBar
    private lateinit var tvTipPercentLabel:TextView
    private lateinit var tvTipAmount:TextView
    private lateinit var tvTotalAmount:TextView
    private lateinit var tvTipDescription:TextView
    private lateinit var seekbarPPL:SeekBar
    private lateinit var tvPerPersonAmount:TextView
    private lateinit var numberPPL:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount=findViewById(R.id.etBaseAmount)
        seekBarTip=findViewById(R.id.seekBarTip)
        tvTipPercentLabel=findViewById(R.id.tvTipPercentLabel)
        tvTipAmount=findViewById(R.id.tvTipAmount)
        tvTotalAmount=findViewById(R.id.tvTotalAmount)
        tvTipDescription=findViewById(R.id.tvTipDescription)
        seekbarPPL=findViewById(R.id.seekbarNumberPPL)
        tvPerPersonAmount=findViewById(R.id.tvPerPersonAmount)
        numberPPL=findViewById(R.id.numberPPL)

        seekBarTip.progress= INITIAL_TIP_PERCENT
        tvTipPercentLabel.text="$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        numberPPL.text="$INITIAL_NUMBER_SPIT"
        seekBarTip.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG,"onProgressChanged $p1")
                tvTipPercentLabel.text="$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        seekbarPPL.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                numberPPL.text="$p1"
                computeTipAndTotal()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        etBaseAmount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG,"afterTextChanged $p0")
                computeTipAndTotal()
            }
        })
    }

    private fun updateTipDescription(tipPercent : Int) {
        val tipDescription=when(tipPercent){
            in 0..9-> "Poor"
            in 10..14-> "Acceptable"
            in 15..19-> "Good"
            in 20..24-> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text=tipDescription
        val color=ArgbEvaluator().evaluate(
            tipPercent.toFloat()/seekBarTip.max,
            ContextCompat.getColor(this,R.color.Worst_tip)
                ,ContextCompat.getColor(this,R.color.Best_tip)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if(etBaseAmount.text.isEmpty()){
            tvTipAmount.text="0"
            tvTotalAmount.text="0"
            tvPerPersonAmount.text="0"
            return
        }
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent=seekBarTip.progress
        val numberPPl= seekbarPPL.progress

        val tipAmount=baseAmount*tipPercent/100
        val totalAmount=baseAmount+tipAmount
        val splitAmount=(baseAmount+tipAmount)/numberPPl

        tvTipAmount.text="%.2f".format(tipAmount)
        tvTotalAmount.text="%.2f".format(totalAmount)
        tvPerPersonAmount.text="%.2f".format(splitAmount)
    }
}