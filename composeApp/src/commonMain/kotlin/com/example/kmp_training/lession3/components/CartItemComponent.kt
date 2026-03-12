package com.example.kmp_training.lession3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kmp_training.common.BgMuted
import com.example.kmp_training.common.BorderSubtle
import com.example.kmp_training.common.PrimaryGreen
import com.example.kmp_training.common.TextPrimary
import com.example.kmp_training.common.TextSecondary
import com.example.kmp_training.lession3.CartItem
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.outfit_font
import org.jetbrains.compose.resources.Font

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
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = BorderSubtle
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
                    fontFamily = FontFamily(Font(Res.font.outfit_font)),
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "$${cartItem.price}",
                    fontFamily = FontFamily(Font(Res.font.outfit_font)),
                    color = TextSecondary,
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
                    enabled = cartItem.quantity > 0,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = BgMuted,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                }
                Text(
                    text = cartItem.quantity.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(Res.font.outfit_font)),
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .width(35.dp)
                )
                IconButton(
                    onClick = onIncrease,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = PrimaryGreen,
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
