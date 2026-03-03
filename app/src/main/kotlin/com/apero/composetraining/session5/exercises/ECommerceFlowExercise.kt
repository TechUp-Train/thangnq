package com.apero.composetraining.session5.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐ BÀI TẬP 3: E-Commerce Flow (Challenge)
 *
 * Yêu cầu:
 * - Category → Product List → Product Detail → Cart
 * - Type-safe routes: @Serializable data class
 * - Pass categoryId và productId qua navigation
 * - Cart screen: List products + Total
 * - Deep link: "myapp://product/{id}"
 * - NavGraph: authGraph (Login) + mainGraph (Shop)
 * - popUpTo after login (clear login from stack)
 * - Argument passing hoạt động đúng
 */

// TODO: [Session 5] Bài tập 3 - Định nghĩa type-safe routes
// @Serializable object CategoryListRoute
// @Serializable data class ProductListRoute(val category: String)
// @Serializable data class ProductDetailRoute(val productId: Int)
// @Serializable object CartRoute
// @Serializable object LoginRoute

@Composable
fun ECommerceFlowApp() {
    // TODO: [Session 5] Bài tập 3 - Tạo NavController + NavHost
    // NavHost(navController, startDestination = LoginRoute) {
    //     // Auth graph
    //     composable<LoginRoute> {
    //         LoginScreen(onLoginSuccess = {
    //             navController.navigate(CategoryListRoute) {
    //                 popUpTo(LoginRoute) { inclusive = true } // Clear login từ stack
    //             }
    //         })
    //     }
    //
    //     // Main graph
    //     composable<CategoryListRoute> { CategoryScreen(onCategoryClick = { ... }) }
    //     composable<ProductListRoute> { ... }
    //     composable<ProductDetailRoute> { ... }
    //     composable<CartRoute> { CartScreen() }
    // }

    // Placeholder
    Text("Bắt đầu code E-Commerce Flow ở đây!", modifier = Modifier.padding(16.dp))
}

// TODO: [Session 5] Bài tập 3 - Tạo các screens:
// - LoginScreen(onLoginSuccess: () -> Unit)
// - CategoryScreen(onCategoryClick: (String) -> Unit)
// - ProductListScreen(category: String, onProductClick: (Int) -> Unit)
// - ProductDetailScreen(productId: Int, onAddToCart: () -> Unit)
// - CartScreen()

@Preview(showBackground = true)
@Composable
private fun ECommerceFlowAppPreview() {
    AppTheme { ECommerceFlowApp() }
}
