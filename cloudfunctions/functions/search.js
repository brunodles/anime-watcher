const PAGES = [
        // Anime kai
        "animekaionline.com",
        "animeskai.com",
        "animakai.info",
        // Animes Online Br
        "animesonlinebr.com.br",
        //Animes Orion
        "animesorion.site",
        "animesorion.tv",
        "animesorion.video",
        // Anitube brasil
        "animetubebrasil.com",
        // Anitube Br
        "anitubebr.com",
        // Anitube site
        "anitube.site",
        // One Piece Ex
        "onepiece-ex.com.br",
        "one-piece-x.com.br",
        // Anima/tv Curse
        "tvcurse.com",
        "animacurse.moe",
        "animacurse.tv",
        // XVideos
        "xvideos.com"
]
exports.search = (req, res) => {
    var query = req.query.query + " "+ PAGES.map( (p) => "site:"+p)
        .join(" OR ");

    res.redirect("http://google.com/search?q="+encodeURI(query))
};