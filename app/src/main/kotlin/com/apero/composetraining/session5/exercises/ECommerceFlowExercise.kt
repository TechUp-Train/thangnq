package com.apero.composetraining.session5.exercises

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.Category
import com.apero.composetraining.common.Product
import com.apero.composetraining.common.SampleData
import com.apero.composetraining.session5.exercises.ecommerce.CartScreen
import com.apero.composetraining.session5.exercises.ecommerce.CategoryListScreen
import com.apero.composetraining.session5.exercises.ecommerce.ProductDetailScreen
import com.apero.composetraining.session5.exercises.ecommerce.ProductListScreen
import kotlinx.serialization.Serializable

/**
 * ⭐⭐⭐ BÀI TẬP 3: E-Commerce Flow (Challenge — 90 phút)
 */

@Serializable
sealed class ProductFlowKey : NavKey {
    @Serializable
    data object CategoryList : ProductFlowKey()

    @Serializable
    data class ProductList(val categoryId: String) : ProductFlowKey()

    @Serializable
    data class ProductDetail(val productId: Int) : ProductFlowKey()

    @Serializable
    data object Cart : ProductFlowKey()
}

public val sampleCategories = listOf(
    Category(id = "1"),
    Category(id = "2"),
    Category(id = "3"),
    Category(id = "4"),
    Category(id = "5")
)
public val sampleProducts = SampleData.products

@Composable
fun ECommerceApp() {
    val backStack = rememberNavBackStack(ProductFlowKey.CategoryList)
    val cartItems = remember { mutableStateListOf<Product>() }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            entry<ProductFlowKey.CategoryList> {
                CategoryListScreen(
                    categories = sampleCategories,
                    onCategoryClick = { categoryId ->
                        backStack.add(ProductFlowKey.ProductList(categoryId))
                    },
                    cartCount = cartItems.size,
                    onCartClick = {
                        backStack.add(ProductFlowKey.Cart)
                    }
                )
            }

            entry<ProductFlowKey.ProductList> { key ->
                val products = sampleProducts.filter { it.categoryId == key.categoryId }
                val category = sampleCategories.find { it.id == key.categoryId }

                ProductListScreen(
                    categoryName = category?.name ?: "",
                    products = products,
                    onProductClick = { productId ->
                        backStack.add(ProductFlowKey.ProductDetail(productId))
                    },
                    cartCount = cartItems.size,
                    onCartClick = {
                        backStack.add(ProductFlowKey.Cart)
                    },
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }

            entry<ProductFlowKey.ProductDetail> { key ->
                val product = sampleProducts.find { it.id == key.productId }

                if (product != null) {
                    ProductDetailScreen(
                        product = product,
                        onAddToCart = { cartItems.add(product) },
                        onViewCart = { backStack.add(ProductFlowKey.Cart) },
                        onBack = {
                            if (backStack.size > 1) {
                                backStack.removeLastOrNull()
                            }
                        },
                        cartCount = cartItems.size
                    )
                }
            }

            entry<ProductFlowKey.Cart> {
                var showConfirmDialog by remember { mutableStateOf(false) }

                BackHandler {
                    showConfirmDialog = true
                }

                CartScreen(
                    items = cartItems,
                    onBack = { showConfirmDialog = true },
                    onClearCart = {
                        cartItems.clear()
                    }
                )

                if (showConfirmDialog) {
                    AlertWhenBackComponent(
                        onDismiss = { showConfirmDialog = false },
                        onConfirm = {
                            showConfirmDialog = false
                            if (backStack.size > 1) {
                                backStack.removeLastOrNull()
                            }
                        }
                    )
                }
            }
        }
    )
}
@Composable
fun AlertWhenBackComponent(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Bỏ giỏ hàng?") },
        text = { Text("Bạn có chắc muốn rời khỏi giỏ hàng không?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Đồng ý")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Ở lại")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ECommerceAppPreview() {
    AppTheme { ECommerceApp() }
}