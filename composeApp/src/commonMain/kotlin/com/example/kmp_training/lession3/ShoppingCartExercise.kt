package com.example.kmp_training.lession3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kmp_training.common.AppTheme
import com.example.kmp_training.common.BgPage
import com.example.kmp_training.common.PrimaryBlue
import com.example.kmp_training.common.SampleData
import com.example.kmp_training.common.TextPrimary
import com.example.kmp_training.lession3.components.CartItemComponent
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.outfit_font
import org.jetbrains.compose.resources.Font


/**
 * ⭐⭐ BÀI TẬP 2: Shopping Cart (Medium)
 *
 * Yêu cầu:
 * - LazyColumn: 5 products (Name + Price + Quantity selector [- count +])
 * - Bottom bar: "Total: $XXX" (derivedStateOf tính tổng)
 * - State hoisting: CartScreen(items, onQuantityChange)
 * - Total tự update khi thay đổi quantity
 * - Quantity không < 0
 * - Stateless product item component
 */

data class CartItem(
    val productId: Int,
    val name: String,
    val price: Double,
    val quantity: Int = 0
)

@Composable
fun ShoppingCartScreen() {
    val products = SampleData.products.take(5)

    val cartItems = rememberSaveable() {
        mutableStateListOf<CartItem>().apply {
            addAll(products.map {
                CartItem(
                    productId = it.id,
                    name = it.name,
                    price = it.price,
                    quantity = 0
                )
            })
        }
    }

    val total by remember {
        derivedStateOf { cartItems.sumOf { it.price * it.quantity } }
    }

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(total = total) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = BgPage)
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
            ) {
                itemsIndexed(cartItems) { index, item ->
                    CartItemComponent(
                        cartItem = item,
                        onIncrease = {
                            cartItems[index] = item.copy(quantity = item.quantity + 1)
                        },
                        onDecrease = {
                            if (item.quantity > 0) {
                                cartItems[index] = item.copy(quantity = item.quantity - 1)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BgPage)
            .padding(10.dp)
            .statusBarsPadding()
    ) {
        Icon(
            imageVector = Icons.Default.CardGiftcard,
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(24.dp)
        )
        Text(
            text = "Shopping Cart",
            fontFamily = FontFamily(Font(Res.font.outfit_font)),
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp
        )
    }
}

@Composable
private fun BottomBar(total: Double, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 10.dp),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    fontFamily = FontFamily(Font(Res.font.outfit_font)),
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    text = "$total",
                    fontFamily = FontFamily(Font(Res.font.outfit_font)),
                    color = PrimaryBlue,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp
                )
            }

            Button(
                onClick = {},
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Checkout",
                        fontFamily = FontFamily(Font(Res.font.outfit_font)),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        null,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShoppingCartScreenPreview() {
    AppTheme { ShoppingCartScreen() }
}