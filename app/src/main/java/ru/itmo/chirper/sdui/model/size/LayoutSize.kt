package ru.itmo.chirper.sdui.model.size

sealed class LayoutSize {
    object MatchParent : LayoutSize()
    object WrapContent: LayoutSize()
    data class Dp(val value: Int): LayoutSize()
}