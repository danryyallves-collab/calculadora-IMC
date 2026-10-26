package com.example.calculadoraimc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraimc.ui.theme.CalculadoraIMCTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraIMCTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFF2F2F7) // Fundo cinza claro padrão iOS
                ) { innerPadding ->
                    TelaCalculadoraApple(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TelaCalculadoraApple(modifier: Modifier = Modifier) {
    // Valores iniciais iguais ao modelo (você pode alterar livremente no app)
    var pesoTexto by remember { mutableStateOf("72,5") }
    var alturaTexto by remember { mutableStateOf("1,75") }

    var imcValor by remember { mutableStateOf("23.6") }
    var classificacao by remember { mutableStateOf("Peso normal") }
    var mensagem by remember { mutableStateOf("Você está no peso ideal.\nContinue assim!") }
    var corPonto by remember { mutableStateOf(Color(0xFF34C759)) }
    var corFundoBadge by remember { mutableStateOf(Color(0xFFE3F9E5)) }
    var corTextoBadge by remember { mutableStateOf(Color(0xFF28A745)) }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        // Cabeçalho estilo Apple Health
        Text(
            text = "Índice de Massa",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1C1C1E),
            lineHeight = 36.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Corporal (IMC)",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1C1E),
                lineHeight = 36.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            // Ícone do coração em card branco arredondado
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "👨🏻‍⚕️", fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Primeiro Card: Entrada de Dados
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Peso (kg)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1C1E)
                )
                Spacer(modifier = Modifier.height(8.dp))

                CampoTextoApple(
                    valor = pesoTexto,
                    aoMudarValor = { pesoTexto = it },
                    unidade = "kg",
                    placeholder = "0,0"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Altura (metros)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1C1E)
                )
                Spacer(modifier = Modifier.height(8.dp))

                CampoTextoApple(
                    valor = alturaTexto,
                    aoMudarValor = { alturaTexto = it },
                    unidade = "m",
                    placeholder = "0,00"
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Botão Azul iOS (#007AFF) em formato pílula
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        val peso = pesoTexto.replace(",", ".").toDoubleOrNull()
                        val alturaDigitada = alturaTexto.replace(",", ".").toDoubleOrNull()

                        if (peso != null && alturaDigitada != null && alturaDigitada > 0) {
                            val altura = if (alturaDigitada > 3.0) alturaDigitada / 100.0 else alturaDigitada
                            val imc = peso / (altura * altura)
                            imcValor = String.format(Locale.US, "%.1f", imc)

                            when {
                                imc < 18.5 -> {
                                    classificacao = "Abaixo do peso"
                                    mensagem = "Você está abaixo do peso ideal.\nProcure orientação nutricional."
                                    corPonto = Color(0xFF007AFF)
                                    corFundoBadge = Color(0xFFE5F1FF)
                                    corTextoBadge = Color(0xFF0062CC)
                                }
                                imc < 24.9 -> {
                                    classificacao = "Peso normal"
                                    mensagem = "Você está no peso ideal.\nContinue assim!"
                                    corPonto = Color(0xFF34C759)
                                    corFundoBadge = Color(0xFFE3F9E5)
                                    corTextoBadge = Color(0xFF28A745)
                                }
                                imc < 29.9 -> {
                                    classificacao = "Sobrepeso"
                                    mensagem = "Atenção! Você está um pouco\nacima do peso ideal."
                                    corPonto = Color(0xFFFF9500)
                                    corFundoBadge = Color(0xFFFFF3E0)
                                    corTextoBadge = Color(0xFFE65100)
                                }
                                else -> {
                                    classificacao = "Obesidade"
                                    mensagem = "Cuidado com a saúde.\nProcure acompanhamento médico."
                                    corPonto = Color(0xFFFF3B30)
                                    corFundoBadge = Color(0xFFFFEBEE)
                                    corTextoBadge = Color(0xFFD32F2F)
                                }
                            }
                        } else {
                            imcValor = "--"
                            classificacao = "Dados inválidos"
                            mensagem = "Digite valores numéricos válidos\npara peso e altura."
                            corPonto = Color(0xFF8E8E93)
                            corFundoBadge = Color(0xFFF2F2F7)
                            corTextoBadge = Color(0xFF636366)
                        }
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF007AFF),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Calcular IMC",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Segundo Card: Resultado em Destaque
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 28.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = imcValor,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1C1E)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Etiqueta arredondada (Pílula de Status)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(corFundoBadge)
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(corPonto)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = classificacao,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = corTextoBadge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = mensagem,
                    fontSize = 16.sp,
                    color = Color(0xFF1C1C1E),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Composable
fun CampoTextoApple(
    valor: String,
    aoMudarValor: (String) -> Unit,
    unidade: String,
    placeholder: String
) {
    BasicTextField(
        value = valor,
        onValueChange = aoMudarValor,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        textStyle = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF1C1C1E)
        ),
        cursorBrush = SolidColor(Color(0xFF007AFF)),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF2F2F7))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (valor.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 20.sp,
                            color = Color(0xFFC7C7CC)
                        )
                    }
                    innerTextField()
                }
                Text(
                    text = unidade,
                    fontSize = 18.sp,
                    color = Color(0xFF8E8E93)
                )
            }
        }
    )
}