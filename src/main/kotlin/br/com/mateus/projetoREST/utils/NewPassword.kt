package br.com.mateus.projetoREST.utils

import java.util.*

class NewPassword {

    private val rand: Random = Random()

    fun newPassword(): String {
        return getRandomPassword()
    }

    private fun getRandomPassword(): String {
        val random = Random()
        val names = arrayOf("zuno", "zuna", "zona", "zaga", "voto", "vota", "voou", "voga", "voas", "voar", "voam", "vazo", "vaza", "vaso", "varo", "vara", "vamo", "valo", "vala", "vago", "vaga", "uvas", "usou", "usos", "usas", "usar", "usam", "urso", "ursa", "urro", "urra", "urna", "urjo", "urja", "urdo", "urda", "unto", "unta", "unha", "unas", "unam", "umas", "tudo", "tubo", "tuas", "traz", "toso", "tosa", "toro", "tora", "topo", "topa", "tons", "tona", "tomo", "toma", "tolo", "tola", "toga", "todo", "toda", "taxo", "taxa", "tatu", "tato", "tara", "tapo", "tapa", "talo", "tala", "taba", "suou", "suor", "sumo", "suma", "sujo", "suja", "sugo", "suga", "subo", "suba", "suas", "suar", "suam", "sovo", "sova", "soro", "sopa", "soou", "sons", "sono", "somo", "soma", "solo", "sola", "soja", "soda", "soas", "soar", "soam", "saro", "sara", "sapo", "sano", "sana", "sala", "saga", "safo", "safa", "rumo", "ruma", "rujo", "ruja", "rugo", "ruga", "rufo", "rufa", "ruas", "roxo", "roxa", "roto", "rota", "rolo", "rola", "rogo", "roga", "rodo", "roda", "roas", "roam", "rato", "rata", "raso", "rasa", "raro", "rara", "rapo", "rapa", "ramo", "ralo", "rala", "rajo", "raja", "rabo", "qual", "puxo", "puxa", "puro", "pura", "puno", "puna", "puma", "pulo", "pula", "puas", "proa", "povo", "poso", "posa", "poro", "pomo", "podo", "poda", "paus", "pato", "pata", "paro", "para", "papo", "papa", "pano", "pago", "paga", "ovos", "oval", "ouso", "ousa", "ouro", "osso", "orou", "orno", "orna", "orlo", "orla", "oras", "orar", "oram", "oral", "opus", "opto", "opta", "opor", "onda", "olho", "olha", "odor", "obra", "nuns", "numa", "nulo", "nula", "nuas", "novo", "noto", "nota", "nora", "nono", "nona", "nojo", "nato", "nata", "nano", "nana", "naja", "nado", "nada", "nabo", "musa", "muro", "mura", "mula", "mujo", "muja", "mudo", "muda", "muar", "movo", "mova", "moto", "moro", "mora", "mono", "momo", "mola", "mofo", "mofa", "modo", "moda", "moas", "moam", "maus", "mato", "mata", "mapa", "mano", "mana", "mamo", "mama", "mala", "mago", "luzo", "luza", "luxo", "luva", "luto", "luta", "luso", "lusa", "lupa", "lula", "luas", "luar", "loto", "lota", "lona", "loja", "logo", "lodo", "lobo", "loba", "lhos", "lhas", "lavo", "lava", "lata", "lapa", "lama", "lago", "lado", "juta", "juro", "jura", "jugo", "juba", "jota", "jogo", "joga", "jazo", "jaza", "jato", "huno", "humo", "hora", "halo", "haja", "gusa", "gula", "grau", "gota", "goro", "gora", "gomo", "goma", "gols", "gola", "gnus", "gato", "gata", "gamo", "gama", "galo", "gala", "gajo", "gaja", "gago", "gaga", "gado", "gabo", "gaba", "fuso", "furo", "fura", "fumo", "fuma", "fujo", "fuja", "fuga", "fruo", "frua", "foto", "foro", "fora", "fogo", "fofo", "fofa", "fluo", "flua", "flor", "favo", "fava", "fato", "fama", "falo", "fala", "fado", "fada", "duto", "duro", "dura", "duns", "duna", "duma", "duas", "dual", "doto", "dota", "doso", "dosa", "dopo", "dopa", "doou", "dons", "dono", "dona", "domo", "doma", "dolo", "doas", "doar", "doam", "dava", "dato", "data", "dano", "dana", "damo", "dama", "dado", "dada", "buxo", "bulo", "bula", "bufo", "bufa", "buda", "broa", "boto", "bota", "boro", "bons", "bolo", "bola", "bobo", "boba", "boas", "bato", "bata", "balo", "bala", "bago", "baga", "bafo", "babo", "baba", "azul", "azar", "avos", "aval", "auto", "aura", "aula", "atuo", "atum", "atua", "atou", "atos", "ator", "atol", "atas", "atar", "atam", "asso", "assa", "aspa", "asno", "asma", "asas", "arou", "aros", "armo", "arma", "arfo", "arfa", "ardo", "arda", "aras", "arar", "aram", "apto", "apta", "anuo", "anua", "anta", "anos", "anjo", "ando", "anda", "amou", "amos", "amor", "amas", "amar", "amam", "alvo", "alva", "alto", "alta", "alma", "alho", "algo", "alga", "alfa", "alas", "ajas", "ajam", "aguo", "agua", "agro", "agar", "afta", "afro", "adro", "abro", "abra", "abas", "Ruhr", "Rosa", "Roma", "Ogum", "Nova")
        val password = names[random.nextInt(names.size)]
        var p: String? = password
        p = p?.let { firstUpperCase(it) }
        var n: String = random.nextInt(99).toString()
        if (n.length == 1) {
            n +=random.nextInt(9)
        }
        p += n
        return p.toString()
    }

    private fun firstUpperCase(lWord: String): String {
        val palav = lWord.split(" ").toTypedArray()
        var lReturn = ""
        for (i in palav.indices) {
            if (lReturn.isNotEmpty()) {
                lReturn += " "
            }
            if (lWord.isNotEmpty()) {
                lReturn += palav[i].substring(0, 1).toUpperCase() + palav[i].substring(1)
            } else {
                if (palav[i].length > 1) {
                    lReturn += palav[i].substring(0, 1).toUpperCase()
                }
            }
        }
        return lReturn
    }



}