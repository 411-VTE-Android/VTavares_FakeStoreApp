package com.example.fakestoreapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.services.ProductsService
import com.example.fakestoreapp.ui.theme.FakeStoreAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun ProductDetailScreen(id: Int) {
    val BASE_URL = "https://fakestoreapi.com/"
    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        try {
            val retrofitBuilder = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val result = async(Dispatchers.IO) {
                val productService = retrofitBuilder.create(ProductsService::class.java)
                productService.getProductById(id)
            }

            product = result.await()
            isLoading = false
        } catch (e: Exception) {
            Log.e("ProductScreen", e.message.toString())
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.Black)
        }
    } else if (product != null) {
        Scaffold(
            bottomBar = {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 12.dp,
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .navigationBarsPadding(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "PRECIO", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            Text(
                                text = "$${product!!.price}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Button(
                            onClick = { },
                            modifier = Modifier.height(56.dp).width(200.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text("Añadir al carrito", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF5F5F8)) // Fondo gris claro consistente
            ) {
                // BLOQUE SUPERIOR: Imagen Maximizada
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.3f) // Aumentamos el peso para que la imagen sea más grande
                ) {
                    AsyncImage(
                        model = product!!.image,
                        contentDescription = product!!.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp), // Reducimos padding para que la imagen crezca
                        contentScale = ContentScale.Fit
                    )
                }

                // BLOQUE INFERIOR: Información
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.2f),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = product!!.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 36.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // FILA DE CHIPS (Efecto Cristal)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Chip de Categoría
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Gray.copy(alpha = 0.1f),
                                modifier = Modifier.border(1.dp, Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            ) {
                                Text(
                                    text = product!!.category.uppercase(),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray
                                )
                            }

                            // Chip de Rating (Efecto Cristal)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Gray.copy(alpha = 0.1f),
                                modifier = Modifier.border(1.dp, Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = product!!.rating.rate.toString(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Información del Producto",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = product!!.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            textAlign = TextAlign.Justify,
                            lineHeight = 24.sp
                        )
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error al cargar producto", color = Color.Red)
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun ProductDetailScreenPreview(){
    FakeStoreAppTheme {
        ProductDetailScreen(
            id = 1
        )
    }
}