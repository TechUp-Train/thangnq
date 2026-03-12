package com.example.kmp_training.lession5

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.kmp_training.common.AppTheme
import com.example.kmp_training.common.Category
import com.example.kmp_training.common.Product
import com.example.kmp_training.common.SampleData
import com.example.kmp_training.lession5.ecommerce.CartScreen
import com.example.kmp_training.lession5.ecommerce.CategoryListScreen
import com.example.kmp_training.lession5.ecommerce.ProductDetailScreen
import com.example.kmp_training.lession5.ecommerce.ProductListScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

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
    val config = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(ProductFlowKey.ProductList::class)
                subclass(ProductFlowKey.ProductDetail::class)
                subclass(ProductFlowKey.CategoryList::class)
                subclass(ProductFlowKey.Cart::class)
            }
        }
    }
    val backStack = rememberNavBackStack(config, ProductFlowKey.CategoryList)
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

                NavigationBackHandler(
                    state = rememberNavigationEventState(NavigationEventInfo.None),
                    isBackEnabled = true,
                    onBackCompleted = {
                        showConfirmDialog = true
                    }
                )

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
    onConfirm: () -> Unit
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