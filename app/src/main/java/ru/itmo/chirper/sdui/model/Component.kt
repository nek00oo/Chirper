package ru.itmo.chirper.sdui.model

import ru.itmo.chirper.sdui.model.position.LayoutParams
import ru.itmo.chirper.sdui.model.styles.CardStyle

sealed class Component {
    data class Avatar(
        val url: String,
        val layout: LayoutParams = LayoutParams()
    ) : Component()

    data class Text(
        val text: String,
        val style: String? = null,
        val layout: LayoutParams = LayoutParams()
    ) : Component()

    data class Card(
        val text: String,
        val layout: LayoutParams = LayoutParams(),
        val style: CardStyle = CardStyle()
    ) : Component()

    data class StatItem(val label: String, val value: String)
    data class Stats(
        val items: List<StatItem>,
        val layout: LayoutParams = LayoutParams()
    ) : Component()
}