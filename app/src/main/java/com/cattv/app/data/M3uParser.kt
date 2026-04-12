package com.cattv.app.data

import android.content.Context
import com.cattv.app.model.Channel

object M3uParser {

    private val countryNames = mapOf(
        "us" to "Estados Unidos",
        "ru" to "Rusia",
        "br" to "Brasil",
        "it" to "Italia",
        "de" to "Alemania",
        "fr" to "Francia",
        "uk" to "Reino Unido",
        "ca" to "Canadá",
        "gr" to "Grecia",
        "kr" to "Corea del Sur",
        "vn" to "Vietnam",
        "ua" to "Ucrania",
        "th" to "Tailandia",
        "pl" to "Polonia",
        "jp" to "Japón",
        "rs" to "Serbia",
        "au" to "Australia",
        "tw" to "Taiwán",
        "fi" to "Finlandia",
        "hk" to "Hong Kong",
        "pe" to "Perú",
        "ph" to "Filipinas",
        "sg" to "Singapur",
        "no" to "Noruega",
        "se" to "Suecia",
        "is" to "Islandia",
        "nz" to "Nueva Zelanda",
        "cu" to "Cuba",
        "il" to "Israel",
        "kp" to "Corea del Norte",
        "mo" to "Macao",
        "rw" to "Ruanda"
    )

    private val countryFlags = mapOf(
        "us" to "🇺🇸", "ru" to "🇷🇺", "br" to "🇧🇷", "it" to "🇮🇹",
        "de" to "🇩🇪", "fr" to "🇫🇷", "uk" to "🇬🇧", "ca" to "🇨🇦",
        "gr" to "🇬🇷", "kr" to "🇰🇷", "vn" to "🇻🇳", "ua" to "🇺🇦",
        "th" to "🇹🇭", "pl" to "🇵🇱", "jp" to "🇯🇵", "rs" to "🇷🇸",
        "au" to "🇦🇺", "tw" to "🇹🇼", "fi" to "🇫🇮", "hk" to "🇭🇰",
        "pe" to "🇵🇪", "ph" to "🇵🇭", "sg" to "🇸🇬", "no" to "🇳🇴",
        "se" to "🇸🇪", "is" to "🇮🇸", "nz" to "🇳🇿", "cu" to "🇨🇺",
        "il" to "🇮🇱", "kp" to "🇰🇵", "mo" to "🇲🇴", "rw" to "🇷🇼"
    )

    fun getCountryName(code: String) = countryNames[code.lowercase()] ?: code.uppercase()
    fun getCountryFlag(code: String) = countryFlags[code.lowercase()] ?: "🌐"

    fun parse(context: Context): List<Channel> {
        val channels = mutableListOf<Channel>()
        var id = 0

        try {
            val content = context.assets.open("channels.m3u8")
                .bufferedReader()
                .readText()

            val lines = content.lines()
            var i = 0

            while (i < lines.size) {
                val line = lines[i].trim()

                if (line.startsWith("#EXTINF")) {
                    val name = line.substringAfterLast(",").trim()
                    val logoUrl = Regex("""tvg-logo="([^"]*)"""")
                        .find(line)?.groupValues?.get(1) ?: ""
                    val category = Regex("""group-title="([^"]*)"""")
                        .find(line)?.groupValues?.get(1) ?: "General"
                    val tvgId = Regex("""tvg-id="([^"]*)"""")
                        .find(line)?.groupValues?.get(1) ?: ""

                    // Extraer código de país del tvg-id (ej: "ATV.pe" -> "pe")
                    val countryCode = tvgId.substringAfterLast(".").lowercase()
                        .takeIf { it.length == 2 } ?: "us"

                    // Buscar la URL en la siguiente línea no vacía
                    var j = i + 1
                    while (j < lines.size && lines[j].trim().startsWith("#")) j++

                    if (j < lines.size) {
                        val url = lines[j].trim()
                        if (url.startsWith("http")) {
                            channels.add(
                                Channel(
                                    id = id++,
                                    name = name,
                                    url = url,
                                    category = category.split(";").first(),
                                    logoUrl = logoUrl,
                                    countryCode = countryCode,
                                    countryName = getCountryName(countryCode)
                                )
                            )
                        }
                        i = j + 1
                    } else {
                        i++
                    }
                } else {
                    i++
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return channels
    }
}
