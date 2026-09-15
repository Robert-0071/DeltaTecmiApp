package com.example.actividad1aplicacion.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.actividad1aplicacion.ui.screens.AdaptiveNoteScreen
import com.example.actividad1aplicacion.ui.screens.NoteFormScreen
import com.example.actividad1aplicacion.ui.screens.NoteListScreen
import com.example.actividad1aplicacion.ui.screens.ProductFormScreen
import com.example.actividad1aplicacion.ui.screens.ProductListScreen
import com.example.actividad1aplicacion.ui.screens.SettingsScreen
import com.example.actividad1aplicacion.viewmodel.ProductViewModel
import com.example.notemanager.data.local.NoteEntity
import com.example.notemanager.ui.NoteUiEvent
import com.example.notemanager.ui.NoteViewModel

@Composable
fun DeltaNavHost(
    windowSize: WindowSizeClass,
    productViewModel: ProductViewModel = hiltViewModel(),
    noteViewModel: NoteViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val products by productViewModel.products.collectAsState()
    val notes by noteViewModel.notesState.collectAsState()
    val isColorBlind by productViewModel.isColorBlindMode.collectAsState()
    val isTalkBack by productViewModel.isTalkBackMode.collectAsState()

    val isExpanded = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded

    NavHost(navController = navController, startDestination = "product_list") {
        // PANTALLA 1: PRODUCTOS
        composable("product_list") {
            ProductListScreen(
                products = products,
                isColorBlind = isColorBlind,
                onAddClick = { navController.navigate("product_form") },
                onSettingsClick = { navController.navigate("settings") },
                onDeleteProduct = { productViewModel.deleteProduct(it) },
                onFavoriteClick = { id, status -> productViewModel.toggleFavorite(id, status) },
                onEditClick = { id -> navController.navigate("edit_product/$id") },
                onGoToNotes = { navController.navigate("note_list") }
            )
        }
        
        // PANTALLA 2: LISTA DE NOTAS (Adaptable)
        composable("note_list") {
            if (isExpanded) {
                AdaptiveNoteScreen(
                    notes = notes,
                    onAddNoteClick = { navController.navigate("note_form") },
                    onSettingsClick = { navController.navigate("settings") },
                    onDeleteNote = { note -> noteViewModel.onEvent(NoteUiEvent.DeleteNote(note)) },
                    onToggleComplete = { note -> noteViewModel.onEvent(NoteUiEvent.ToggleComplete(note)) },
                    onBackToHome = { navController.navigate("product_list") { popUpTo("product_list") { inclusive = true } } }
                )
            } else {
                NoteListScreen(
                    notes = notes,
                    onAddNoteClick = { navController.navigate("note_form") },
                    onSettingsClick = { navController.navigate("settings") },
                    onDeleteNote = { note -> noteViewModel.onEvent(NoteUiEvent.DeleteNote(note)) },
                    onToggleComplete = { note -> noteViewModel.onEvent(NoteUiEvent.ToggleComplete(note)) },
                    onBackToHome = { navController.navigate("product_list") { popUpTo("product_list") { inclusive = true } } },
                    onGoToFavs = { navController.navigate("product_list") }
                )
            }
        }

        // FORMULARIOS PRODUCTOS
        composable("product_form") {
            ProductFormScreen(
                onSaveProduct = { name, store, price ->
                    productViewModel.addProduct(name, store, price)
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
                        productViewModel.updateProduct(productId, name, store, price)
                    }
                    navController.popBackStack()
                },
                onDeleteProduct = {
                    if (productId != null) {
                        productViewModel.deleteProduct(productId)
                        navController.popBackStack()
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // FORMULARIO NOTAS
        composable("note_form") {
            NoteFormScreen(
                onSaveNote = { title, content, uri ->
                    noteViewModel.onEvent(NoteUiEvent.SaveNote(title, content, uri))
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        // AJUSTES
        composable("settings") {
            SettingsScreen(
                isColorBlind = isColorBlind,
                isTalkBack = isTalkBack,
                onColorBlindToggle = { productViewModel.setColorBlindMode(it) },
                onTalkBackToggle = { productViewModel.setTalkBackMode(it) },
                onLogout = onLogout,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
