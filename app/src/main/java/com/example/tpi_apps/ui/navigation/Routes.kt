package com.example.tpi_apps.ui.navigation

sealed class Routes(val route: String) {
    object Inicio : Routes("inicio")
    object Resenia : Routes("resenia")
    object Camara : Routes("camara")
    object Explorar : Routes("explorar")
    object Perfil : Routes("perfil")
    object BrandItems : Routes("brand_items/{brandName}") {
        fun createRoute(brandName: String) = "brand_items/$brandName"
    }
    object Confirmacion : Routes("confirmacion")
}