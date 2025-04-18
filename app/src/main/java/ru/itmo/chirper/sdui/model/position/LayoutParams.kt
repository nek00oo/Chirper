package ru.itmo.chirper.sdui.model.position

import ru.itmo.chirper.sdui.model.size.LayoutSize

data class LayoutParams(
    val alignment: Alignment = Alignment.START,
    val margin: Margin = Margin(),
    val padding: Padding = Padding(),
    val width: LayoutSize = LayoutSize.MatchParent,
    val height: LayoutSize = LayoutSize.WrapContent
)