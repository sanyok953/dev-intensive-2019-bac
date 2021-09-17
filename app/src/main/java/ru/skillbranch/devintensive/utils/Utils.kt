package ru.skillbranch.devintensive.utils

import android.content.Context
import android.graphics.*
import android.util.TypedValue
import ru.skillbranch.devintensive.R
import kotlin.math.round

object Utils {



    val exlude_list = listOf(
        "enterprise",
        "features",
        "topics",
        "collections",
        "trending",
        "events",
        "marketplace",
        "pricing",
        "nonprofit",
        "customer-stories",
        "security",
        "login",
        "join"
    )

    fun charTranslit(value: String): String? {
        var charsMap = mutableMapOf<String, String>()
        var resp: String? = null
        charsMap.put("а", "a")
        charsMap.put("б", "b")
        charsMap.put("в", "v")
        charsMap.put("г", "g")
        charsMap.put("д", "d")
        charsMap.put("е", "e")
        charsMap.put("ё", "e")
        charsMap.put("ж", "zh")
        charsMap.put("з", "z")
        charsMap.put("и", "i")
        charsMap.put("й", "i")
        charsMap.put("к", "k")
        charsMap.put("л", "l")
        charsMap.put("м", "m")
        charsMap.put("н", "n")
        charsMap.put("о", "o")
        charsMap.put("п", "p")
        charsMap.put("р", "r")
        charsMap.put("с", "s")
        charsMap.put("т", "t")
        charsMap.put("у", "u")
        charsMap.put("ф", "f")
        charsMap.put("х", "h")
        charsMap.put("ц", "c")
        charsMap.put("ч", "ch")
        charsMap.put("ш", "sh")
        charsMap.put("щ", "sh'")
        charsMap.put("ъ", "")
        charsMap.put("ы", "i")
        charsMap.put("ь", "")
        charsMap.put("э", "e")
        charsMap.put("ю", "yu")
        charsMap.put("я", "ya")

        charsMap.put("А", "A")
        charsMap.put("Б", "B")
        charsMap.put("В", "V")
        charsMap.put("Г", "G")
        charsMap.put("Д", "D")
        charsMap.put("Е", "E")
        charsMap.put("Ё", "E")
        charsMap.put("Ж", "ZH")
        charsMap.put("З", "Z")
        charsMap.put("И", "I")
        charsMap.put("Й", "I")
        charsMap.put("К", "K")
        charsMap.put("Л", "L")
        charsMap.put("М", "M")
        charsMap.put("Н", "N")
        charsMap.put("О", "O")
        charsMap.put("П", "P")
        charsMap.put("Р", "R")
        charsMap.put("С", "S")
        charsMap.put("Т", "T")
        charsMap.put("У", "U")
        charsMap.put("Ф", "F")
        charsMap.put("Х", "H")
        charsMap.put("Ц", "C")
        charsMap.put("Ч", "CH")
        charsMap.put("Ш", "SH")
        charsMap.put("Щ", "SH'")
        charsMap.put("Ъ", "")
        charsMap.put("Ы", "I")
        charsMap.put("Ь", "")
        charsMap.put("Э", "E")
        charsMap.put("Ю", "YU")
        charsMap.put("Я", "YA")

        if (charsMap.containsKey(value)) {
            resp = charsMap.getValue(value)
        }
        return resp
    }


    fun validateRepoName(name: String?): Boolean {

        if (name.isNullOrBlank())
            return true

        var name_ = name.trim();
        if (name.takeLast(1) == "/") {
            name_ = name_.substring(0, name_.length - 1)
        }
        val prefix = when {
            name_.startsWith(
                "https://www.github.com/",
                ignoreCase = true
            ) -> "https://www.github.com/"
            name_.startsWith("https://github.com/", ignoreCase = true) -> "https://github.com/"
            name_.startsWith(
                "http://www.github.com/",
                ignoreCase = true
            ) -> "http://www.github.com/"
            name_.startsWith("http://github.com/", ignoreCase = true) -> "http://github.com/"
            name_.startsWith("www.github.com/", ignoreCase = true) -> "www.github.com/"
            name_.startsWith("github.com/", ignoreCase = true) -> "github.com/"
            else -> "-"
        }

        if (prefix.length > 5) {
            val user = name_.substring(prefix.length)
            if (user.contains("/", ignoreCase = true) || user.contains(" ", ignoreCase = true))
                return false;
            if (user !in exlude_list)
                return true
        }
        return false
    }


