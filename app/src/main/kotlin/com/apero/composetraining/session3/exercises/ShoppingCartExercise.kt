package com.apero.composetraining.session3.exercises

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.R
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.SampleData

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

    // TODO: [Session 3] Bài tập 2 - Tạo state cho cart items
    // val cartItems = remember {
    //     mutableStateListOf(*products.map { CartItem(it.id, it.name, it.price, 0) }.toTypedArray())
    // }
    val cartItems = remember {
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

    // TODO: [Session 3] Bài tập 2 - Tính total bằng derivedStateOf
    // val total by remember { derivedStateOf { cartItems.sumOf { it.price * it.quantity } } }
    val total by remember {
        derivedStateOf { cartItems.sumOf { it.price * it.quantity } }
    }
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(R.color.bg_page))
                    .padding(10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cart),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(24.dp)
                )
                Text(
                    text = "Shopping Cart",
                    fontFamily = FontFamily(Font(R.font.outfit_font)),
                    color = colorResource(R.color.text_primary),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp
                )
            }
        },
        bottomBar = {
            // TODO: [Session 3] Bài tập 2 - Bottom bar hiển thị "Total: $XXX"
            // Surface với tonalElevation, Row chứa Text "Total" + Text "$total"
            Surface(
                modifier = Modifier
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
                            fontFamily = FontFamily(Font(R.font.outfit_font)),
                            color = colorResource(R.color.text_primary),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "$${String.format("%.2f", total)}",
                            fontFamily = FontFamily(Font(R.font.outfit_font)),
                            color = colorResource(R.color.primary_blue),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp
                        )
                    }

                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.primary_blue)
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
                                fontFamily = FontFamily(Font(R.font.outfit_font)),
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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(color = colorResource(R.color.bg_page))
        ) {
            // TODO: [Session 3] Bài tập 2 - LazyColumn hiển thị cart items
            // Mỗi item: CartItemRow(item, onIncrease, onDecrease)
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

// TODO: [Session 3] Bài tập 2 - Tạo CartItemRow composable (STATELESS)
// Params: cartItem: CartItem, onIncrease: () -> Unit, onDecrease: () -> Unit
// Layout: Row chứa Column(name, price) + Row(Button "-" + Text quantity + Button "+")
@Composable
fun CartItemComponent(
    cartItem: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp, vertical = 5.dp),
        color = colorResource(R.color.bg_card),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(R.color.border_subtle)
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.name,
                    fontFamily = FontFamily(Font(R.font.outfit_font)),
                    color = colorResource(R.color.text_primary),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "$${cartItem.price}",
                    fontFamily = FontFamily(Font(R.font.outfit_font)),
                    color = colorResource(R.color.text_secondary),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(20.dp)
            ) {
                IconButton(
                    onClick = onDecrease,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = colorResource(R.color.bg_muted),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = null,
                        tint = colorResource(R.color.text_secondary)
                    )
                }
                Text(
                    text = cartItem.quantity.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.outfit_font)),
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .width(35.dp)
                )
                IconButton(
                    onClick = onIncrease,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = colorResource(R.color.primary_green),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White
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

@Preview
@Composable
private fun CartItemPreview() {
    val cartItem = CartItem(
        productId = 1,
        name = "Hoa 8/3",
        price = 100000.0,
        quantity = 100
    )
    CartItemComponent(
        cartItem = cartItem,
        onIncrease = {},
        onDecrease = {},
        modifier = Modifier.background(Color.White)
    )
}
