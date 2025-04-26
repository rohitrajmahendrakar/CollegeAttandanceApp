package com.example.collegeattendaceapp

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collegeattendaceapp.database.AttendanceViewModel
import com.example.collegeattendaceapp.database.getLast30Days
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ViewAttendanceHistoryActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            AttendanceHistoryScreen(AttendanceViewModel())

//            AttendanceHistoryListScreen(email = CollegeData.readMail(this))
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AttendanceHistoryScreen(viewModel: AttendanceViewModel) {
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val attendanceMap =
        remember { mutableStateOf<Map<LocalDate, List<AttendanceData>>>(emptyMap()) }

    // Generate last 30 days
    val last30Days = remember {
        List(30) { LocalDate.now().minusDays(it.toLong()) }.sortedDescending()
    }

    LaunchedEffect(Unit) {
        viewModel.fetchAttendanceHistory(context) { result ->
            val grouped = result.mapNotNull {
                try {
                    val date = LocalDate.parse(it.date, formatter)
                    date to it
                } catch (e: Exception) {
                    null
                }
            }.groupBy({ it.first }, { it.second })


            attendanceMap.value = grouped
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
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
                modifier = Modifier.size(36.dp).clickable {
                    (context as Activity).finish()
                },
                painter = painterResource(id = R.drawable.baseline_arrow_back_36),
                contentDescription = "Back",
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                text = "Attendance History",
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


            LazyColumn {
                items(last30Days.chunked(3)) { date ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        date.forEach { product ->
                            Box(modifier = Modifier.weight(1f)) {
//                            StockItemCard(product)
                                val records = attendanceMap.value[product]

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(color = Color.Magenta),
                                            text = "${product.format(formatter)}",
                                            textAlign = TextAlign.Center,
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        if (records.isNullOrEmpty()) {
                                            Spacer(modifier = Modifier.height(6.dp))

                                            Text(text = "Attendance", color = Color.Gray)

                                            Spacer(modifier = Modifier.height(6.dp))

                                            Text(text = " Not Marked", color = Color.Gray)
                                            Spacer(modifier = Modifier.height(6.dp))

                                        } else {
                                            Spacer(modifier = Modifier.height(6.dp))

                                            val presentCount =
                                                records.count { it.status == "Present" }
                                            val absentCount =
                                                records.count { it.status == "Absent" }
                                            Text(
                                                text = "✅ Present: $presentCount",
                                                color = Color(0xFF388E3C)
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = "❌ Absent: $absentCount",
                                                color = Color(0xFFD32F2F)
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))

                                        }
                                    }
                                }
                            }
                        }
                        if (date.size < 3) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }


//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(8.dp)
//        ) {
//            items(last30Days) { date ->
//                val records = attendanceMap.value[date]
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp),
//                    elevation = CardDefaults.cardElevation(4.dp)
//                ) {
//                    Column(modifier = Modifier.padding(12.dp)) {
//                        Text(
//                            text = "Date: ${date.format(formatter)}",
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                        if (records.isNullOrEmpty()) {
//                            Text(text = "Status: Not Marked", color = Color.Gray)
//                        } else {
//                            val presentCount = records.count { it.status == "Present" }
//                            val absentCount = records.count { it.status == "Absent" }
//                            Text(text = "✅ Present: $presentCount", color = Color(0xFF388E3C))
//                            Text(text = "❌ Absent: $absentCount", color = Color(0xFFD32F2F))
//                        }
//                    }
//                }
//            }
//        }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AttendanceHistoryScreenOld(viewModel: AttendanceViewModel, userEmail: String) {
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    val attendanceList = remember { mutableStateOf<List<AttendanceData>>(emptyList()) }

    LaunchedEffect(Unit) {
        viewModel.fetchAttendanceHistory(context) { result ->
            attendanceList.value = result
        }
    }


    val last30Days = getLast30Days()

//    LazyColumn(modifier = Modifier
//        .fillMaxSize()
//        .padding(8.dp)) {
//        items(last30Days) { date ->
//            val records = attendanceMap[date]
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 4.dp),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                Column(modifier = Modifier.padding(12.dp)) {
//                    Text(
//                        text = "Date: ${date.format(formatter)}",
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                    if (records.isNullOrEmpty()) {
//                        Text(text = "Status: Not Marked", color = Color.Gray)
//                    } else {
//                        val presentCount = records.count { it.status == "Present" }
//                        val absentCount = records.count { it.status == "Absent" }
//                        Text(text = "✅ Present: $presentCount", color = Color(0xFF388E3C))
//                        Text(text = "❌ Absent: $absentCount", color = Color(0xFFD32F2F))
//                    }
//                }
//            }
//        }
//    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AttendanceHistoryListScreen(email: String) {
    val attendanceList = remember { mutableStateListOf<AttendanceData>() }

    LaunchedEffect(email) {
        val emailKey = email.replace(".", ",")
        val ref = FirebaseDatabase.getInstance().getReference("Attendance").child(emailKey)
        val thirtyDaysAgo = LocalDate.now().minusDays(30)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                attendanceList.clear()
                for (child in snapshot.children) {
                    val data = child.getValue(AttendanceData::class.java)
                    data?.let {
                        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        val recordDate = LocalDate.parse(it.date, formatter)

                        if (recordDate.isAfter(thirtyDaysAgo)) {
                            attendanceList.add(it)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Attendance History (Last 30 Days)", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(attendanceList) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Course: ${it.courseName}", fontWeight = FontWeight.Bold)
                        Text("Date: ${it.date}")
                        Text("Time: ${it.time}")
                        Text("Status: ${it.status}")
                    }
                }
            }
        }
    }
}
