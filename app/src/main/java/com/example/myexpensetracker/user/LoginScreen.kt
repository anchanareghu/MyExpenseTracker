package com.example.myexpensetracker.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myexpensetracker.R
import com.example.myexpensetracker.ui.theme.Blue
import com.example.myexpensetracker.ui.theme.Purple

@Composable
fun SignUpPage(navController: NavController, userViewModel: UserViewModel, signUp: () -> Unit) {
    val nameState = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Purple, Blue)
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
        )
        Text(
            text = "Welcome! Enter your name to continue",
            fontSize = 36.sp, color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(28.dp)
        )
        BasicTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 12.dp),
            decorationBox = { innerTextField ->
                TextField(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    label = { Text(text = "Enter your name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                innerTextField()
            }
        )

        TextButton(
            onClick = {
                userViewModel.updateName(nameState.value)
                signUp()
                navController.navigate("home")
            },
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 12.dp)
                .background(Color.Black)
        ) {
            Text(
                text = "Continue",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SignUpPagePreview() {
    SignUpPage(
        navController = rememberNavController(),
        userViewModel = UserViewModel(LocalContext.current),
        signUp = {}
    )
}