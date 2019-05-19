package com.unitri

import com.unitri.analyzers.Lexical
import com.unitri.analyzers.Syntatic
import com.unitri.table.TokenTable
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {

    if (args.isNullOrEmpty()) {
        throw IllegalArgumentException("Target program is not provided!")
    }

    var tokenTable = TokenTable()

    args.forEach {
        val content = readFileContent(it)
        tokenTable = Lexical.createTable(content)
        println(tokenTable)
    }

    val syntatic = Syntatic(tokenTable.tokens)
    val syntaticTree = syntatic.analyze(tokenTable.tokens)
}

@Throws(IOException::class)
fun readFileContent(filePath: String): MutableList<String> {
    return Files.readAllLines(Paths.get(filePath))
}