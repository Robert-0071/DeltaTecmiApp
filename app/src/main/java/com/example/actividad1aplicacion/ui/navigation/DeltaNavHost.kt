package com.example.actividad1aplicacion.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.actividad1aplicacion.ui.screens.ProductFormScreen
import com.example.actividad1aplicacion.ui.screens.ProductListScreen
import com.example.actividad1aplicacion.ui.screens.SettingsScreen
import com.example.actividad1aplicacion.viewmodel.ProductViewModel

@Composable
fun DeltaNavHost(
    viewModel: ProductViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val products by viewModel.products.collectAsState()
    val isColorBlind by viewModel.isColorBlindMode.collectAsState()
    val isTalkBack by viewModel.isTalkBackMode.collectAsState()

    NavHost(navController = navController, startDestination = "product_list") {
        composable("product_list") {
            ProductListScreen(
                products = products,
                isColorBlind = isColorBlind,
                onAddClick = { navController.navigate("product_form") },
                onSettingsClick = { navController.navigate("settings") },
                onDeleteProduct = { viewModel.deleteProduct(it) },
                onFavoriteClick = { id, status -> viewModel.toggleFavorite(id, status) },
                onEditClick = { id -> navController.navigate("edit_product/$id") }
            )
        }
        
        composable("product_form") {
            ProductFormScreen(
                onSaveProduct = { name, store, price ->
                    viewModel.addProduct(name, store, price)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "edit_product/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            val product = products.find { it.id == productId }
            ProductFormScreen(
                product = product,
                onSaveProduct = { name, store, price ->
                    if (productId != null) {
                        viewModel.updateProduct(productId, name, store, price)
                    }
                    navController.popBackStack()
                },
                onDeleteProduct = {
                    if (productId != null) {
                        viewModel.deleteProduct(productId)
                        navController.popBackStack()
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("settings") {
            SettingsScreen(
                isColorBlind = isColorBlind,
                isTalkBack = isTalkBack,
                onColorBlindToggle = { viewModel.setColorBlindMode(it) },
                onTalkBackToggle = { viewModel.setTalkBackMode(it) },
                onLogout = onLogout,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
