package com.example.fakestoreapp.services

import com.example.fakestoreapp.models.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductsService {

    @GET("products")
    suspend fun getAllProducts() : List<Product>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int) : Product
}