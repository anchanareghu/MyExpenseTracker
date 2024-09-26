package com.example.myexpensetracker.addexpense

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myexpensetracker.data.model.ExpenseEntity
import com.example.myexpensetracker.ui.theme.Blue
import com.example.myexpensetracker.ui.theme.MyExpenseTrackerTheme
import com.example.myexpensetracker.ui.theme.Purple
import com.example.myexpensetracker.ui.theme.Purple40
import com.example.myexpensetracker.ui.theme.Red
import com.example.myexpensetracker.utils.Utils
import kotlinx.coroutines.launch

@Composable
fun AddExpense(
    navController: NavController
) {
    val viewModel: AddExpenseViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (nameRow, card, topBar) = createRefs()
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
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 38.dp, start = 16.dp, end = 16.dp)
            .constrainAs(nameRow) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                )
            }
            Text(
                text = "Add Expense",
                fontSize = 16.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
                    .align(Alignment.Center)
            )
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
                    .align(Alignment.CenterEnd)
            )
        }
        DataFormCard(
            modifier = Modifier
                .padding(top = 28.dp, start = 16.dp, end = 16.dp)
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            onAddExpense = {
                coroutineScope.launch {
                    viewModel.addExpense(it)
                    navController.popBackStack()
                }
            }
        )
    }
}

