package com.example.myexpensetracker.home

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myexpensetracker.R
import com.example.myexpensetracker.data.model.ExpenseEntity
import com.example.myexpensetracker.ui.theme.Blue
import com.example.myexpensetracker.ui.theme.Green
import com.example.myexpensetracker.ui.theme.MyExpenseTrackerTheme
import com.example.myexpensetracker.ui.theme.Purple
import com.example.myexpensetracker.ui.theme.PurpleGrey40
import com.example.myexpensetracker.ui.theme.Red
import com.example.myexpensetracker.user.UserViewModel
import com.example.myexpensetracker.utils.Utils.formatDayMonthYear
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Time

@Composable
fun HomeScreen(navController: NavController) {
    val userViewModel: UserViewModel = hiltViewModel()
    val viewModel: HomeViewModel = hiltViewModel()
    val name by remember(userViewModel.name) { mutableStateOf(userViewModel.name) }
    val expanded = remember { mutableStateOf(false) }
    val currencyState = viewModel.selectedCurrency
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },

        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(listOf(Purple, Blue)),
                        RoundedCornerShape(16.dp)
                    )
            ) {
                AnimatedVisibility(visible = true) {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate("add")
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )
                        },
                        containerColor = Color.Transparent,
                        contentColor = Color.Black,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    )
                }
            }
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color.White)
        ) {
            val (nameRow, list, card, topBar) = createRefs()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Purple, Blue)
                        )
                    )
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                Column(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                ) {
                    Text(
                        text = when {
                            Time(System.currentTimeMillis()).hours < 12 -> "Good Morning,"
                            Time(System.currentTimeMillis()).hours < 18 -> "Good Afternoon,"
                            else -> "Good Evening,"
                        },
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "$name.",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
                Box(
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    IconButton(onClick = { expanded.value = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.currencyconverter),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                        modifier = Modifier.background(
                            Brush.horizontalGradient(
                                colors = listOf(Purple, Blue)
                            )
                        )
                    ) {
                        CurrencyDropdownMenu(
                            currency = "USD",
                            icon = R.drawable.dollar,
                            onClick = {
                                currencyState.value = "USD"
                                expanded.value = false
                            })
                        CurrencyDropdownMenu(
                            currency = "EUR",
                            icon = R.drawable.euro,
                            onClick = {
                                currencyState.value = "EUR"
                                expanded.value = false
                            })
                        CurrencyDropdownMenu(
                            currency = "GBP",
                            icon = R.drawable.pound,
                            onClick = {
                                currencyState.value = "GBP"
                                expanded.value = false
                            })
                        CurrencyDropdownMenu(
                            currency = "INR",
                            icon = R.drawable.indianrupee,
                            onClick = {
                                currencyState.value = "INR"
                                expanded.value = false
                            })
                    }
                }

            }

            val state by viewModel.uiState.collectAsState()
            when (state) {
                is ExpensesUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Purple)
                    }
                }

                is ExpensesUiState.Success -> {
                    val expenses = (state as ExpensesUiState.Success).expenses
                    val expense = viewModel.getExpense(expenses)
                    val income = viewModel.getIncome(expenses)
                    val balance = viewModel.getBalance(expenses)

                    DetailCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 42.dp, start = 16.dp, end = 16.dp)
                            .constrainAs(card) {
                                top.linkTo(nameRow.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        balance = balance,
                        expense = expense,
                        income = income
                    )
                    TransactionList(
                        modifier = Modifier.constrainAs(list) {
                            top.linkTo(card.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                        },
                        list = expenses,
                        viewModel = viewModel,
                        currency = currencyState.value,
                        snackBarHostState = snackBarHostState
                    )
                }

                is ExpensesUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .constrainAs(list) {
                                top.linkTo(card.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.fillToConstraints
                            }
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Failed to load data",
                            color = Color.Red
                        )
                    }

                }
            }

        }
    }
}

@Composable
fun CurrencyDropdownMenu(currency: String, icon: Int, onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(currency) },
        trailingIcon = {
            Icon(
                painter = painterResource(id = icon),
                modifier = Modifier.size(20.dp),
                contentDescription = null,
            )
        },
        onClick = onClick,
        colors = MenuItemColors(
            textColor = Color.Black,
            leadingIconColor = Color.Black,
            trailingIconColor = Color.Black,
            disabledTextColor = Color.Black,
            disabledLeadingIconColor = Color.Black,
            disabledTrailingIconColor = Color.Black
        )
    )
}


