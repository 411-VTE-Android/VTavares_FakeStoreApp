package com.example.fakestoreapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.fakestoreapp.models.Product

@Composable
fun ProductItem(
    product: Product,
    onClick: () -> Unit = { }
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(260.dp) // Altura fija para mantener la cuadrícula simétrica
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp), // Bordes bien redondeados
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Sombra sutil
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Mitad superior: Fondo suave (tipo aparador) con la imagen
            Box(
                modifier = Modifier
                    .weight(1.2f) // Toma un poco más de la mitad del espacio
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F8)), // Un gris/azulado muy claro
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Mitad inferior: Textos (Título y Precio)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween // Empuja el precio hacia abajo
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray,
                    maxLines = 2, // Vital para que no rompa el diseño
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary // Usa el color principal de tu tema
                )
            }
        }
    }
}