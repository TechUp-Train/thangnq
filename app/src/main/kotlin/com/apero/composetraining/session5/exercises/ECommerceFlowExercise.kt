package com.apero.composetraining.session5.exercises

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme
// TODO: Thêm imports Nav3
// import androidx.navigation3.runtime.NavKey
// import androidx.navigation3.runtime.entryProvider
// import androidx.navigation3.runtime.rememberNavBackStack
// import androidx.navigation3.ui.NavDisplay
// import kotlinx.serialization.Serializable

/**
 * ⭐⭐⭐ BÀI TẬP 3: E-Commerce Flow (Challenge — 90 phút)
 *
 * Yêu cầu:
 * - Flow: CategoryList → ProductList(categoryId) → ProductDetail(productId) → Cart
 * - Dùng sealed class ProductFlowKey : NavKey để nhóm tất cả keys
 * - "Add to Cart" không navigate — chỉ update cartCount (Int state)
 * - CartKey hiện danh sách items + tổng giá
 * - BackHandler trên Cart: hiện confirm dialog "Bỏ giỏ hàng?" trước khi back
 * - Badge trên cart icon hiện số lượng items
 *
 * Sealed class gợi ý:
 * ```kotlin
 * @Serializable
 * sealed class ProductFlowKey : NavKey {
 *     @Serializable data object CategoryList : ProductFlowKey()
 *     @Serializable data class ProductList(val categoryId: Int) : ProductFlowKey()
 *     @Serializable data class ProductDetail(val productId: Int, val categoryId: Int) : ProductFlowKey()
 *     @Serializable data object Cart : ProductFlowKey()
 * }
 * ```
 *
 * Back stack dùng <Any> vì NavDisplay yêu cầu List<Any>:
 * ```kotlin
 * val backStack = rememberNavBackStack<Any>(ProductFlowKey.CategoryList)
 * ```
 *
 * BackHandler pattern:
 * ```kotlin
 * entry<ProductFlowKey.Cart> {
 *     var showConfirmDialog by remember { mutableStateOf(false) }
 *
 *     BackHandler { showConfirmDialog = true }
 *
 *     CartScreen(onBack = { showConfirmDialog = true })
 *
 *     if (showConfirmDialog) {
 *         AlertDialog(
 *             onDismissRequest = { showConfirmDialog = false },
 *             title = { Text("Bỏ giỏ hàng?") },
 *             text = { Text("Các sản phẩm trong giỏ sẽ bị xóa") },
 *             confirmButton = {
 *                 TextButton(onClick = {
 *                     showConfirmDialog = false
 *                     backStack.removeLastOrNull()
 *                 }) { Text("Đồng ý") }
 *             },
 *             dismissButton = {
 *                 TextButton(onClick = { showConfirmDialog = false }) { Text("Ở lại") }
 *             }
 *         )
 *     }
 * }
 * ```
 *
 * Tiêu chí nghiệm thu:
 * - Sealed class đúng (CategoryList, ProductList, ProductDetail, Cart)
 * - cartCount tăng khi "Add to Cart", hiện trên Badge
 * - BackHandler trên Cart hiện dialog trước khi back
 * - Data class keys pass data đúng (categoryId, productId)
 */

// TODO: [Session 5] Bài tập 3 - Định nghĩa sealed class ProductFlowKey : NavKey
// @Serializable
// sealed class ProductFlowKey : NavKey { ... }

// TODO: [Session 5] Bài tập 3 - Sample data
// data class Product(val id: Int, val name: String, val price: Int, val categoryId: Int)
// data class Category(val id: Int, val name: String)
// val sampleCategories = listOf(...)
// val sampleProducts = listOf(...)

// TODO: [Session 5] Bài tập 3 - Implement ECommerceApp
@Composable
fun ECommerceApp() {
    // TODO: Back stack bắt đầu từ CategoryList
    // val backStack = rememberNavBackStack<Any>(ProductFlowKey.CategoryList)

    // TODO: Cart state (danh sách products đã add)
    // val cartItems = remember { mutableStateListOf<Product>() }

    // TODO: NavDisplay với entryProvider cho tất cả screens
    // NavDisplay(
    //     backStack = backStack,
    //     onBack = { backStack.removeLastOrNull() },
    //     entryProvider = entryProvider {
    //         entry<ProductFlowKey.CategoryList> { ... }
    //         entry<ProductFlowKey.ProductList> { key -> ... }
    //         entry<ProductFlowKey.ProductDetail> { key -> ... }
    //         entry<ProductFlowKey.Cart> { ... } // BackHandler ở đây
    //     }
    // )

    // Placeholder
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("TODO: Implement E-Commerce Flow với Navigation 3")
    }
}

// TODO: Implement các screen composables
// CategoryListScreen(categories, onCategoryClick, cartCount)
// ProductListScreen(products, onProductClick, cartCount)
// ProductDetailScreen(product, onAddToCart, onViewCart, onBack)
// CartScreen(items, onBack)

@Preview(showBackground = true)
@Composable
private fun ECommerceAppPreview() {
    AppTheme { ECommerceApp() }
}
