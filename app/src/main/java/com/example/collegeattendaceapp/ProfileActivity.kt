package com.example.collegeattendaceapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val userName = CollegeData.readUserName(this)
            val userEmail = CollegeData.readMail(this)

            ProfileScreen(
                userName, userEmail
            )

        }
    }
}


@Composable
fun ProfileScreen(
    name: String = "John Doe",
    email: String = "johndoe@example.com",
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(id = R.color.p3)
                )
                .padding(horizontal = 12.dp)
                .clickable {
                    (context as Activity).finish()
                },
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
                text = "Profile",
                color = Color.White,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            val profileImage = CollegeData.getStudentPhoto(context)

            Image(
                bitmap = decodeBase64ToBitmap(profileImage)!!.asImageBitmap(),
                contentDescription = "Student Image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 8.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            Text(
                text = name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email
            Text(
                text = email,
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier
                    .clickable {
                        CollegeData.writeLS(context, false)

                        context.startActivity(Intent(context, FacultyLoginActivity::class.java))
                        (context as Activity).finish()
                    }
                    .width(200.dp)
                    .padding(horizontal = 12.dp)
                    .background(
                        color = Color.Black,
                        shape = RoundedCornerShape(
                            10.dp
                        )
                    )
                    .border(
                        width = 2.dp,
                        color = colorResource(id = R.color.black),
                        shape = RoundedCornerShape(
                            10.dp
                        )
                    )
                    .padding(vertical = 12.dp, horizontal = 12.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Logout",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = colorResource(id = R.color.white),
                )
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
//    ProfileScreen()
}
