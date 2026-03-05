package com.example.myapplication.core.ui.screen.components

import android.icu.text.DecimalFormatSymbols
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.colorBorderFocused
import com.example.myapplication.ui.theme.colorBorderIdle
import com.example.myapplication.ui.theme.colorCurrencyLabel
import com.example.myapplication.ui.theme.colorDigits
import com.example.myapplication.ui.theme.colorHint
import java.util.Locale

@Composable
fun MoneyAmountInput(value: String,
                     onValueChange: (String) -> Unit,
                     modifier: Modifier = Modifier,
                     currencySymbol: String = "₴",
                     hint: String = "0.00",
                     digitFontSize: androidx.compose.ui.unit.TextUnit = 48.sp,
                     borderWidth: Dp = 2.dp,
                     maxDecimalPlaces: Int = 2,) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = if (isFocused) colorBorderFocused else colorBorderIdle
    val bw = if (isFocused) borderWidth else borderWidth * 0.75f

    // Decimal separator for the current locale
    val decimalSeparator = remember {
        DecimalFormatSymbols(Locale.getDefault()).decimalSeparator.toString()
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                // Draw ONLY the bottom border
                val y = size.height
                drawLine(
                    color = borderColor,
                    start = Offset(0f, y),
                    end   = Offset(size.width, y),
                    strokeWidth = bw.toPx()
                )
            }
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ── Currency symbol ───────────────────────────────────────────────────
        Text(
            text  = currencySymbol,
            style = TextStyle(
                fontSize = digitFontSize * 0.55f,
                fontWeight = FontWeight.Light,
                color = if (value.isEmpty()) colorHint else colorCurrencyLabel,
            ),
            modifier = Modifier
                .padding(end = 6.dp)
                .alignByBaseline()
        )

        // ── Amount digits ─────────────────────────────────────────────────────
        BasicTextField(
            value = value,
            onValueChange = { raw ->
                val filtered = filterMoneyInput(raw, decimalSeparator, maxDecimalPlaces)
                onValueChange(filtered)
            },
            modifier           = Modifier
                .weight(1f)
                .alignByBaseline(),
            textStyle = TextStyle(
                fontSize   = digitFontSize,
                fontWeight = FontWeight.SemiBold,
                color      = colorDigits,
                textAlign  = TextAlign.Start,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine         = true,
            cursorBrush        = SolidColor(colorBorderFocused),
            interactionSource  = interactionSource,
            decorationBox      = { innerTextField ->
                Box {
                    // Hint / placeholder
                    if (value.isEmpty()) {
                        Text(
                            text  = hint,
                            style = TextStyle(
                                fontSize   = digitFontSize,
                                fontWeight = FontWeight.SemiBold,
                                color      = colorHint,
                                textAlign  = TextAlign.Start,
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

/**
 * Strips everything that isn't a digit or the locale decimal separator,
 * ensures at most one separator, and caps fractional digits.
 */
private fun filterMoneyInput(
    raw: String,
    decimalSeparator: String,
    maxDecimalPlaces: Int,
): String {
    // Allow only digits and the locale decimal separator
    val allowed = raw.filter { it.isDigit() || it.toString() == decimalSeparator }

    // Keep only the first decimal separator
    val firstSep = allowed.indexOf(decimalSeparator[0])
    val clean = if (firstSep == -1) {
        allowed
    } else {
        allowed.take(firstSep + 1) +
                allowed.substring(firstSep + 1).replace(decimalSeparator, "")
    }

    // Cap decimal places
    return if (maxDecimalPlaces > 0) {
        val sepIdx = clean.indexOf(decimalSeparator[0])
        if (sepIdx != -1 && clean.length - sepIdx - 1 > maxDecimalPlaces) {
            clean.take(sepIdx + 1 + maxDecimalPlaces)
        } else clean
    } else {
        // Integer-only mode: strip everything after separator
        clean.substringBefore(decimalSeparator)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MoneyAmountInputPreview() {
    var amount by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        // UAH – integers only
        MoneyAmountInput(
            value           = amount,
            onValueChange   = { amount = it },
            currencySymbol  = "₴",
            hint            = "0.00",
        )
    }
}