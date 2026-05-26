package com.example.tpi_apps.ui.navigation

import com.example.tpi_apps.R

sealed class BottomNavItem(
    val route: String,
    val icon: Int,
    val label: String
) {
    object Inicio : BottomNavItem(Routes.Inicio.route, R.drawable.inicioicon, "Inicio")
    object Resenia : BottomNavItem(Routes.Resenia.route, R.drawable.reseniaicon, "Reseña")
    object Camara : BottomNavItem(Routes.Camara.route, R.drawable.camaraicon, "Cámara")
    object Explorar : BottomNavItem(Routes.Explorar.route, R.drawable.exploraricon, "Explorar")
    object Perfil : BottomNavItem(Routes.Perfil.route, R.drawable.perfilicon, "Perfil")
}