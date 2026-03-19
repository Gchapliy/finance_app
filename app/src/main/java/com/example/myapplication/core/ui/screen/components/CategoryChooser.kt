package com.example.myapplication.core.ui.screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Data ─────────────────────────────────────────────────────────────────────

data class ExpenseCategory(
    val id: String,
    val emoji: String,
    val label: String,
    val usageCount: Int = 0, // higher → shown first in popular chips
)

// Sample predefined categories – replace / extend freely
val defaultCategories: List<ExpenseCategory> = listOf(
    ExpenseCategory("food", "🍔", "Food", usageCount = 42),
    ExpenseCategory("transport", "🚌", "Transport", usageCount = 38),
    ExpenseCategory("groceries", "🛒", "Groceries", usageCount = 35),
    ExpenseCategory("health", "💊", "Health", usageCount = 20),
    ExpenseCategory("coffee", "☕", "Coffee", usageCount = 19),
    ExpenseCategory("rent", "🏠", "Rent", usageCount = 12),
    ExpenseCategory("clothing", "👗", "Clothing", usageCount = 10),
    ExpenseCategory("entertainment", "🎬", "Entertainment", usageCount = 9),
    ExpenseCategory("subscriptions", "📱", "Subscriptions", usageCount = 8),
    ExpenseCategory("gym", "🏋️", "Gym", usageCount = 7),
    ExpenseCategory("utilities", "💡", "Utilities", usageCount = 6),
    ExpenseCategory("travel", "✈️", "Travel", usageCount = 5),
    ExpenseCategory("education", "📚", "Education", usageCount = 4),
    ExpenseCategory("gifts", "🎁", "Gifts", usageCount = 3),
    ExpenseCategory("pets", "🐾", "Pets", usageCount = 2),
)

// ─── Palette ──────────────────────────────────────────────────────────────────

private val BgSurface = Color(0xFFF7F7F8)
private val BgCard = Color(0xFFFFFFFF)
private val AccentInk = Color(0xFF1A1A2E)
private val AccentPrimary = Color(0xFF1A1A2E)
private val ChipSelected = Color(0xFF1A1A2E)
private val ChipUnselected = Color(0xFFF0F0F2)
private val BorderColor = Color(0xFFE2E2E6)
private val HintColor = Color(0xFFAAAAAF)
private val SearchBg = Color(0xFFF0F0F2)

// ─── Main composable ──────────────────────────────────────────────────────────

/**
 * Expandable category chooser widget.
 *
 * Collapsed state  → shows selected category name (or placeholder) + chevron.
 * Expanded state   → popular chips row + search field + "add new" input below.
 *
 * @param categories        Full list of available categories.
 * @param selectedCategory  Currently selected category (null = none).
 * @param onCategorySelected Called when a category is chosen (chip tap, search result tap,
 *                           or new category confirmed).
 * @param onCategoryCreated  Optional: called when user creates a brand-new category.
 */
