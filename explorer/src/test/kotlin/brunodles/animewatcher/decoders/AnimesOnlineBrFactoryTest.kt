package brunodles.animewatcher.decoders

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import brunodles.animewatcher.testhelper.FactoryChecker.whenCheckIsEpisode
import brunodles.animewatcher.testhelper.FactoryChecker.whenEpisode
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class AnimesOnlineBrFactoryTest {

    companion object {

        val VALID_URLS = arrayOf(
            "https://www.animesonlinebr.com.br/video/50034",
            "https://www.animesonlinebr.com.br/desenho/1909"
        )
        val INVALID_URLS = emptyArray<String>()
        val ESPISODE_AND_SINGLE = Episode(
            number = 1,
            description = "Episódio 01 – Eu sou Uzumaki Boruto!! online, Boruto - Episódio 01 – Eu sou Uzumaki Boruto!! Online",
            animeName = "Boruto",
            image = null,
            link = "https://www.animesonlinebr.com.br/video/50034",
            video = "https://www.blogger.com/video-play.mp4?contentId=3dbfcd746b2b8f21",
            nextEpisodes = arrayListOf(
                Episode(
                    animeName = "Boruto",
                    description = "Boruto -  Episódio 02 – O Filho do Hokage!!",
                    link = "https://animesonlinebr.com.br/video/50035",
                    number = 2
                )
            )
        )
        val ABOUT_PAGE = Episode(
            number = 1,
            description = "Hora de Aventura Dublado – Episódio 01 – Hora De Negócios online, Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 01 – Hora De Negócios Online",
            animeName = "Adventure Time (Hora de Aventura)",
            link = "https://www.animesonlinebr.com.br/desenho/1909",
            video = "https://www.blogger.com/video-play.mp4?contentId=https://www.blogger.com/video-play.mp4?contentId=29c01408ea2c7bd",
            image = null,
            nextEpisodes = arrayListOf(
                Episode(
                    number = 2,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 02 – Despejado",
                    link = "https://www.animesonlinebr.com.br/video/50690"
                ),
                Episode(
                    number = 3,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 03 – Pânico Na Festa Do Pijama",
                    link = "https://www.animesonlinebr.com.br/video/50691"
                ),
                Episode(
                    number = 4,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 04 – Problemas Na Terra Do Caroço",
                    link = "https://www.animesonlinebr.com.br/video/50692"
                ),
                Episode(
                    number = 5,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 05 – Prisioneiras Do Amor",
                    link = "https://www.animesonlinebr.com.br/video/50693"
                ),
                Episode(
                    number = 6,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 06 – Dona Tromba",
                    link = "https://www.animesonlinebr.com.br/video/50694"
                ),
                Episode(
                    number = 7,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 07 – O Enquiridio",
                    link = "https://www.animesonlinebr.com.br/video/50695"
                ),
                Episode(
                    number = 8,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 08 – Zig-Zag",
                    link = "https://www.animesonlinebr.com.br/video/50696"
                ),
                Episode(
                    number = 9,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 09 – Ricardio, O Coração",
                    link = "https://www.animesonlinebr.com.br/video/50697"
                ),
                Episode(
                    number = 10,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 10 – Minhas Duas Pessoas Prediletas",
                    link = "https://www.animesonlinebr.com.br/video/50698"
                ),
                Episode(
                    number = 11,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 11 – Lembranças Da Montanha Boom-Boom",
                    link = "https://www.animesonlinebr.com.br/video/50699"
                ),
                Episode(
                    number = 12,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 12 – Bruxo",
                    link = "https://www.animesonlinebr.com.br/video/50700"
                ),
                Episode(
                    number = 13,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 13 – Cidade Dos Ladrões",
                    link = "https://www.animesonlinebr.com.br/video/50701"
                ),
                Episode(
                    number = 14,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 14 – O Jardim Da Bruxa",
                    link = "https://www.animesonlinebr.com.br/video/50702"
                ),
                Episode(
                    number = 15,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 15 – O Que é A Vida?",
                    link = "https://www.animesonlinebr.com.br/video/50703"
                ),
                Episode(
                    number = 16,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 16 – Oceanos De Medo",
                    link = "https://www.animesonlinebr.com.br/video/50704"
                ),
                Episode(
                    number = 17,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 17 – Quando O Sinos De Casamento Derretem",
                    link = "https://www.animesonlinebr.com.br/video/50705"
                ),
                Episode(
                    number = 18,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 18 – Masmorra",
                    link = "https://www.animesonlinebr.com.br/video/50706"
                ),
                Episode(
                    number = 19,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 19 – O Duke",
                    link = "https://www.animesonlinebr.com.br/video/50707"
                ),
                Episode(
                    number = 20,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 20 – Cidade Das Aberrações",
                    link = "https://www.animesonlinebr.com.br/video/50708"
                ),
                Episode(
                    number = 21,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 21 – Donny",
                    link = "https://www.animesonlinebr.com.br/video/50709"
                ),
                Episode(
                    number = 22,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 22 – Criado",
                    link = "https://www.animesonlinebr.com.br/video/50710"
                ),
                Episode(
                    number = 23,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 23 – Sonho De Dia Chuvoso",
                    link = "https://www.animesonlinebr.com.br/video/50711"
                ),
                Episode(
                    number = 24,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 24 – O Que Vocês Fizeram?",
                    link = "https://www.animesonlinebr.com.br/video/50712"
                ),
                Episode(
                    number = 25,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 25 – O Herói Dele!",
                    link = "https://www.animesonlinebr.com.br/video/50713"
                ),
                Episode(
                    number = 26,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura Dublado – Episódio 26 – O Mói Tripa",
                    link = "https://www.animesonlinebr.com.br/video/50714"
                ),
                Episode(
                    number = 1,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 01 – Veio Da Noitosfera",
                    link = "https://www.animesonlinebr.com.br/video/50715"
                ),
                Episode(
                    number = 2,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 02 – Os Olhos",
                    link = "https://www.animesonlinebr.com.br/video/50716"
                ),
                Episode(
                    number = 3,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 03 – Lealdade Ao Rei",
                    link = "https://www.animesonlinebr.com.br/video/50717"
                ),
                Episode(
                    number = 4,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 04 – Sangue Sob A Pele",
                    link = "https://www.animesonlinebr.com.br/video/50718"
                ),
                Episode(
                    number = 5,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 05 – Contando Histórias",
                    link = "https://www.animesonlinebr.com.br/video/50719"
                ),
                Episode(
                    number = 6,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 06 – Amor Lento",
                    link = "https://www.animesonlinebr.com.br/video/50720"
                ),
                Episode(
                    number = 7,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 07 – Incansável",
                    link = "https://www.animesonlinebr.com.br/video/50721"
                ),
                Episode(
                    number = 8,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 08 – Cristais Tem Poder",
                    link = "https://www.animesonlinebr.com.br/video/50722"
                ),
                Episode(
                    number = 10,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 10 – Para Cortar Os Cabelos De Uma Mulher",
                    link = "https://www.animesonlinebr.com.br/video/50723"
                ),
                Episode(
                    number = 11,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 11 – A Câmara Das Lâminas Congeladas",
                    link = "https://www.animesonlinebr.com.br/video/50724"
                ),
                Episode(
                    number = 12,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 12 – Os Pais Dela",
                    link = "https://www.animesonlinebr.com.br/video/50725"
                ),
                Episode(
                    number = 13,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 13 – Os Feijões",
                    link = "https://www.animesonlinebr.com.br/video/50726"
                ),
                Episode(
                    number = 14,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 14 – O Rei Silêncioso",
                    link = "https://www.animesonlinebr.com.br/video/50727"
                ),
                Episode(
                    number = 15,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 15 – Você De Verdade",
                    link = "https://www.animesonlinebr.com.br/video/50728"
                ),
                Episode(
                    number = 16,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 16 – Uardiões Do Brilho Do Sol",
                    link = "https://www.animesonlinebr.com.br/video/50729"
                ),
                Episode(
                    number = 17,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 17 – Morte Em Botão",
                    link = "https://www.animesonlinebr.com.br/video/50730"
                ),
                Episode(
                    number = 18,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 18 – Suzana Forte",
                    link = "https://www.animesonlinebr.com.br/video/50731"
                ),
                Episode(
                    number = 19,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 19 – Trem Misterioso",
                    link = "https://www.animesonlinebr.com.br/video/50732"
                ),
                Episode(
                    number = 20,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 20 – Vem Comigo",
                    link = "https://www.animesonlinebr.com.br/video/50733"
                ),
                Episode(
                    number = 21,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 21 – Barriga Da Besta",
                    link = "https://www.animesonlinebr.com.br/video/50734"
                ),
                Episode(
                    number = 22,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 22 – O Limite",
                    link = "https://www.animesonlinebr.com.br/video/50735"
                ),
                Episode(
                    number = 23,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 23 – Cineastras",
                    link = "https://www.animesonlinebr.com.br/video/50736"
                ),
                Episode(
                    number = 24,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 24 – Assinatura De Calor",
                    link = "https://www.animesonlinebr.com.br/video/50737"
                ),
                Episode(
                    number = 25,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 25 – Folia Mortal",
                    link = "https://www.animesonlinebr.com.br/video/50738"
                ),
                Episode(
                    number = 26,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (2ª Temporada) Episódio 26 – Recuo Mortal",
                    link = "https://www.animesonlinebr.com.br/video/50739"
                ),
                Episode(
                    number = 1,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 01 – Vitória Sobre A Fofura",
                    link = "https://www.animesonlinebr.com.br/video/50740"
                ),
                Episode(
                    number = 2,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 02 – Morituri Te Salutamos",
                    link = "https://www.animesonlinebr.com.br/video/50741"
                ),
                Episode(
                    number = 3,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 03 – Memória De Uma Memória",
                    link = "https://www.animesonlinebr.com.br/video/50742"
                ),
                Episode(
                    number = 4,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 04 – Batedor",
                    link = "https://www.animesonlinebr.com.br/video/50743"
                ),
                Episode(
                    number = 5,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 05 – Jovem Demais",
                    link = "https://www.animesonlinebr.com.br/video/50744"
                ),
                Episode(
                    number = 6,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 06 – O Monstro",
                    link = "https://www.animesonlinebr.com.br/video/50745"
                ),
                Episode(
                    number = 7,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 07 – Parados",
                    link = "https://www.animesonlinebr.com.br/video/50746"
                ),
                Episode(
                    number = 8,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 08 – Batalha Dos Magos",
                    link = "https://www.animesonlinebr.com.br/video/50747"
                ),
                Episode(
                    number = 9,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 09 – Fionna E Cake",
                    link = "https://www.animesonlinebr.com.br/video/50748"
                ),
                Episode(
                    number = 10,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 10 – O Que Faltava",
                    link = "https://www.animesonlinebr.com.br/video/50749"
                ),
                Episode(
                    number = 11,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 11 – O Ladrão De Maçãs",
                    link = "https://www.animesonlinebr.com.br/video/50750"
                ),
                Episode(
                    number = 12,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 12 – Bonitopia",
                    link = "https://www.animesonlinebr.com.br/video/50751"
                ),
                Episode(
                    number = 13,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 13 – O Susto",
                    link = "https://www.animesonlinebr.com.br/video/50752"
                ),
                Episode(
                    number = 14,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 14 – De Mal A Pior",
                    link = "https://www.animesonlinebr.com.br/video/50753"
                ),
                Episode(
                    number = 15,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 15 – Ninguém Te Ouvindo",
                    link = "https://www.animesonlinebr.com.br/video/50754"
                ),
                Episode(
                    number = 16,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 16 – Jake Vs Mi-Mau",
                    link = "https://www.animesonlinebr.com.br/video/50755"
                ),
                Episode(
                    number = 17,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 17 – Obrigado",
                    link = "https://www.animesonlinebr.com.br/video/50756"
                ),
                Episode(
                    number = 18,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 18 – A Nova Fronteira",
                    link = "https://www.animesonlinebr.com.br/video/50757"
                ),
                Episode(
                    number = 1,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 19 – Segredos Holly Jolly – Parte 1",
                    link = "https://www.animesonlinebr.com.br/video/50758"
                ),
                Episode(
                    number = 2,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 20 – Segredos Holly Jolly – Parte 2",
                    link = "https://www.animesonlinebr.com.br/video/50759"
                ),
                Episode(
                    number = 21,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 21 – O Armário De Marceline",
                    link = "https://www.animesonlinebr.com.br/video/50760"
                ),
                Episode(
                    number = 22,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 22 – Pedro Papel",
                    link = "https://www.animesonlinebr.com.br/video/50761"
                ),
                Episode(
                    number = 23,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 23 – De Outro Jeito",
                    link = "https://www.animesonlinebr.com.br/video/50762"
                ),
                Episode(
                    number = 24,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 24 – Princesa Fantasma",
                    link = "https://www.animesonlinebr.com.br/video/50763"
                ),
                Episode(
                    number = 25,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 25 – A Masmorra Do Papai",
                    link = "https://www.animesonlinebr.com.br/video/50764"
                ),
                Episode(
                    number = 26,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (3ª Temporada) Episódio 26 – INCÊNDIO",
                    link = "https://www.animesonlinebr.com.br/video/50765"
                ),
                Episode(
                    number = 1,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 01 – Quente Ao Toque",
                    link = "https://www.animesonlinebr.com.br/video/50766"
                ),
                Episode(
                    number = 2,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 02 – Cinco Historinhas",
                    link = "https://www.animesonlinebr.com.br/video/50767"
                ),
                Episode(
                    number = 3,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 03 – Teia De Estranhos",
                    link = "https://www.animesonlinebr.com.br/video/50768"
                ),
                Episode(
                    number = 4,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 04 – Sonho De Amor",
                    link = "https://www.animesonlinebr.com.br/video/50769"
                ),
                Episode(
                    number = 5,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 05 – Retorno A Noitosfera",
                    link = "https://www.animesonlinebr.com.br/video/50770"
                ),
                Episode(
                    number = 6,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 06 – A Monstrinha Do Papai",
                    link = "https://www.animesonlinebr.com.br/video/50771"
                ),
                Episode(
                    number = 7,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 07 – Seguindo Seus Passos",
                    link = "https://www.animesonlinebr.com.br/video/50772"
                ),
                Episode(
                    number = 8,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 08 – Lobo Do Abraço",
                    link = "https://www.animesonlinebr.com.br/video/50773"
                ),
                Episode(
                    number = 9,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 09 – Esposa Princesa Monstro",
                    link = "https://www.animesonlinebr.com.br/video/50774"
                ),
                Episode(
                    number = 10,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 10 – Goliad",
                    link = "https://www.animesonlinebr.com.br/video/50775"
                ),
                Episode(
                    number = 11,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 11 – Além Deste Plano Terreno",
                    link = "https://www.animesonlinebr.com.br/video/50776"
                ),
                Episode(
                    number = 12,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 12 – Te Peguei!",
                    link = "https://www.animesonlinebr.com.br/video/50777"
                ),
                Episode(
                    number = 13,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 13 – Princesa Biscoito",
                    link = "https://www.animesonlinebr.com.br/video/50778"
                ),
                Episode(
                    number = 14,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 14 – Guerra Das Cartas",
                    link = "https://www.animesonlinebr.com.br/video/50779"
                ),
                Episode(
                    number = 15,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 15 – Filhos De Marte",
                    link = "https://www.animesonlinebr.com.br/video/50780"
                ),
                Episode(
                    number = 16,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 16 – Pegando Um Foguinho",
                    link = "https://www.animesonlinebr.com.br/video/50781"
                ),
                Episode(
                    number = 17,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 17 – BMO Noire",
                    link = "https://www.animesonlinebr.com.br/video/50782"
                ),
                Episode(
                    number = 18,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 18 – Rei Minhoca",
                    link = "https://www.animesonlinebr.com.br/video/50783"
                ),
                Episode(
                    number = 19,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 19 – Lady & Jujuba",
                    link = "https://www.animesonlinebr.com.br/video/50784"
                ),
                Episode(
                    number = 20,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 20 – Você Me Fez!",
                    link = "https://www.animesonlinebr.com.br/video/50785"
                ),
                Episode(
                    number = 21,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 21 – Quem Ganha",
                    link = "https://www.animesonlinebr.com.br/video/50786"
                ),
                Episode(
                    number = 22,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 22 – Ponto De Ignição",
                    link = "https://www.animesonlinebr.com.br/video/50787"
                ),
                Episode(
                    number = 23,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 23 – O Fácil Difícil",
                    link = "https://www.animesonlinebr.com.br/video/50788"
                ),
                Episode(
                    number = 24,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 24 – Reino De Gunters",
                    link = "https://www.animesonlinebr.com.br/video/50789"
                ),
                Episode(
                    number = 25,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 25 – Me Lembro De Você",
                    link = "https://www.animesonlinebr.com.br/video/50790"
                ),
                Episode(
                    number = 1,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (4ª Temporada) Episódio 26 – O Lich (Parte 1)",
                    link = "https://www.animesonlinebr.com.br/video/50791"
                ),
                Episode(
                    number = 1,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 01 – Finn O Humano",
                    link = "https://www.animesonlinebr.com.br/video/50792"
                ),
                Episode(
                    number = 2,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 02 – Jake O Cão",
                    link = "https://www.animesonlinebr.com.br/video/50793"
                ),
                Episode(
                    number = 3,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 03 – Mais Cinco Historinhas",
                    link = "https://www.animesonlinebr.com.br/video/50794"
                ),
                Episode(
                    number = 4,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 04 – Subindo Na Árvore",
                    link = "https://www.animesonlinebr.com.br/video/50795"
                ),
                Episode(
                    number = 5,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 05 – Jake, O Pai",
                    link = "https://www.animesonlinebr.com.br/video/50796"
                ),
                Episode(
                    number = 6,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 06 – Davey",
                    link = "https://www.animesonlinebr.com.br/video/50797"
                ),
                Episode(
                    number = 7,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 07 – A Culpa é Toda Sua",
                    link = "https://www.animesonlinebr.com.br/video/50798"
                ),
                Episode(
                    number = 8,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 08 – Amiguinho",
                    link = "https://www.animesonlinebr.com.br/video/50799"
                ),
                Episode(
                    number = 9,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 09 – Menininho Mal",
                    link = "https://www.animesonlinebr.com.br/video/50800"
                ),
                Episode(
                    number = 10,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 10 – Caverna De Ossos",
                    link = "https://www.animesonlinebr.com.br/video/50801"
                ),
                Episode(
                    number = 11,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 11 – O Grande Homem Pássaro",
                    link = "https://www.animesonlinebr.com.br/video/50802"
                ),
                Episode(
                    number = 12,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 12 – Simon & Marcy",
                    link = "https://www.animesonlinebr.com.br/video/50803"
                ),
                Episode(
                    number = 13,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 13 – Erro E Erro",
                    link = "https://www.animesonlinebr.com.br/video/50804"
                ),
                Episode(
                    number = 14,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 14 – Puhoy",
                    link = "https://www.animesonlinebr.com.br/video/50805"
                ),
                Episode(
                    number = 15,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 15 – BMO Perdido",
                    link = "https://www.animesonlinebr.com.br/video/50806"
                ),
                Episode(
                    number = 16,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 16 – Festinha De Princesas",
                    link = "https://www.animesonlinebr.com.br/video/50807"
                ),
                Episode(
                    number = 17,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 17 – James Baxter, O Cavalo",
                    link = "https://www.animesonlinebr.com.br/video/50808"
                ),
                Episode(
                    number = 18,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 18 – Um Último Serviço",
                    link = "https://www.animesonlinebr.com.br/video/50809"
                ),
                Episode(
                    number = 19,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 19 – James Baxter, O Cavalo",
                    link = "https://www.animesonlinebr.com.br/video/50810"
                ),
                Episode(
                    number = 20,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 20 – Silencio!!",
                    link = "https://www.animesonlinebr.com.br/video/50811"
                ),
                Episode(
                    number = 21,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 21 – O Pretendente",
                    link = "https://www.animesonlinebr.com.br/video/50812"
                ),
                Episode(
                    number = 22,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 22 – Acabou A Festa, Isla De Señorita",
                    link = "https://www.animesonlinebr.com.br/video/50813"
                ),
                Episode(
                    number = 23,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 23 – Um Último Serviço",
                    link = "https://www.animesonlinebr.com.br/video/50814"
                ),
                Episode(
                    number = 24,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 24 – Mais Outras Cinco Historinhas",
                    link = "https://www.animesonlinebr.com.br/video/50815"
                ),
                Episode(
                    number = 25,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 25 – Ruas Doces",
                    link = "https://www.animesonlinebr.com.br/video/50816"
                ),
                Episode(
                    number = 26,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 26 – Só Magos, Tolos",
                    link = "https://www.animesonlinebr.com.br/video/50817"
                ),
                Episode(
                    number = 27,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 27 – Jake Suit",
                    link = "https://www.animesonlinebr.com.br/video/50818"
                ),
                Episode(
                    number = 28,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 28 – Ser Mais",
                    link = "https://www.animesonlinebr.com.br/video/50819"
                ),
                Episode(
                    number = 29,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 29 – A Bruxa Do Céu",
                    link = "https://www.animesonlinebr.com.br/video/50820"
                ),
                Episode(
                    number = 30,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 30 – Frost & Fogo",
                    link = "https://www.animesonlinebr.com.br/video/50821"
                ),
                Episode(
                    number = 31,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 31 – Velha Demais",
                    link = "https://www.animesonlinebr.com.br/video/50822"
                ),
                Episode(
                    number = 32,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 32 – O Cofre",
                    link = "https://www.animesonlinebr.com.br/video/50823"
                ),
                Episode(
                    number = 33,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 33 – Jogos De Amor",
                    link = "https://www.animesonlinebr.com.br/video/50824"
                ),
                Episode(
                    number = 34,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 34 – O Trem Masmorra",
                    link = "https://www.animesonlinebr.com.br/video/50825"
                ),
                Episode(
                    number = 35,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 35 – Jogos De Amor",
                    link = "https://www.animesonlinebr.com.br/video/50826"
                ),
                Episode(
                    number = 36,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 36 – Trem De Calabouço",
                    link = "https://www.animesonlinebr.com.br/video/50827"
                ),
                Episode(
                    number = 37,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 37 – Príncipe De Caixa",
                    link = "https://www.animesonlinebr.com.br/video/50828"
                ),
                Episode(
                    number = 38,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 38 – Fome De Vermelho",
                    link = "https://www.animesonlinebr.com.br/video/50829"
                ),
                Episode(
                    number = 39,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 39 – Consertamos Um Caminhão",
                    link = "https://www.animesonlinebr.com.br/video/50830"
                ),
                Episode(
                    number = 40,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 40 – Data Do Jogo",
                    link = "https://www.animesonlinebr.com.br/video/50831"
                ),
                Episode(
                    number = 41,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 41 – O Pit",
                    link = "https://www.animesonlinebr.com.br/video/50832"
                ),
                Episode(
                    number = 42,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 42 – James",
                    link = "https://www.animesonlinebr.com.br/video/50833"
                ),
                Episode(
                    number = 43,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 43 – Tipo De Cerveja",
                    link = "https://www.animesonlinebr.com.br/video/50834"
                ),
                Episode(
                    number = 44,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 44 – Casamento De Maça",
                    link = "https://www.animesonlinebr.com.br/video/50835"
                ),
                Episode(
                    number = 45,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 45 – Lâmina De Grama",
                    link = "https://www.animesonlinebr.com.br/video/50836"
                ),
                Episode(
                    number = 46,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 46 – Rattleballs",
                    link = "https://www.animesonlinebr.com.br/video/50837"
                ),
                Episode(
                    number = 47,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 47 – O Trono Vermelho",
                    link = "https://www.animesonlinebr.com.br/video/50838"
                ),
                Episode(
                    number = 48,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 48 – Betty",
                    link = "https://www.animesonlinebr.com.br/video/50839"
                ),
                Episode(
                    number = 49,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 49 – Tempo Mau",
                    link = "https://www.animesonlinebr.com.br/video/50840"
                ),
                Episode(
                    number = 50,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 50 – Acorde",
                    link = "https://www.animesonlinebr.com.br/video/50841"
                ),
                Episode(
                    number = 51,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 51 – Fuga Da Cidadela",
                    link = "https://www.animesonlinebr.com.br/video/50842"
                ),
                Episode(
                    number = 52,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (5ª Temporada) Episódio 52 – James II",
                    link = "https://www.animesonlinebr.com.br/video/50843"
                ),
                Episode(
                    number = 1,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 01 – A Torre",
                    link = "https://www.animesonlinebr.com.br/video/50844"
                ),
                Episode(
                    number = 2,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 02 – Cara Triste",
                    link = "https://www.animesonlinebr.com.br/video/50845"
                ),
                Episode(
                    number = 3,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 03 – Breezinha",
                    link = "https://www.animesonlinebr.com.br/video/50846"
                ),
                Episode(
                    number = 4,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 04 – Cadeia Alimentar",
                    link = "https://www.animesonlinebr.com.br/video/50847"
                ),
                Episode(
                    number = 5,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 05 – Móveis E Carne",
                    link = "https://www.animesonlinebr.com.br/video/50848"
                ),
                Episode(
                    number = 6,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 06 – O Príncipe Que Queria Tudo",
                    link = "https://www.animesonlinebr.com.br/video/50849"
                ),
                Episode(
                    number = 7,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 07 – Alguma Coisa Grande",
                    link = "https://www.animesonlinebr.com.br/video/50850"
                ),
                Episode(
                    number = 8,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 08 – Irmãozinho",
                    link = "https://www.animesonlinebr.com.br/video/50851"
                ),
                Episode(
                    number = 9,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 09 – Ocarina",
                    link = "https://www.animesonlinebr.com.br/video/50852"
                ),
                Episode(
                    number = 10,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 10 – Obrigado Pelas Maçãs, Giuseppe!",
                    link = "https://www.animesonlinebr.com.br/video/50853"
                ),
                Episode(
                    number = 11,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 11 – O Dia Da Princesa",
                    link = "https://www.animesonlinebr.com.br/video/50854"
                ),
                Episode(
                    number = 12,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 12 – Nêmesis",
                    link = "https://www.animesonlinebr.com.br/video/50855"
                ),
                Episode(
                    number = 13,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 13 – Joshua E Margaret",
                    link = "https://www.animesonlinebr.com.br/video/50856"
                ),
                Episode(
                    number = 14,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 14 – Mosca Fantasma",
                    link = "https://www.animesonlinebr.com.br/video/50857"
                ),
                Episode(
                    number = 15,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 15 – Tudo é Jake",
                    link = "https://www.animesonlinebr.com.br/video/50858"
                ),
                Episode(
                    number = 16,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 16 – É Você?",
                    link = "https://www.animesonlinebr.com.br/video/50859"
                ),
                Episode(
                    number = 17,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 17 – Jake O Tijolo",
                    link = "https://www.animesonlinebr.com.br/video/50860"
                ),
                Episode(
                    number = 18,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 18 – Dentista",
                    link = "https://www.animesonlinebr.com.br/video/50861"
                ),
                Episode(
                    number = 19,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 19 – O Resfriador",
                    link = "https://www.animesonlinebr.com.br/video/50862"
                ),
                Episode(
                    number = 20,
                    animeName = "Adventure Time (Hora de Aventura)",
                    description = "Adventure Time (Hora de Aventura) - Hora de Aventura (6ª Temporada) Episódio 20 – A Guerra Do Pijama",
                    link = "https://www.animesonlinebr.com.br/video/50863"
                )
            )
        )
    }

    init {
        FactoryChecker.describe(AnimesOnlineBrFactory) {
            whenCheckIsEpisode(VALID_URLS, INVALID_URLS)
            Spectrum.describe("when episode page") {
                whenEpisode(ESPISODE_AND_SINGLE)
            }
            Spectrum.describe("when anime about page") {
                whenEpisode(ABOUT_PAGE)
            }
        }
    }
}
