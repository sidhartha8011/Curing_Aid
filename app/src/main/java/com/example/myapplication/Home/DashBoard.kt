package com.example.myapplication.Home

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


import com.github.mikephil.charting.data.PieEntry




class DashBoard : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dash_board, container, false)
    }

    private lateinit var chart: PieChart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore
        val auth = FirebaseAuth.getInstance().currentUser
        val docRef = db.collection(auth!!.uid)

        chart = view.findViewById(R.id.pieChart)

// Create an empty list of entries
        val entries = ArrayList<PieEntry>()
        var entryCounter = 0

// Create a data set from the entries
        val dataSet = PieDataSet(entries, "Colors")

        val table2: TableLayout =view.findViewById(R.id.total_show)

        val tableLayout = view.findViewById<TableLayout>(R.id.total_show)




        val baseColor = Color.rgb(31, 97, 141) // Base color (dark blue)
        val shadeInterval = 20 // Amount to lighten the color for each new entry

// Add the base color to the color list
        val colors = ArrayList<Int>()
        colors.add(baseColor)
        dataSet.colors = colors

// Set the value text size and position outside the slices
        dataSet.valueTextSize = 12f
        dataSet.setValueLinePart1OffsetPercentage(80f)
        dataSet.setValueLinePart1Length(0.5f)
        dataSet.setValueLinePart2Length(0.4f)
        dataSet.isValueLineVariableLength = true

// Create a data object from the data set
        val data = PieData(dataSet)
        chart.data = data

// Customize the chart as needed
     //   chart.setUsePercentValues(true)
        chart.setDrawEntryLabels(false) // Disable default entry labels
        chart.description.isEnabled = false
// Add legend
        val legend = chart.legend
        legend.isEnabled = true
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.textSize = 12f
        legend.xEntrySpace = 10f
        legend.yEntrySpace = 10f
        legend.yOffset = 0f

        chart.setExtraOffsets(0f, 0f, 50f, 0f) // Adjust chart offsets to make space for legend

        chart.isRotationEnabled = true
        chart.animateY(1000)

        docRef.get().addOnSuccessListener { documents ->
            for (document in documents) {

                val layoutParams = TableRow.LayoutParams(0, 90)

                layoutParams.bottomMargin = 10

                val tableRow1 = TableRow(context)
                layoutParams.weight = 2F

                tableRow1.layoutParams = layoutParams

                val textView1 = TextView(context)
                textView1.layoutParams = layoutParams

                tableRow1.addView(textView1)

                val textView2 = TextView(context)
                layoutParams.weight = 1F

                textView2.layoutParams = layoutParams

                tableRow1.addView(textView2)


                val textView3 = TextView(context)
                layoutParams.weight = 2F
                textView3.layoutParams = layoutParams
                tableRow1.addView(textView3)

                textView1.textSize = 18F
                textView1.gravity = Gravity.CENTER
                textView1.setTypeface(null, Typeface.BOLD)
                textView1.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
                textView1.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey
                    )
                )
                textView1.setPadding(0, 2, 0, 2)
                textView1.setAllCaps(true)

                textView2.textSize = 18F
                textView2.gravity = Gravity.CENTER
                textView2.setTypeface(null, Typeface.BOLD)
                textView2.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
                textView2.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey
                    )
                )
                textView2.setPadding(0, 2, 0, 2)
                textView2.setAllCaps(true)

                textView3.textSize = 18F
                textView3.gravity = Gravity.CENTER
                textView3.setTypeface(null, Typeface.BOLD)
                textView3.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
                textView3.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey
                    )
                )
                textView3.setPadding(0, 2, 0, 2)
                textView3.setAllCaps(true)



                val data = document.data
                val name = data["medName"].toString()
                var days = data["days"].toString().toFloat()

                val total = data["days1"].toString().toFloat()


                val newEntry = PieEntry(days, name)
                entries.add(newEntry)

                // Lighten the color for each new entry
                val newColor = lightenColor(baseColor, shadeInterval * entryCounter)
                colors.add(newColor)

                entryCounter++

                textView1.text=name
                days=total-days

                textView2.text= "${days.toInt()} / ${total.toInt()}"

                if (total.toString() == "0")
                    textView3.text = "0"
                else {

                    val percentage = (days!!.toFloat() / total!!.toFloat()) * 100
                    textView3.text = percentage.toInt().toString()+"%"

                    if(total>10 && percentage.toInt()<75)
                        textView3.setTextColor(android.graphics.Color.parseColor("#E40F0F"))

                }


                tableLayout.addView(tableRow1)
            }

            // Notify the data set that the data has changed
            dataSet.notifyDataSetChanged()

            // Refresh the chart
            chart.notifyDataSetChanged()
            chart.invalidate()

        }





    }
    private fun lightenColor(color: Int, amount: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] += amount / 100f // Increase the value (brightness) component
        return Color.HSVToColor(hsv)
    }
}