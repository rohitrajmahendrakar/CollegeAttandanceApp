package com.example.collegeattendaceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MarkAttendanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarkAttendanceScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MarkAttendanceScreenPreview() {
    MarkAttendanceScreen()
}

@Composable
fun MarkAttendanceScreen() {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AttendanceFormScreen()
    }
}

@Composable
fun AttendanceFormScreen() {
    var selectedCourse by remember { mutableStateOf("") }
    var classTime by remember { mutableStateOf("") }
    var attendanceStatus by remember { mutableStateOf("Present") }
    var gpsStatus by remember { mutableStateOf(true) }
    var confirmationMessage by remember { mutableStateOf("") }

    val courseOptions = listOf("Mathematics", "Physics", "History", "Computer Science")
    val attendanceOptions = listOf("Present", "Absent", "Late")

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(id = R.color.p3)
                )
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier.size(36.dp),
                painter = painterResource(id = R.drawable.baseline_arrow_back_36),
                contentDescription = "Back",
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                text = "Mark Attendance",
                color = Color.White,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {


            Spacer(modifier = Modifier.height(16.dp))

            // Course Dropdown
            Text("Select Course")
            DropdownMenuWithLabel(
                options = courseOptions,
                selectedOption = selectedCourse,
                onOptionSelected = { selectedCourse = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Class Time
            OutlinedTextField(
                value = classTime,
                onValueChange = { classTime = it },
                label = { Text("Class Time") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Attendance Status Radio Buttons
            Text("Attendance Status")
            attendanceOptions.forEach { status ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = attendanceStatus == status,
                        onClick = { attendanceStatus = status }
                    )
                    Text(status)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // GPS Status
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("GPS Status: ")
                Text(if (gpsStatus) "✅ In Location" else "❌ Out of Location")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    confirmationMessage = if (gpsStatus) {
                        "✅ Attendance marked as $attendanceStatus"
                    } else {
                        "❌ Unable to mark attendance - Out of location"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (confirmationMessage.isNotEmpty()) {
                Text(
                    text = confirmationMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (confirmationMessage.startsWith("✅")) Color.Green else Color.Red
                )
            }
        }
    }
}

@Composable
fun DropdownMenuWithLabel(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text("Course Name") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