@Composable
fun DataFormCard(modifier: Modifier, onAddExpense: (model: ExpenseEntity) -> Unit) {
    val name = rememberSaveable{ mutableStateOf("") }
    val amount = rememberSaveable { mutableStateOf("") }
    val category = rememberSaveable { mutableStateOf("Upwork") }
    val type = rememberSaveable { mutableStateOf("Income") }
    val currency = rememberSaveable { mutableStateOf("USD") }
    val date = rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    val dateDialogVisible = remember { mutableStateOf(false) }
    val expanded = remember { mutableStateOf(false) }

    val nameError = rememberSaveable { mutableStateOf("") }
    val amountError = rememberSaveable { mutableStateOf("") }

    val textFieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color.White,
        unfocusedLabelColor = Color.Black,
        disabledContainerColor = Color.White,
        focusedContainerColor = Color.White,
        disabledTextColor = Color.Black,
        focusedTextColor = Color.Black,
        focusedIndicatorColor = Purple,
        unfocusedTextColor = Color.Black,
        focusedSupportingTextColor = Color.Black,
        cursorColor = Color.Black,
        errorContainerColor = Color.White,
        errorCursorColor = Red,
        errorLabelColor = Red,
        errorLeadingIconColor = Red,
        errorTrailingIconColor = Red,
        errorIndicatorColor = Red
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
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
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "NAME", fontSize = 14.sp, color = Color.Gray)
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {
                        name.value = it
                        nameError.value = ""
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Brush.horizontalGradient(listOf(Purple, Blue)),
                            shape = RoundedCornerShape(4.dp)
                        ),
                    colors = textFieldColors,
                    isError = nameError.value.isNotEmpty()
                )
                if (nameError.value.isNotEmpty()) {
                    Text(
                        text = nameError.value,
                        fontSize = 12.sp,
                        color = Red
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "CATEGORY", fontSize = 14.sp, color = Color.Gray)
                CategoryDropDown(
                    categories = listOf(
                        "Upwork",
                        "Netflix",
                        "Spotify",
                        "Youtube",
                        "Starbucks",
                        "Paypal",
                        "Google Pay",
                        "Mastercard",
                        "Other"
                    ),
                    onCategorySelected = { category.value = it },
                    color = Color.Unspecified
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "AMOUNT", fontSize = 14.sp, color = Color.Gray)
                OutlinedTextField(
                    value = amount.value,
                    onValueChange = {
                        amount.value = it
                        amountError.value = ""
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Brush.horizontalGradient(listOf(Purple, Blue)),
                            shape = RoundedCornerShape(4.dp)
                        ),
                    trailingIcon = {
                        Box {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.wrapContentSize()
                            ) {
                                Text(
                                    text = currency.value,
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .clickable { expanded.value = true }
                                )
                            }
                        }
                    },
                    colors = textFieldColors,
                    isError = amountError.value.isNotEmpty(),
                )
                if (amountError.value.isNotEmpty()) {
                    Text(
                        text = amountError.value,
                        fontSize = 12.sp,
                        color = Red
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "DATE", fontSize = 14.sp, color = Color.Gray)
                OutlinedTextField(
                    value = Utils.formatDateForCalender(date.longValue),
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .border(
                            1.dp,
                            Brush.horizontalGradient(listOf(Purple, Blue)),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable { dateDialogVisible.value = true },
                    enabled = false,
                    colors = textFieldColors,
                )


                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "TYPE", fontSize = 14.sp, color = Color.Gray)
                CategoryDropDown(
                    categories = listOf("Income", "Expense"),
                    onCategorySelected = { type.value = it },
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        var valid = true
                        if (name.value.isEmpty()) {
                            nameError.value = "*name is required"
                            valid = false
                        } else if (amount.value.isEmpty()) {
                            amountError.value = "*amount is required"
                            valid = false
                        } else {
                            val amountValue = amount.value.toDoubleOrNull()
                            if (amountValue == null) {
                                amountError.value = "Invalid amount"
                                valid = false
                            }
                        }
                        if (valid) {
                            onAddExpense(
                                ExpenseEntity(
                                    id = 0,
                                    title = name.value,
                                    amount = amount.value.toDouble(),
                                    category = category.value,
                                    type = type.value,
                                    date = date.longValue.toString()
                                )
                            )
                            name.value = ""
                            amount.value = ""
                            category.value = "Upwork"
                            type.value = "Income"
                            currency.value = "USD"
                            date.longValue = System.currentTimeMillis()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Purple, Blue)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            )
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Add", color = Color.DarkGray)
                    }
                }
            }
        }
        if (dateDialogVisible.value) {
            DatePickerDialog(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Purple, Blue)
                        ), RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp)),
                onDateSelected = {
                    date.value = it
                    dateDialogVisible.value = false
                }, onDismissRequest = {
                    dateDialogVisible.value = false
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    modifier: Modifier = Modifier,
    onDateSelected: (Long) -> Unit,
    onDismissRequest: () -> Unit,

    ) {
    val datePickerState = rememberDatePickerState()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = null,
        text = {
            DatePicker(
                state = datePickerState,
                modifier = modifier
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Purple, Blue)
                        )
                    ),
                colors = DatePickerDefaults.colors(
                    containerColor = Purple,
                    titleContentColor = Color.Black,
                    headlineContentColor = Color.Black,
                    weekdayContentColor = Color.Black,
                    subheadContentColor = Color.Black,
                    navigationContentColor = Color.Black,
                    yearContentColor = Color.Black,
                    disabledYearContentColor = Color.Black,
                    currentYearContentColor = Purple40,
                    selectedYearContentColor = Purple40,
                    disabledSelectedYearContentColor = Blue,
                    selectedYearContainerColor = Color.White,
                    disabledSelectedYearContainerColor = Purple,
                    dayContentColor = Color.Black,
                    disabledDayContentColor = Color.Black,
                    selectedDayContentColor = Purple40,
                    disabledSelectedDayContentColor = Blue,
                    selectedDayContainerColor = Color.White,
                    disabledSelectedDayContainerColor = Blue,
                    todayContentColor = Purple40,
                    todayDateBorderColor = Purple40,
                    dayInSelectionRangeContainerColor = Color.White,
                    dayInSelectionRangeContentColor = Color.Black,
                    dividerColor = Color.White,
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(
                        datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            ) {
                Text("Select")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            ) {
                Text("Cancel")
            }
        },
        modifier = modifier
            .background(
                Brush.horizontalGradient(
                    colors = listOf(Purple, Blue)
                )
            ),
        containerColor = Color.Transparent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDown(
    categories: List<String>,
    onCategorySelected: (String) -> Unit,
    color: Color
) {
    val viewModel: AddExpenseViewModel = hiltViewModel()
    val expanded = remember { mutableStateOf(false) }

    val selectedCategory = rememberSaveable { mutableStateOf(categories.first()) }

    val textFieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color.White,
        unfocusedLabelColor = Color.Black,
        disabledContainerColor = Blue,
        focusedContainerColor = Color.White,
        disabledTextColor = Color.Black,
        focusedTextColor = Color.Black,
        focusedIndicatorColor = Purple,
        unfocusedTextColor = Color.Black,
        focusedSupportingTextColor = Color.Black,
        cursorColor = Color.Black,
    )
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = it
        }
    ) {
        OutlinedTextField(
            value = selectedCategory.value,
            onValueChange = {
                selectedCategory.value = it
            },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(
                    1.dp,
                    Brush.horizontalGradient(listOf(Purple, Blue)),
                    shape = RoundedCornerShape(4.dp)
                )
                .menuAnchor(),
            colors = textFieldColors,
            leadingIcon = {

                Icon(
                    painter = painterResource(
                        id = viewModel.getItemIcon(
                            selectedCategory.value,
                            selectedCategory.value
                        ),
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(8.dp),
                    tint = color
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded.value
                )
            }
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.background(
                Brush.horizontalGradient(listOf(Purple, Blue))
            )
        ) {
            categories.forEach {
                DropdownMenuItem(
                    text = { Text(it, color = Color.Black) },
                    onClick = {
                        selectedCategory.value = it
                        onCategorySelected(selectedCategory.value)
                        expanded.value = false
                    },
                    colors = MenuItemColors(
                        textColor = LocalContentColor.current,
                        disabledTextColor = LocalContentColor.current,
                        disabledTrailingIconColor = Color.Black,
                        leadingIconColor = Color.Unspecified,
                        trailingIconColor = Color.Black,
                        disabledLeadingIconColor = Color.Black
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = viewModel.getItemIcon(it, it)),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddExpensePreview() {
    MyExpenseTrackerTheme {
        AddExpense(
            navController = NavController(LocalContext.current)
        )
    }
}