    fun parseFullName(fullName: String?): Pair<String?, String?> {
        if (fullName == null || fullName == "" || fullName == " ") {
            return null to null
        }

        var parts: List<String>? = fullName?.split(" ")

        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)

        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        var trString = ""
        for (c in payload) {
            if (c.toString() == " ") {
                trString += divider
            } else if (charTranslit(c.toString()) != null) {
                trString += charTranslit(c.toString())
            } else {
                trString += c.toString()
            }
        }
        return trString
    }

    fun skl(digit: Long, type: Int): String {
        val array1 : Array<String> = arrayOf("минуту", "минуты", "минут")
        val array2 : Array<String> = arrayOf("час", "часа", "часов")
        val array3 : Array<String> = arrayOf("день", "дня", "дней")
        val array4 : Array<String> = arrayOf("секунду", "секунды", "секунд")

        val array: Array<String> = if (type == 1) array1 else if (type == 2) array2 else if (type == 3) array3 else array4
        return when(sklCheck(digit)) {
            1L -> array[0]
            2L, 3L, 4L -> array[1]
            else -> array[2]
        }

    }

    fun sklNV(digit: Long, type: Int): String {
        val array1 : Array<String> = arrayOf("минуту", "минуты", "минут")
        val array2 : Array<String> = arrayOf("час", "часа", "часов")
        val array3 : Array<String> = arrayOf("день", "дня", "дней")
        val array4 : Array<String> = arrayOf("секунду", "секунды", "секунд")

        val array: Array<String> = if (type == 1) array1 else if (type == 2) array2 else if (type == 3) array3 else array4
        return when(sklCheck(digit)) {
            1L -> array[0]
            2L, 3L, 4L -> array[1]
            else -> array[2]
        }

    }

    fun sklCheck(value: Long): Long {
        val m = value % 10
        if (m == 1L || m == 2L || m == 3L || m == 4L) {
            if (value > 10) {
                var f = ""
                var l = ""
                for (i in value.toString()) {
                    f = l
                    l = i.toString()
                }
                if ((f+l).contains("11") || ("$f$l").contains("12") || ("$f$l").contains("13") || ("$f$l").contains("14")) {
                    return 8L
                }
            }
            return m
        } else {
            return m
        }
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        var initF: String? = null
        var initL: String? = null

        if (firstName != null && firstName != " " && firstName != "") {
            if (firstName != null) {
                for (c in firstName) {
                    if (c.toString() != null) {
                        initF = c.toString().toUpperCase()
                    } else {
                        initF = null
                    }
                    break
                }
            }
        } else {
            initF = null
        }

        if (lastName != null && lastName != " " && lastName != "") {
            if (lastName != null) {
                for (ch in lastName) {
                    if (ch.toString() != null) {
                        initL = ch.toString().toUpperCase()
                    } else {
                        initL = null
                    }
                    break
                }
            }
        } else {
            initL = null
        }

        if (initF == null && initL == null) return null
        else if (initF != null && initL == null) return initF
        else if (initF == null && initL != null) return initL
        else return "$initF$initL"
    }

    fun generateAvatar(ctx: Context, imgSize: Int, text: String, col: Int): Bitmap {

        //val imgSize = ctx.resources.getDimensionPixelSize(R.dimen.avatar_round_size)

        val rect = Rect().apply {
            bottom = imgSize
            right = imgSize
        }
        val value = TypedValue()
        val resColor = ctx.obtainStyledAttributes(value.data, intArrayOf(R.attr.colorAccent))
        val paintBg = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = col // resColor.getColor(0,0)
            isAntiAlias = true
        }


        val paintText = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = ctx.getColor(android.R.color.white)
            textSize = imgSize * 0.5f
            textAlign = Paint.Align.CENTER
        }

        val bitmap = Bitmap.createBitmap(imgSize, imgSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        with(canvas) {
            drawRect(rect, paintBg)
            drawText(text, round(imgSize/2f), round((imgSize/1.5f)), paintText)
        }
        resColor.recycle()
        return bitmap

    }

}