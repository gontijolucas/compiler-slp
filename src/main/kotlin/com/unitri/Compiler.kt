package com.unitri

import com.unitri.analyzers.Lexical
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    args.forEach {
        val content = readFileContent(it)
        val tokentable = Lexical.createTable(content)
        println(tokentable)
    }
}

@Throws(IOException::class)
fun readFileContent(filePath: String): MutableList<String> {
    return Files.readAllLines(Paths.get(filePath))
}