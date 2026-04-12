package com.cattv.app.data

import com.cattv.app.model.Channel

object ChannelRepository {

    // Canales peruanos + internacionales (fuente: iptv-org, streams públicos)
    val channels = listOf(

        // --- PERÚ ---
        Channel(
            id = 1,
            name = "ATV",
            url = "https://d2qsan2ut81n2k.cloudfront.net/live/25046411-8673-4dec-8ae8-5b41984f34e1/ts:abr.m3u8",
            category = "Perú · General",
            logoUrl = "https://i.imgur.com/eUzYZfg.png"
        ),
        Channel(
            id = 2,
            name = "ATV+",
            url = "https://d2qsan2ut81n2k.cloudfront.net/live/77eece7f-8de5-4406-9f7e-7be16d81f2ce/ts:abr.m3u8",
            category = "Perú · Noticias",
            logoUrl = "https://i.imgur.com/fY9256H.png"
        ),
        Channel(
            id = 3,
            name = "Latina",
            url = "https://redirector.rudo.video/hls-video/567ffde3fa319fadf3419efda25619456231dfea/latina/latina.smil/playlist.m3u8",
            category = "Perú · General",
            logoUrl = "https://i.imgur.com/MOsWriY.png"
        ),
        Channel(
            id = 4,
            name = "Latina Clásicos",
            url = "https://redirector.rudo.video/hls-video/plus226/latina2/latina2.smil/playlist.m3u8",
            category = "Perú · Series",
            logoUrl = "https://i.imgur.com/q0dAWyj.png"
        ),
        Channel(
            id = 5,
            name = "Exitosa TV",
            url = "https://luna-4-video.mediaserver.digital/exitosatv_233b-4b49-a726-5a451262/index.m3u8",
            category = "Perú · Noticias",
            logoUrl = "https://i.imgur.com/pu0BSB7.png"
        ),
        Channel(
            id = 6,
            name = "La Tele",
            url = "https://live-evg1.tv360.bitel.com.pe/bitel/latele/playlist.m3u8",
            category = "Perú · Entretenimiento",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/LaTele_Per%C3%BA_2018_Logo.png/960px-LaTele_Per%C3%BA_2018_Logo.png"
        ),
        Channel(
            id = 7,
            name = "L1 Max",
            url = "https://live20.bozztv.com/akamaissh101/ssh101/l1maxhd/playlist.m3u8",
            category = "Perú · Deportes",
            logoUrl = "https://i.imgur.com/2fziU2B.png"
        ),
        Channel(
            id = 8,
            name = "ATV Sur",
            url = "https://alba-pe-atv-atvsur.stream.mediatiquestream.com/index.m3u8",
            category = "Perú · General",
            logoUrl = "https://i.imgur.com/LnyVa5H.png"
        ),
        Channel(
            id = 9,
            name = "Karibena",
            url = "https://live-player.egostreaming.pe/karibenatv_685a-pe-a5676-584412/index.fmp4.m3u8",
            category = "Perú · Música",
            logoUrl = "https://i.imgur.com/cgHjg1e.png"
        ),

        // --- INTERNACIONAL ---
        Channel(
            id = 10,
            name = "DW Español",
            url = "https://dwamdstream104.akamaized.net/hls/live/2015530/dwstream104/index.m3u8",
            category = "Internacional · Noticias",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/75/Deutsche_Welle_symbol_2012.svg/200px-Deutsche_Welle_symbol_2012.svg.png"
        ),
        Channel(
            id = 11,
            name = "France 24 Español",
            url = "https://stream.france24.com/hls/live/2037986/F24_ES_LO_HLS/master.m3u8",
            category = "Internacional · Noticias",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5/France_24_logo.svg/200px-France_24_logo.svg.png"
        ),
        Channel(
            id = 12,
            name = "NASA TV",
            url = "https://ntv1.akamaized.net/hls/live/2014075/NASA-NTV1-HLS/master.m3u8",
            category = "Internacional · Ciencia",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/NASA_logo.svg/200px-NASA_logo.svg.png"
        )
    )

    fun search(query: String): List<Channel> {
        if (query.isBlank()) return channels
        val q = query.lowercase().trim()
        return channels.filter {
            it.name.lowercase().contains(q) || it.category.lowercase().contains(q)
        }
    }
}
