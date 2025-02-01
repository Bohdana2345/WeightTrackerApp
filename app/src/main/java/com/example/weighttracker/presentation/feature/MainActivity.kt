package com.example.weighttracker.presentation.feature
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.weighttracker.R
import com.example.weighttracker.data.model.RecordsViewModel
import com.example.weighttracker.presentation.ui.WeightTrackerTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import com.example.weighttracker.data.WeightRecord
import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val recordsViewModel: RecordsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        setContent {
                WeightTrackerTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Transparent
                    ) {
                        MainPage(recordsViewModel = recordsViewModel)
                    }
                }
            }
        }
    }

@Composable
fun MainPage(recordsViewModel: RecordsViewModel) {
    val records by recordsViewModel.allRecords.observeAsState(emptyList())
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val recordToDelete = remember { mutableStateOf<WeightRecord?>(null) }

    if (showDialog.value && recordToDelete.value != null) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Delete Record") },
            text = { Text("Are you sure you want to delete this record???") },
            confirmButton = {
                TextButton(
                    onClick = {
                        recordToDelete.value?.let { recordsViewModel.removeRecord(it) }
                        showDialog.value = false
                    }
                ) {
                    Text("Yep")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Nah")
                }
            }
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.main_screen_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.main_screen_headline_ll_width))
                .padding(top = dimensionResource(id = R.dimen.main_screen_headline_ll_margin_top))
        ) {
            Text(
                text = stringResource(id = R.string.track_weight_text),
                fontFamily = FontFamily(Font(R.font.gliker_regular)),
                fontSize = dimensionResource(id = R.dimen.data_entry_headline_text_size).value.sp,
                color = Color.White,
                textAlign = TextAlign.Start,
                lineHeight = dimensionResource(id = R.dimen.main_headline_line_height).value.sp,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.main_screen_headline_padding_start))
            )
        }

        Column(

            modifier = Modifier.fillMaxSize()
                .padding(top = (dimensionResource(id = R.dimen.main_screen_margin_top_recycler_view))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyColumn(
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.main_screen_recycler_view_width))
                    .height(dimensionResource(id = R.dimen.main_screen_recycler_view_height))
                    .padding(dimensionResource(id = R.dimen.main_screen_recycler_view_padding))
                    .testTag("rvRecordsList")
            ) {
                if (records.isEmpty()) {
                    item {
                        Text(
                            text = "No records yet",
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.spacing_between_elements)),
                            style = TextStyle(
                                fontSize = dimensionResource(id = R.dimen.record_text_size_date).value.sp,
                                color = Color.Gray
                            )
                        )
                    }
                } else {
                    items(records) { record ->
                        val parsedDate = try {
                            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            record.date?.let { inputFormat.parse(it) }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }

                        val formattedDate = parsedDate?.let {
                            val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                            outputFormat.format(it)
                        } ?: "???"

                        Row(
                            modifier = Modifier
                                .width(dimensionResource(id = R.dimen.record_block_width))
                                .padding(dimensionResource(id = R.dimen.output_field_padding))
                                .border(
                                    width = dimensionResource(id = R.dimen.record_border_width),
                                    color = Color.White,
                                    shape = RectangleShape
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "${record.weight} kg",
                                modifier = Modifier
                                    .width(dimensionResource(id = R.dimen.weight_output_width))
                                    .padding(dimensionResource(id = R.dimen.output_field_padding)),
                                fontFamily = FontFamily(Font(R.font.gliker_regular)),
                                fontSize = dimensionResource(id = R.dimen.record_text_size_weight).value.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = formattedDate,
                                modifier = Modifier
                                    .width(dimensionResource(id = R.dimen.date_output_width))
                                    .padding(dimensionResource(id = R.dimen.output_field_padding)),
                                fontFamily = FontFamily(Font(R.font.gliker_regular)),
                                fontSize = dimensionResource(id = R.dimen.record_text_size_date).value.sp,
                                color = colorResource(id = R.color.main_button_color),
                                textAlign = TextAlign.Center
                            )

                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                modifier = Modifier
                                    .padding(dimensionResource(id = R.dimen.delete_button_padding))
                                    .clickable {
                                        recordToDelete.value = record
                                        showDialog.value = true
                                    },
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                val intent = Intent(context, DataEntryScreen::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.add_button_width))
                .align(Alignment.BottomCenter)
                .padding(bottom = dimensionResource(id = R.dimen.margin_bottom_add_button)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.main_button_color)
            )
        ) {
            Text(
                text = stringResource(id = R.string.add),
                fontFamily = FontFamily(Font(R.font.gliker_regular)),
                fontSize = dimensionResource(id = R.dimen.text_size_add_button).value.sp,
                color = colorResource(id = R.color.custom_bg_color),
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.padding_start_add_button),
                    end = dimensionResource(id = R.dimen.padding_end_add_button),
                    top = dimensionResource(id = R.dimen.padding_top_add_button),
                    bottom = dimensionResource(id = R.dimen.padding_bottom_add_button)
                )
            )
        }
    }
}

