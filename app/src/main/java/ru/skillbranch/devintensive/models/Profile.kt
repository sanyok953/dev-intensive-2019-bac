package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils

data class Profile(
    val firstName: String,
    val lastName: String,
    //val initials: String,
    val about: String,
    val repository: String,
    val rating: Int = 0,
    val respect: Int = 0
) {
    val nickname = Utils.transliteration("$firstName $lastName", "_")!! // TODO
    val rank: String = "Junior Android Developer"

    fun toMap(): Map<String, Any> = mapOf(
        "nickName" to nickname,
        "rank" to rank,
        "firstName" to firstName,
        "lastName" to lastName,
        //"initials" to getInitials(firstName, lastName),
        "about" to about,
        "repository" to repository,
        "rating" to rating,
        "respect" to respect
    )
}