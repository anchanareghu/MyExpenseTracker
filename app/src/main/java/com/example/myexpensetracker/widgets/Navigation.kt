package com.example.myexpensetracker.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myexpensetracker.R
import com.example.myexpensetracker.addexpense.AddExpense
import com.example.myexpensetracker.home.HomeScreen
import com.example.myexpensetracker.statistics.StatisticsScreen
import com.example.myexpensetracker.ui.theme.Blue
import com.example.myexpensetracker.user.SignUpPage
import com.example.myexpensetracker.user.UserViewModel
import com.example.myexpensetracker.user.isFirstTimeUser
import com.example.myexpensetracker.user.setFirstTimeUser

@Composable
fun NavHostScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    var bottomAppBarState by remember { mutableStateOf(true) }
    val userViewModel: UserViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = bottomAppBarState) {
                BottomNavigationBar(
                    items = listOf(
                        NavigationItem(
                            name = "Home",
                            route = "home",
                            icon = Icons.Default.Home
                        ),
                        NavigationItem(
                            name = "Statistics",
                            route = "statistics",
                            icon = ImageVector.vectorResource(
                                id = R.drawable.statistics
                            )
                        )
                    ),
                    navController = navController,
                    onItemClick = {
                        navController.navigate(it.route)
                    }
                )
            }
        },
    ) {
        NavHost(
            navController = navController,
            startDestination = if (isFirstTimeUser(LocalContext.current)) "signup" else "home",
            modifier = Modifier.padding(it)
        ) {
            composable("signup") {
                bottomAppBarState = false
                SignUpPage(navController, userViewModel) {
                    setFirstTimeUser(context, false)
                    navController.navigate("home") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            }
            composable("home") {
                bottomAppBarState = true
                HomeScreen(navController)
            }
            composable("add") {
                bottomAppBarState = false
                AddExpense(navController)
            }
            composable("statistics") {
                bottomAppBarState = true
                StatisticsScreen(navController)
            }
        }
    }
}

data class NavigationItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun BottomNavigationBar(
    items: List<NavigationItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (NavigationItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(modifier = modifier, containerColor = Color.Gray, tonalElevation = 5.dp) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Blue,
                    selectedIconColor = Color.Gray,
                    unselectedIconColor = Blue,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = Blue
                ),
                modifier = Modifier
                    .weight(1f),
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(80.dp)
                    ) {
                        Icon(imageVector = item.icon, contentDescription = item.name)
                        if (selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                style = LocalTextStyle.current.copy(fontSize = 8.sp)
                            )
                        }
                    }
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NavHostScreenPreview() {
    NavHostScreen()
}
