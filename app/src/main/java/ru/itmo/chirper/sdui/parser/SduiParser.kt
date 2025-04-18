package ru.itmo.chirper.sdui.parser

import org.json.JSONArray
import org.json.JSONObject
import ru.itmo.chirper.sdui.model.*
import ru.itmo.chirper.sdui.model.position.Alignment
import ru.itmo.chirper.sdui.model.position.LayoutParams
import ru.itmo.chirper.sdui.model.position.Margin
import ru.itmo.chirper.sdui.model.position.Padding
import ru.itmo.chirper.sdui.model.size.LayoutSize
import ru.itmo.chirper.sdui.model.styles.CardStyle

fun parseScreen(jsonStr: String): Screen {
    val root   = JSONObject(jsonStr).getJSONObject("screen")
    val title  = root.getString("title")
    val comps  = mutableListOf<Component>()

    root.getJSONArray("components").forEach { _, elem ->
        val obj    = elem as JSONObject
        val layout = parseLayout(obj.optJSONObject("layout"))
        when (obj.getString("type")) {
            "avatar" -> comps += Component.Avatar(
                url    = obj.getString("url"),
                layout = layout
            )

            "text" -> comps += Component.Text(
                text   = obj.getString("text"),
                style  = obj.optString("style", null),
                layout = layout
            )

            "stats" -> {
                val items = obj.getJSONArray("items").mapIndexed { _, item ->
                    val o = item as JSONObject
                    Component.StatItem(
                        label = o.getString("label"),
                        value = o.getString("value")
                    )
                }
                comps += Component.Stats(
                    items  = items,
                    layout = layout
                )
            }

            "card" -> comps += Component.Card(
                text   = obj.getString("text"),
                layout = layout,
                style  = parseCardStyle(obj.optJSONObject("style"))
            )
        }
    }

    return Screen(title, comps)
}



private fun parseLayout(obj: JSONObject?): LayoutParams {
    if (obj == null) return LayoutParams()

    val alignment = obj.optString("alignment", "start").uppercase().let {
        runCatching { Alignment.valueOf(it) }.getOrDefault(Alignment.START)
    }

    val m = obj.optJSONObject("margin")
    val margin = if (m != null) Margin(
        top    = if (m.has("top"))    m.getInt("top")    else null,
        bottom = if (m.has("bottom")) m.getInt("bottom") else null,
        start  = if (m.has("start"))  m.getInt("start")  else null,
        end    = if (m.has("end"))    m.getInt("end")    else null
    ) else Margin()

    val p = obj.optJSONObject("padding")
    val padding = if (p != null) Padding(
        top    = if (p.has("top"))    p.getInt("top")    else null,
        bottom = if (p.has("bottom")) p.getInt("bottom") else null,
        start  = if (p.has("start"))  p.getInt("start")  else null,
        end    = if (p.has("end"))    p.getInt("end")    else null,
        all    = if (p.has("all"))    p.getInt("all")    else null
    ) else Padding()

    val width = when (val w = obj.optString("width", "match_parent")) {
        "match_parent" -> LayoutSize.MatchParent
        "wrap_content" -> LayoutSize.WrapContent
        else -> {
            w.toIntOrNull()?.let { LayoutSize.Dp(it) } ?: LayoutSize.MatchParent
        }
    }

    val height = when (val h = obj.optString("height", "wrap_content")) {
        "match_parent" -> LayoutSize.MatchParent
        "wrap_content" -> LayoutSize.WrapContent
        else -> {
            h.toIntOrNull()?.let { LayoutSize.Dp(it) } ?: LayoutSize.WrapContent
        }
    }

    return LayoutParams(
        alignment = alignment,
        margin    = margin,
        padding   = padding,
        width     = width,
        height    = height
    )
}

private fun parseCardStyle(obj: JSONObject?): CardStyle {
    if (obj == null) return CardStyle()
    return CardStyle(
        backgroundColor = obj.optString("backgroundColor", null),
        cornerRadius    = if (obj.has("cornerRadius")) obj.getInt("cornerRadius") else null
    )
}


private fun JSONArray.forEach(action: (Int, Any) -> Unit) {
    for (i in 0 until length()) action(i, get(i))
}

private fun <T> JSONArray.mapIndexed(transform: (Int, Any) -> T): List<T> {
    val list = mutableListOf<T>()
    for (i in 0 until length()) {
        list += transform(i, get(i))
    }
    return list
}
