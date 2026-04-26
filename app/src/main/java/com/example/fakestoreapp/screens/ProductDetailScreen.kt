package com.example.fakestoreapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.services.ProductsService
import com.example.fakestoreapp.ui.theme.FakeStoreAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun ProductDetailScreen(id:Int)
{

    val BASE_URL = "https://fakestoreapi.com/"

    var product by remember{
        mutableStateOf<Product?>(null)
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = true) {
        try {
            //1. Instancia en retroFit
            val retrofitBuilder = Retrofit.Builder() //Patrón de diseño builder
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            //2. Ejecutar la petición HTTP
            val result = async(Dispatchers.IO) {
                val productService = retrofitBuilder.create(ProductsService::class.java)
                productService.getProductById(id)
            }

            //3. Manejar la respuesta
            Log.i("ProductsScreen", result.await().toString())
            product = result.await()
            isLoading = false
        }
        catch (e: Exception) {
            Log.e("ProductScreen", e.message.toString())
            isLoading = false

        }
    }

    if(product != null){

        //CONTENIDO DE PRODUCT_DETAIL
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            AsyncImage(
                model = product!!.image,
                contentDescription = product!!.title,
                modifier = Modifier
                    .size(150.dp)
            )
            Text(text = product!!.title)
        }
    } else if( product == null && isLoading){
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else{
        Column() {
            Text("Error al cargar producto")
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