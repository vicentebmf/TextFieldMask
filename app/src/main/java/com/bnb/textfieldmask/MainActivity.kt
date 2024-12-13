package com.bnb.textfieldmask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.bnb.textfieldmask.ui.theme.TextFieldMaskTheme
import com.santalu.maskara.Mask
import com.santalu.maskara.MaskChangedListener
import com.santalu.maskara.MaskStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TextFieldMaskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FormScreen(modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun FormScreen(modifier: Modifier) {
    var phone by remember {
        mutableStateOf("")
    }
    var cpf by remember {
        mutableStateOf("")
    }
    var cnpj by remember {
        mutableStateOf("")
    }
    Card {
        Column {
            Spacer(modifier = Modifier.padding(top = 32.dp))
            Text(
                text = "MaskStyle: COMPLETABLE",
                modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
            )
            TextField(
                value = phone,
                onValueChange = {
                    // Regex para validar apenas números
                    val regex = "^[0-9]+$".toRegex()
                    if (it.isEmpty() || (it.length < 12 && regex.matches(it))) {
                        phone = it
                    }
                },
                modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                label = {
                    Text(text = "Telefone")
                },
                visualTransformation = PhoneMaskTransformation()
            )
            Text(
                text = "MaskStyle: NORMAL",
                Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
            )
            TextField(
                value = cpf,
                onValueChange = {
                    // Regex para validar apenas números
                    val regex = "^[0-9]+$".toRegex()
                    if (it.isEmpty() || (it.length < 12 && regex.matches(it))) {
                        cpf = it
                    }
                },
                modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = {
                    Text(text = "Cpf")
                },
                visualTransformation = CpfMaskTransformation()
            )
            Text(
                text = "MaskStyle: PERSISTENT",
                modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
            )
            TextField(
                value = cnpj,
                onValueChange = {
                    // Regex para validar apenas números e letras
                    val regex = "^[a-zA-Z0-9]+$".toRegex()
                    if (it.isEmpty() || (it.length < 15 && regex.matches(it))) {
                        cnpj = it
                    }
                },
                modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                label = {
                    Text(text = "Cnpj")
                },
                visualTransformation = CnpjMaskTransformation()
            )
        }
    }

}

//(99) 99999-9999
class PhoneMaskTransformation : VisualTransformation {

    private val mask = Mask(
        value = "(__) _____-____",
        character = '_',
        style = MaskStyle.NORMAL
    )

    override fun filter(text: AnnotatedString): TransformedText {

        val maskOffsetKMapping = MaskOffsetKMapping()
        val dadosMascara = aplicaMascara(
            mask,
            text.text,
            maskOffsetKMapping
        )
        return TransformedText(
            AnnotatedString(dadosMascara.maskedText),
            maskOffsetKMapping
        )
    }
}

//999.999.999-99
class CpfMaskTransformation : VisualTransformation {

    private val mask = Mask(
        value = "___.___.___-__",
        character = '_',
        style = MaskStyle.NORMAL
    )

    override fun filter(text: AnnotatedString): TransformedText {

        val maskOffsetKMapping = MaskOffsetKMapping()
        val dadosMascara = aplicaMascara(
            mask,
            text.text,
            maskOffsetKMapping
        )
        return TransformedText(
            AnnotatedString(dadosMascara.maskedText),
            maskOffsetKMapping
        )
    }
}

//99.999.999/9999-99
class CnpjMaskTransformation : VisualTransformation {

    private val mask = Mask(
        value = "__.___.___/____-__",
        character = '_',
        style = MaskStyle.PERSISTENT
    )

    override fun filter(text: AnnotatedString): TransformedText {

        val maskOffsetKMapping = MaskOffsetKMapping()
        val dadosMascara = aplicaMascara(
            mask,
            text.text,
            maskOffsetKMapping
        )
        return TransformedText(
            AnnotatedString(dadosMascara.maskedText),
            maskOffsetKMapping
        )
    }
}

fun aplicaMascara(
    mask: Mask,
    unMaskedText: String,
    maskOffsetKMapping: MaskOffsetKMapping,
): DadosMascara {

    val characterMask = mask.character.toString()
    val listener = MaskChangedListener(mask)

    listener.onTextChanged(unMaskedText, 0, 1, 0)

    val maskedText = listener.masked
    val array = Array(maskedText.length) { maskedText[it].toString() }
    val indexTransformedText =
        array.indexOfFirst { it == characterMask }.takeIf { it != -1 } ?: array.size
    val indexOriginalText = unMaskedText.length
    val dadosMascara = DadosMascara(listener.masked, indexOriginalText, indexTransformedText)
    maskOffsetKMapping.setIndexMask(indexOriginalText, indexTransformedText)

    return dadosMascara
}

class MaskOffsetKMapping : OffsetMapping {

    private var indexOriginalText = 0
    private var indexTransformedText = 0

    fun setIndexMask(indexOriginalText: Int, indexTransformedText: Int) {
        this.indexOriginalText = indexOriginalText
        this.indexTransformedText = indexTransformedText
    }

    override fun originalToTransformed(offset: Int): Int {
        return indexTransformedText
    }

    override fun transformedToOriginal(offset: Int): Int {
        return indexOriginalText
    }
}

data class DadosMascara(
    val maskedText: String,
    val indexOriginalText: Int,
    val indexTransformedText: Int,
)

