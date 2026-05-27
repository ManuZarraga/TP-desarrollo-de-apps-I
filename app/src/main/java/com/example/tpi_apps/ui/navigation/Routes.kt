package com.example.tpi_apps.ui.navigation

sealed class Routes(val route: String) {
    object Onboarding : Routes("onboarding")
    object Inicio : Routes("inicio")
    object Resenia : Routes("resenia")
    object Camara : Routes("camara")
    object Explorar : Routes("explorar")
    object Perfil : Routes("perfil")
    object BrandItems : Routes("brand_items/{brandName}") {
        fun createRoute(brandName: String) = "brand_items/$brandName"
    }
    object Confirmacion : Routes("confirmacion")
    object ReseniaList : Routes("resenia_list/{brandName}/{itemName}") {
        fun createRoute(brandName: String, itemName: String) = "resenia_list/$brandName/$itemName"
    }
    object ReseniaSpecific : Routes("resenia_specific/{reviewId}") {
        fun createRoute(reviewId: String) = "resenia_specific/$reviewId"
    }
}