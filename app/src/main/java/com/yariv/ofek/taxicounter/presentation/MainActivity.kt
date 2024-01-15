package com.yariv.ofek.taxicounter.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yariv.ofek.taxicounter.presentation.calculation.compose.CalculationFragment
import com.yariv.ofek.taxicounter.presentation.fragments.OrderFragment
import com.yariv.ofek.taxicounter.presentation.fragments.RealTimeCounterFragment
import com.yariv.ofek.taxicounter.presentation.navigation.NavigationItem
import com.yariv.ofek.taxicounter.presentation.theme.Moniton2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Moniton2Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainContent()
                }
            }
        }
    }
}


@Composable
fun MainContent() {
    val navController = rememberNavController()
    val items = remember { NavigationItem.entries.toTypedArray() }
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry.value?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = item.icon),
                                contentDescription = item.title.asString(),
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text(item.title.asString()) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.Calculation.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationItem.RealTimeCounter.route) { RealTimeCounterFragment() }
            composable(NavigationItem.Calculation.route) { CalculationFragment() }
            composable(NavigationItem.Order.route) { OrderFragment() }
        }
    }
}