@Composable
fun DetailCard(modifier: Modifier, balance: String, expense: String, income: String) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = balance,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "Total Balance",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
        Spacer(modifier = Modifier.height(28.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CardRowItem(
                modifier = Modifier,
                title = "Income",
                amount = income,
                icon = R.drawable.income
            )
            CardRowItem(
                modifier = Modifier,
                title = "Expense",
                amount = expense,
                icon = R.drawable.expenses
            )
        }
    }
}

@Composable
fun CardRowItem(modifier: Modifier, title: String, amount: String, icon: Int) {
    Column {
        Row {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Color.Black,
                modifier = modifier.align(Alignment.CenterVertically)
            )
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
        Text(
            text = amount,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
fun TransactionList(
    modifier: Modifier = Modifier,
    list: List<ExpenseEntity>,
    viewModel: HomeViewModel,
    title: String = "Recent Transactions",
    currency: String,
    snackBarHostState: SnackbarHostState
) {
    var isAscending by remember { mutableStateOf(true) }

    val sortedList = remember(list, isAscending) {
        if (isAscending) {
            list.sortedBy { it.date }
        } else {
            list.sortedByDescending { it.date }
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                color = PurpleGrey40,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            Icon(
                painter = painterResource(
                    id = if (isAscending) R.drawable.increasing else R.drawable.decreasing
                ),
                contentDescription = if (isAscending) "Sort ascending" else "Sort descending",
                tint = PurpleGrey40,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(20.dp)
                    .clickable { isAscending = !isAscending }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (sortedList.isEmpty()) {
            // Show empty state
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.empty),
                    contentDescription = "No transactions",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "No transactions yet",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(sortedList) { item ->
                    TransactionItem(
                        item = item,
                        onRemove = { removedItem ->
                            viewModel.removeItem(removedItem)
                        },
                        onUndo = { removedItem ->
                            viewModel.addItem(removedItem)
                        },
                        icon = viewModel.getItemIcon(item, item.type),
                        color = if (item.type == "Income") Green else Red,
                        currency = currency,
                        snackBarHostState = snackBarHostState
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Red
        SwipeToDismissBoxValue.Settled -> Color.Transparent
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color, RoundedCornerShape(16.dp))
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = "delete",
            tint = Color.Black
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionItem(
    item: ExpenseEntity,
    onRemove: (ExpenseEntity) -> Unit,
    onUndo: (ExpenseEntity) -> Unit,
    icon: Int,
    color: Color,
    currency: String,
    snackBarHostState: SnackbarHostState
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onRemove(item)
                    CoroutineScope(Dispatchers.Main).launch {
                        snackBarHostState.showSnackbar(
                            message = "Transaction deleted",
                            actionLabel = "Undo"
                        ).let {
                            if (it == SnackbarResult.ActionPerformed) {
                                onUndo(item)
                            }
                        }
                    }
                }

                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
                SwipeToDismissBoxValue.EndToStart -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        positionalThreshold = { it * .25f }
    )

    val currencySymbol = when (currency) {
        "USD" -> "$"
        "EUR" -> "€"
        "GBP" -> "£"
        "INR" -> "₹"
        else -> ""
    }
    val currencyAmount = convertCurrency(item.amount, currency).first

    SwipeToDismissBox(
        state = dismissState,
        modifier = Modifier,
        backgroundContent = { DismissBackground(dismissState) },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Purple, Blue)
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(text = item.title, fontSize = 14.sp, color = Color.Black)
                    val date = formatDayMonthYear(item.date.toLong())
                    val today = formatDayMonthYear(System.currentTimeMillis())
                    val yesterday = formatDayMonthYear(System.currentTimeMillis() - 86400000)
                    Text(
                        text = if (date == today) "Today" else if (date == yesterday) "Yesterday" else date,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "$currencySymbol ${"%.2f".format(currencyAmount)}",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    color = color
                )
            }
        }
    )
}

sealed class ExpensesUiState {
    object Loading : ExpensesUiState()
    data class Success(val expenses: List<ExpenseEntity>) : ExpensesUiState()
    data class Error(val message: String) : ExpensesUiState()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    MyExpenseTrackerTheme {
        HomeScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemPreview() {
    TransactionItem(
        item = ExpenseEntity(
            title = "Netflix",
            amount = 100.0,
            category = "Netflix",
            date = "2023-07-01",
            type = "Expense"
        ),
        onRemove = {},
        onUndo = {},
        icon = R.drawable.netflix,
        color = Color.Red,
        currency = "USD",
        snackBarHostState = SnackbarHostState()
    )
}

@Preview(showBackground = true)
@Composable
fun CurrencyMenuItemPreview() {
    CurrencyDropdownMenu(
        currency = "USD",
        icon = R.drawable.dollar,
        onClick = {
        })
}