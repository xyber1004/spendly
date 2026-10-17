package com.example.spendly.navigation

import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spendly.feature.add_expense.screens.AddExpenseRoute
import com.example.spendly.feature.expenses.screens.ExpensesRoute
import kotlinx.serialization.Serializable

@Serializable
data object ExpensesDestination

@Serializable
data object AddExpenseDestination

@Composable
fun SpendlyNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = ExpensesDestination) {
        composable<ExpensesDestination> {
            ExpensesRoute(onAddExpense = { navController.navigate(AddExpenseDestination) })
        }
        composable<AddExpenseDestination> {
            AddExpenseRoute(onDone = { navController.navigateUp() })
        }
    }
}