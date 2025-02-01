package com.example.weighttracker.presentation.feature
import java.text.SimpleDateFormat
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weighttracker.MyApp
import com.example.weighttracker.R
import com.example.weighttracker.data.WeightRecord
import com.example.weighttracker.presentation.ui.WeightTrackerTheme
import kotlin.math.roundToInt
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextStyle
import com.chargemap.compose.numberpicker.ListItemPicker
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType

class DataEntryScreen : AppCompatActivity() {
    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()

        setContent {
            WeightTrackerTheme {
                val step = 0.1
                val minValue = 30.0
                val maxValue = 200.0

                val locale = Locale.getDefault()
                val decimalValues = remember {
                    Array(((maxValue - minValue) / step + 1).toInt()) { i ->
                        String.format(locale, "%.1f", minValue + i * step)
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    val weightIndex = remember { mutableIntStateOf(0) }
                    val weight = decimalValues[weightIndex.intValue].toDouble()

                    DataEntryScreenPage(
                        onSaveClick = {
                            val roundedWeight = ((weight * 10).roundToInt() / 10.0)
                            val date = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
                            val newRecord = WeightRecord(weight = roundedWeight, date = date)

                            CoroutineScope(Dispatchers.IO).launch {
                                MyApp.database.weightRecordDao().insertWeightRecord(newRecord)
                            }

                            Toast.makeText(this@DataEntryScreen, "Saved successfully :)", Toast.LENGTH_SHORT).show()
                            finish()
                        },
                        onWeightChange = { newIndex -> weightIndex.intValue = newIndex },
                        decimalValues = decimalValues
                    )
                }
            }
        }
    }
}

@Composable
fun DataEntryScreenPage(
    onSaveClick: () -> Unit,
    onWeightChange: (Int) -> Unit,
    decimalValues: Array<String>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.data_entry_screen_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.weight_block_height))
                .width(dimensionResource(id = R.dimen.weight_block_width)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_between_elements), Alignment.Bottom)
        ) {
            Text(
                text = stringResource(id = R.string.log_weight_journey),
                fontFamily = FontFamily(Font(R.font.gliker_regular)),
                fontSize = dimensionResource(id = R.dimen.data_entry_headline_text_size).value.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = dimensionResource(id = R.dimen.data_entry_headline_line_height).value.sp,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_bottom_headline))
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                var weightIndex by remember { mutableIntStateOf(0) }
                var state by remember { mutableStateOf(decimalValues[weightIndex]) }
                var textFieldValue by remember { mutableStateOf(decimalValues[weightIndex]) }

                TextField(
                    value = textFieldValue,
                    onValueChange = { input ->
                        textFieldValue = input
                        if (decimalValues.contains(input)) {
                            state = input
                            weightIndex = decimalValues.indexOf(input)
                            onWeightChange(weightIndex)
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(
                        fontSize = dimensionResource(id = R.dimen.text_field_font_size).value.sp,
                    ),
                    modifier = Modifier
                        .width(dimensionResource(id = R.dimen.text_field_width))
                )

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_between_textfield_picker)))

                ListItemPicker(
                    label = { it },
                    value = state,
                    onValueChange = { newValue ->
                        if (decimalValues.contains(newValue)) {
                            state = newValue
                            textFieldValue = newValue
                            val newIndex = decimalValues.indexOf(newValue)
                            if (newIndex != -1) {
                                onWeightChange(newIndex)
                            }
                        }
                    },
                    list = decimalValues.toList(),
                    textStyle = TextStyle(
                        fontSize = dimensionResource(id = R.dimen.text_field_font_size).value.sp,
                    ),
                    modifier = Modifier
                        .border(1.dp, Color.White, shape = RoundedCornerShape(dimensionResource(id = R.dimen.border_radius)))
                )

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_between_textfield_picker)))

                Text(
                    text = stringResource(id = R.string.weight_unit),
                    fontSize = dimensionResource(id = R.dimen.data_entry_units_text_size).value.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_start_weight_unit), end = dimensionResource(id = R.dimen.padding_end_weight_unit))
                )
            }

            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.save_button_width))
                    .height(dimensionResource(id = R.dimen.save_button_height))
                    .background(color = Color.Transparent, shape = RoundedCornerShape(dimensionResource(id = R.dimen.save_button_corner_radius))),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.button_color)
                )
            ) {
                Text(
                    text = stringResource(id = R.string.add),
                    fontFamily = FontFamily(Font(R.font.gliker_regular)),
                    fontSize = dimensionResource(id = R.dimen.data_entry_save_button_text_size).value.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DataEntryScreenPagePreview() {
    WeightTrackerTheme {
        DataEntryScreenPage(
            onSaveClick = {},
            onWeightChange = {},
            decimalValues = arrayOf("30.0", "30.1", "30.2")
        )
    }
}
