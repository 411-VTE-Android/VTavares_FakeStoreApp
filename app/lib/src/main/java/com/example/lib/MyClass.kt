package com.example.lib

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    println("=== CORRUTINAS ===")

    //1. runBlocking es una corrutina que BLOQUEA EL HILA PRINCIPAL
    //NO SE DEBE USAR EN PRODUCCIÓN

    corrutinaLaunch()
}

fun corrutinaAsync() {
    runBlocking {
        println("Haciendo petición /GET")
        val result = async {
            println("Haciendo consulta de la API")
            delay(6000)
            """ { "id":1,"name":"Victor" } """
        }
        println("El resultado de la petición es ${result.await()}")
    }
}

fun corrutinaLaunch() {
    runBlocking {
        println("Cargando interfáz gráfica...")
        launch(Dispatchers.IO) {
            consultaAPI()
        }
        println("La UI sigue cargando mientras termina la corrutina")
    }
}


suspend fun consultaAPI() {
    println("Consultando la API")
    delay(6000)
    println("La respuesta es que todo salió bien")
}