package com.example.myapplication.core.ui.screen.components
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.createRippleModifierNode
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.core.ui.screen.MoneyAmountInput


// ─── Shared palette (keep in sync with MoneyAmountInput / CategoryChooser) ────
private val AccentInk     = Color(0xFF1A1A2E)
private val HintColor     = Color(0xFFAAAAAF)
private val BorderIdle    = Color(0xFFBDBDBD)
private val BorderFocused = Color(0xFF1A1A2E)

// ─── Description field ────────────────────────────────────────────────────────

/**
 * Multi-line description input that shares the same bottom-border-only style
 * as [MoneyAmountInput].
 *
 * @param value         Current text value.
 * @param onValueChange Called on every keystroke.
 * @param hint          Placeholder text.
 * @param maxLines      Max visible lines before it scrolls (default 4).
 */
@Composable
fun DescriptionField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "Description (optional)",
    maxLines: Int = 4,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = if (isFocused) BorderFocused else BorderIdle
    val borderWidth = if (isFocused) 2.dp else 1.dp

    BasicTextField(
        value             = value,
        onValueChange     = onValueChange,
        modifier          = modifier
            .fillMaxWidth()
            .drawBehind {
                val y = size.height
                drawLine(
                    color       = borderColor,
                    start       = Offset(0f, y),
                    end         = Offset(size.width, y),
                    strokeWidth = borderWidth.toPx()
                )
            }
            .padding(bottom = 8.dp),
        textStyle         = TextStyle(
            fontSize   = 16.sp,
            fontWeight = FontWeight.Normal,
            color      = AccentInk,
            lineHeight = 24.sp,
        ),
        keyboardOptions   = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction      = ImeAction.Default,   // allows newlines
        ),
        maxLines          = maxLines,
        cursorBrush       = SolidColor(AccentInk),
        interactionSource = interactionSource,
        decorationBox     = { innerTextField ->
            Box {
                if (value.isEmpty()) {
                    Text(
                        text  = hint,
                        style = TextStyle(
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color      = HintColor,
                            lineHeight = 24.sp,
                        )
                    )
                }
                innerTextField()
            }
        }
    )
}

// ─── Create button ────────────────────────────────────────────────────────────

/**
 * Transparent ghost button with a thin rounded border and a subtle ripple.
 * Fades to 35 % opacity when [enabled] is false.
 *
 * @param text    Label text (default "Create").
 * @param onClick Action callback.
 * @param enabled Whether the button is interactive.
 */
@Composable
fun CreateButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Create",
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val borderAlpha by animateFloatAsState(
        targetValue   = if (enabled) 1f else 0.35f,
        animationSpec = tween(200),
        label         = "borderAlpha"
    )
    val contentColor = AccentInk.copy(alpha = borderAlpha)

    val clickModifier = if (enabled) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication        = ripple(color = AccentInk.copy(alpha = 0.07f)),
            onClick           = onClick
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = 1.dp,
                color = contentColor,
                shape = RoundedCornerShape(14.dp)
            )
            .then(clickModifier)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text  = text,
            style = TextStyle(
                fontSize      = 16.sp,
                fontWeight    = FontWeight.Medium,
                color         = contentColor,
                letterSpacing = 0.5.sp,
            )
        )
    }
}

// ─── Full form assembled ──────────────────────────────────────────────────────

/**
 * Convenience composable that stacks:
 *
 *   MoneyAmountInput
 *   CategoryChooser
 *   DescriptionField
 *   CreateButton
 *
 * Wire each piece to your ViewModel / state holders.
 */
@Composable
fun ExpenseForm(
    amount: String,
    onAmountChange: (String) -> Unit,
    categories: List<ExpenseCategory>,
    selectedCategory: ExpenseCategory?,
    onCategorySelected: (ExpenseCategory) -> Unit,
    onCategoryCreated: (ExpenseCategory) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier,
    createEnabled: Boolean = true,
) {
    Column(
        modifier            = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        MoneyAmountInput(
            value         = amount,
            onValueChange = onAmountChange,
        )

        CategoryChooser(
            categories         = categories,
            selectedCategory   = selectedCategory,
            onCategorySelected = onCategorySelected,
            onCategoryCreated  = onCategoryCreated,
        )

        DescriptionField(
            value         = description,
            onValueChange = onDescriptionChange,
        )

        CreateButton(
            text    = "Create",
            enabled = createEnabled,
            onClick = onCreateClick,
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF7F7F8)
@Composable
private fun ExpenseFormPreview() {
    val cats = remember { mutableStateListOf(*defaultCategories.toTypedArray()) }
    var amount by remember { mutableStateOf("") }
    var cat    by remember { mutableStateOf<ExpenseCategory?>(null) }
    var desc   by remember { mutableStateOf("") }

    Box(Modifier.padding(24.dp)) {
        ExpenseForm(
            amount              = amount,
            onAmountChange      = { amount = it },
            categories          = cats,
            selectedCategory    = cat,
            onCategorySelected  = { cat = it },
            onCategoryCreated   = { cats.add(it) },
            description         = desc,
            onDescriptionChange = { desc = it },
            onCreateClick       = { /* submit to VM */ },
            createEnabled       = amount.isNotBlank() && cat != null,
        )
    }
}