@Composable
fun CategoryChooser(
    categories: List<ExpenseCategory>,
    selectedCategory: ExpenseCategory?,
    onCategorySelected: (ExpenseCategory) -> Unit,
    onCategoryCreated: ((ExpenseCategory) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var newCategoryText by remember { mutableStateOf("") }
    val searchFocusRequester = remember { FocusRequester() }

    // Popular chips = top-N by usage
    val popularCategories = remember(categories) {
        categories.sortedByDescending { it.usageCount }.take(6)
    }

    // Filtered list for search results
    val searchResults = remember(searchQuery, categories) {
        if (searchQuery.isBlank()) emptyList()
        else categories.filter {
            it.label.contains(searchQuery, ignoreCase = true)
        }
    }

    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "chevron"
    )

    Column(modifier = modifier.fillMaxWidth()) {

        // ── Collapsed / header row ─────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    if (expanded) RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    else RoundedCornerShape(16.dp)
                )
                .background(BgCard)
                .border(
                    width = 1.dp,
                    color = if (expanded) AccentPrimary.copy(alpha = 0.25f) else BorderColor,
                    shape = if (expanded) RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    else RoundedCornerShape(16.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    expanded = !expanded
                    if (!expanded) searchQuery = ""
                }
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedCategory != null) {
                Text(
                    text = selectedCategory.emoji,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text(
                    text = selectedCategory.label,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = AccentInk
                    ),
                    modifier = Modifier.weight(1f)
                )
            } else {
                Text(
                    text = "Select category",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = HintColor
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = HintColor,
                modifier = Modifier
                    .size(22.dp)
                    .rotate(chevronRotation)
            )
        }

        // ── Expanded panel ─────────────────────────────────────────────────
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(
                animationSpec = tween(320, easing = FastOutSlowInEasing)
            ) + fadeIn(tween(200)),
            exit = shrinkVertically(
                animationSpec = tween(240, easing = FastOutSlowInEasing)
            ) + fadeOut(tween(150))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(BgCard)
                    .border(
                        width = 1.dp,
                        color = AccentPrimary.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    )
                    .padding(bottom = 20.dp)
            ) {
                HorizontalDivider(color = BorderColor, thickness = 1.dp)

                Spacer(Modifier.height(16.dp))

                // ── Popular chips ────────────────────────────────────────
                SectionLabel("Most popular", Modifier.padding(horizontal = 20.dp))
                Spacer(Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(popularCategories, key = { it.id }) { cat ->
                        CategoryChip(
                            category = cat,
                            selected = selectedCategory?.id == cat.id,
                            onClick = {
                                onCategorySelected(cat)
                                expanded = false
                                searchQuery = ""
                            }
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // ── Search ───────────────────────────────────────────────
                SectionLabel("Search", Modifier.padding(horizontal = 20.dp))
                Spacer(Modifier.height(10.dp))

                SearchField(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    focusRequester = searchFocusRequester,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                // Search results
                AnimatedVisibility(visible = searchResults.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(BgSurface)
                    ) {
                        searchResults.forEachIndexed { index, cat ->
                            SearchResultRow(
                                category = cat,
                                selected = selectedCategory?.id == cat.id,
                                onClick = {
                                    onCategorySelected(cat)
                                    expanded = false
                                    searchQuery = ""
                                }
                            )
                            if (index < searchResults.lastIndex) {
                                HorizontalDivider(
                                    color = BorderColor,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                            }
                        }
                    }
                }

                // No results hint
                AnimatedVisibility(
                    visible = searchQuery.isNotBlank() && searchResults.isEmpty()
                ) {
                    Text(
                        text = "No match — create it below ↓",
                        style = TextStyle(fontSize = 13.sp, color = HintColor),
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 8.dp)
                    )
                }

                Spacer(Modifier.height(20.dp))
                HorizontalDivider(
                    color = BorderColor,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(20.dp))

                // ── Add new category ─────────────────────────────────────
                SectionLabel("Add new category", Modifier.padding(horizontal = 20.dp))
                Spacer(Modifier.height(10.dp))

                NewCategoryRow(
                    value = newCategoryText,
                    onChange = { newCategoryText = it },
                    onConfirm = {
                        val trimmed = newCategoryText.trim()
                        if (trimmed.isNotEmpty()) {
                            val newCat = ExpenseCategory(
                                id = trimmed.lowercase().replace(" ", "_"),
                                emoji = "🏷️",
                                label = trimmed,
                            )
                            onCategoryCreated?.invoke(newCat)
                            onCategorySelected(newCat)
                            newCategoryText = ""
                            expanded = false
                            searchQuery = ""
                        }
                    },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }
}

// ─── Sub-composables ──────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = TextStyle(
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = HintColor,
            letterSpacing = 1.2.sp,
        ),
        modifier = modifier
    )
}

@Composable
private fun CategoryChip(
    category: ExpenseCategory,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor = if (selected) ChipSelected else ChipUnselected
    val txtColor = if (selected) Color.White else AccentInk

    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(bgColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = category.emoji, fontSize = 15.sp)
        Text(
            text = category.label,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                color = txtColor
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        textStyle = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = AccentInk
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Search
        ),
        singleLine = true,
        cursorBrush = SolidColor(AccentPrimary),
        decorationBox = { inner ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SearchBg)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = HintColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(10.dp))
                Box(Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            "Search categories…",
                            style = TextStyle(fontSize = 15.sp, color = HintColor)
                        )
                    }
                    inner()
                }
                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Clear",
                        tint = HintColor,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onQueryChange("") }
                    )
                }
            }
        }
    )
}

@Composable
private fun SearchResultRow(
    category: ExpenseCategory,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .background(if (selected) AccentPrimary.copy(alpha = 0.06f) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = category.emoji, fontSize = 18.sp)
        Spacer(Modifier.width(12.dp))
        Text(
            text = category.label,
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (selected) AccentPrimary else AccentInk
            ),
            modifier = Modifier.weight(1f)
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(AccentPrimary, CircleShape)
            )
        }
    }
}

@Composable
private fun NewCategoryRow(
    value: String,
    onChange: (String) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier.fillMaxWidth(),
        textStyle = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = AccentInk
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        cursorBrush = SolidColor(AccentPrimary),
        decorationBox = { inner ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SearchBg)
                    .padding(start = 14.dp, top = 4.dp, bottom = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            "Type a new category name…",
                            style = TextStyle(fontSize = 15.sp, color = HintColor)
                        )
                    }
                    inner()
                }
                AnimatedVisibility(
                    visible = value.isNotBlank(),
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AccentPrimary)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onConfirm
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Add category",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun CategoryChooserSkeleton() {
    val brush = shimmerBrush()

    Row() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(brush)
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF7F7F8)
@Composable
private fun CategoryChooserPreview() {
    val categories = remember { mutableStateListOf(*defaultCategories.toTypedArray()) }
    var selected by remember { mutableStateOf<ExpenseCategory?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // Category chooser
        CategoryChooser(
            categories = categories,
            selectedCategory = selected,
            onCategorySelected = { selected = it },
            onCategoryCreated = { categories.add(it) },
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF7F7F8)
@Composable
private fun CategoryChooserSkeletonPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Category chooser
        CategoryChooserSkeleton()
    }
}
