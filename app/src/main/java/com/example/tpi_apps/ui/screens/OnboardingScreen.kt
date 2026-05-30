package com.example.tpi_apps.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tpi_apps.R
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pages = listOf(
        OnboardingPage(
            "Descubrí comida\nreal y deliciosa",
            "Explorá reseñas honestas de platos que ya probaron usuarios como vos.",
            R.drawable.onboarding_1
        ),
        OnboardingPage(
            "Compartí tu\nesperiencia",
            "Reseñá tus pedidos, subí fotos y ayudá a otros a elegir mejor.",
            R.drawable.onboarding_2
        ),
        OnboardingPage(
            "Tu perfil,\ntus recompensas",
            "Sumá puntos, subí de rango y desbloqueá beneficios por ser parte activa de la comunidad.",
            R.drawable.onboarding_3
        )
    )

    // pages.size + 1 for Terms and Conditions
    val pagerState = rememberPagerState(pageCount = { pages.size + 1 })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF3A63ED), Color(0xFFFFFFFF))
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.hero_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.2f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                userScrollEnabled = pagerState.currentPage < pages.size // Disable manual swipe on Terms
            ) { position ->
                if (position < pages.size) {
                    OnboardingPageContent(
                        page = pages[position],
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    )
                } else {
                    TermsAndConditionsScreen(onAccept = onFinish)
                }
            }

            // Only show indicators for onboarding pages
            if (pagerState.currentPage < pages.size) {
                Row(
                    Modifier
                        .padding(bottom = 32.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pages.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration) Color(0xFF213474) else Color(0xFF8E9DFF)
                        Box(
                            modifier = Modifier
                                .padding(6.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(12.dp)
                        )
                    }
                }
            }
        }

        if (pagerState.currentPage < pages.size) {
            Text(
                text = "Saltar",
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(24.dp)
                    .align(Alignment.TopEnd)
                    .clickable { 
                        scope.launch {
                            pagerState.animateScrollToPage(pages.size)
                        }
                    },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = page.title,
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 44.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = page.description,
            fontSize = 19.sp,
            color = Color(0xFF213474),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp),
            fontWeight = FontWeight.SemiBold,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun TermsAndConditionsScreen(onAccept: () -> Unit) {
    val scrollState = rememberLazyListState()
    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = scrollState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.last()
                lastVisibleItem.index == layoutInfo.totalItemsCount - 1 &&
                        lastVisibleItem.offset + lastVisibleItem.size <= layoutInfo.viewportEndOffset
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    )


    {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE2E8F0))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEFF6FF)),
                        contentAlignment = Alignment.Center

                    ) {


                        Icon(
                            painter = painterResource(id = R.drawable.tycicon),
                            contentDescription = null,
                            tint = Color(0xFF3B82F6),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Términos y Condiciones",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = "Última actualización: 12 de abril de 2024",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(16.dp))

                // Terms Content
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        TermSection("1. Aceptación de los términos", "Al crear una cuenta o utilizar la aplicación Recomiendo, aceptás estos Términos y Condiciones en su totalidad. Si no estás de acuerdo con alguna parte de estos términos, no debés utilizar la aplicación.\nEl uso continuado de Recomiendo implica la aceptación expresa de cualquier modificación futura que pueda realizarse sobre estos términos.")
                    }
                    item {
                        TermSection("2. Descripción del servicio", "Recomiendo es una plataforma que permite a los usuarios descubrir, reseñar y compartir experiencias relacionadas con restaurantes, comidas y productos gastronómicos.\nLos usuarios pueden:\n• Explorar reseñas publicadas por otros miembros.\n• Publicar reseñas propias.\n• Calificar productos mediante puntuaciones.\n• Compartir fotografías de sus pedidos.\n• Interactuar con contenido generado por otros usuarios.\n• Acumular puntos y progresar dentro del sistema de rangos de la plataforma.\nRecomiendo no garantiza la exactitud absoluta de las opiniones publicadas, ya que las mismas representan experiencias personales de cada usuario.")
                    }
                    item {
                        TermSection("3. Registro y cuentas de usuario", "Para acceder a determinadas funcionalidades es necesario crear una cuenta.\nEl usuario se compromete a:\n• Proporcionar información verídica y actualizada.\n• Mantener la confidencialidad de sus credenciales.\n• No compartir su cuenta con terceros.\n• Notificar cualquier uso no autorizado de la cuenta.\nRecomiendo podrá suspender o cancelar cuentas que incumplan estos términos o realicen actividades sospechosas.")
                    }
                    item {
                        TermSection("4. Contenido generado por los usuarios", "Las reseñas, fotografías y comentarios publicados en Recomiendo son responsabilidad exclusiva de sus autores.\nAl publicar contenido, el usuario declara que:\n• Es propietario del material compartido o posee autorización para utilizarlo.\n• El contenido no infringe derechos de terceros.\n• La información proporcionada es veraz según su experiencia.\nRecomiendo podrá eliminar contenido que considere:\n• Engañoso.\n• Fraudulento.\n• Ofensivo.\n• Discriminatorio.\n• Violento.\n• Irrelevante para la temática de la plataforma.")
                    }
                    item {
                        TermSection("5. Normas de conducta", "Los usuarios se comprometen a utilizar la plataforma de manera respetuosa y responsable.\nNo está permitido:\n• Publicar contenido ofensivo o discriminatorio.\n• Acosar a otros usuarios.\n• Utilizar lenguaje inapropiado.\n• Difundir información falsa de manera intencional.\n• Manipular el sistema de puntuaciones.\n• Crear cuentas falsas.\n• Intentar perjudicar la reputación de negocios o usuarios mediante reseñas fraudulentas.\nLas infracciones podrán derivar en advertencias, suspensiones temporales o bloqueos permanentes.")
                    }
                    item {
                        TermSection("6. Sistema de puntuaciones y rangos", "Recomiendo podrá otorgar puntos a los usuarios por distintas actividades dentro de la plataforma.\nEntre ellas:\n• Publicar reseñas.\n• Subir fotografías.\n• Recibir valoraciones positivas.\n• Participar activamente en la comunidad.\nLos puntos acumulados podrán utilizarse para desbloquear rangos, beneficios o futuras funcionalidades.\nLa empresa se reserva el derecho de modificar los criterios de asignación de puntos en cualquier momento.")
                    }
                    item {
                        TermSection("7. Veracidad de las reseñas", "El objetivo principal de Recomiendo es brindar opiniones auténticas sobre experiencias gastronómicas.\nPor este motivo:\n• Las reseñas deben basarse en experiencias reales.\n• Las fotografías deben corresponder al producto efectivamente recibido.\n• No se permite publicar contenido promocional encubierto.\nLa detección de reseñas falsas podrá derivar en la eliminación del contenido y la suspensión de la cuenta.")
                    }
                    item {
                        TermSection("8. Propiedad intelectual", "Todos los elementos que conforman la plataforma, incluyendo:\n• Nombre de la aplicación.\n• Logotipos.\n• Diseño visual.\n• Interfaces.\n• Textos institucionales.\n• Funcionalidades exclusivas.\nson propiedad de Recomiendo o de sus respectivos titulares.\nQueda prohibida la reproducción, distribución o utilización no autorizada de dichos elementos.")
                    }
                    item {
                        TermSection("9. Licencia sobre el contenido publicado", "Al publicar contenido en Recomiendo, el usuario concede una licencia no exclusiva, gratuita y mundial para que dicho contenido pueda ser mostrado dentro de la plataforma.\nEsta licencia tiene como única finalidad permitir el correcto funcionamiento del servicio.\nEl usuario conserva la titularidad de sus fotografías y reseñas.")
                    }
                    item {
                        TermSection("10. Privacidad y datos personales", "Recomiendo recopila determinados datos necesarios para brindar sus servicios.\nEntre ellos:\n• Nombre de usuario.\n• Dirección de correo electrónico.\n• Información de uso de la aplicación.\n• Fotografías cargadas por el usuario.\nEl tratamiento de estos datos se realizará de acuerdo con la Política de Privacidad vigente.")
                    }
                    item {
                        TermSection("11. Disponibilidad del servicio", "Recomiendo realiza esfuerzos razonables para mantener la aplicación disponible de forma continua.\nSin embargo, no garantiza:\n• Ausencia de interrupciones.\n• Disponibilidad permanente.\n• Funcionamiento libre de errores.\nPodrán producirse tareas de mantenimiento, actualizaciones o interrupciones temporales.")
                    }
                    item {
                        TermSection("12. Limitación de responsabilidad", "Recomiendo actúa únicamente como intermediario entre los usuarios y el contenido publicado.\nPor lo tanto:\n• No garantiza la calidad de productos o servicios reseñados.\n• No se responsabiliza por decisiones tomadas a partir de opiniones publicadas.\n• No responde por daños derivados del uso de la plataforma.\nCada usuario es responsable de evaluar la información consultada.")
                    }
                    item {
                        TermSection("13. Modificaciones de la plataforma", "Recomiendo podrá modificar, agregar o eliminar funcionalidades en cualquier momento.\nAsimismo, podrá actualizar estos Términos y Condiciones cuando resulte necesario.\nLas modificaciones entrarán en vigencia una vez publicadas dentro de la aplicación.")
                    }
                    item {
                        TermSection("14. Suspensión o cancelación de cuentas", "La empresa podrá suspender o cancelar cuentas que:\n• Infrinjan estos términos.\n• Intenten vulnerar la seguridad de la plataforma.\n• Participen en actividades fraudulentas.\n• Generen contenido perjudicial para la comunidad.\nLa decisión podrá tomarse sin previo aviso cuando la gravedad de la situación lo justifique.")
                    }
                    item {
                        TermSection("15. Contacto", "Para consultas relacionadas con estos Términos y Condiciones, podés comunicarte con el equipo de Recomiendo a través de los canales de soporte disponibles dentro de la aplicación.")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onAccept,
                    enabled = isAtBottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6),
                        disabledContainerColor = Color(0xFF94A3B8)
                    )
                ) {
                    Text(
                        text = if (isAtBottom) "Aceptar y Continuar" else "Deslizá hasta el final",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TermSection(title: String, content: String) {
    Column {
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            fontSize = 13.sp,
            color = Color(0xFF64748B),
            lineHeight = 18.sp
        )
    }
}
