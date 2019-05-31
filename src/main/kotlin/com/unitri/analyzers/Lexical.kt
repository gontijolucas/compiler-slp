package com.unitri.analyzers

import com.unitri.table.Token
import com.unitri.table.TokenTable
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.regex.Pattern

class Lexical {

    companion object {

        private const val CAPTURE_SENTENCES_INCLUDING_STRINGS = "([^\"]\\S*|\".+?\")\\s*"

        fun createTable(lines: MutableList<String>): TokenTable {

            removeComments(lines)
            removeExcessSpace(lines)

            val table = TokenTable()

            val index = AtomicInteger(0)
            lines.forEach {
                table.addTokens(nextLine(it, index.toInt()))
                index.addAndGet(1)
            }

            addEndOfFile(table)

            return table
        }

        private fun removeComments(lines: MutableList<String>) {
            lines.removeIf { it.startsWith("#") }
        }

        private fun removeExcessSpace(lines: MutableList<String>) {
            lines.removeIf { it.contentEquals("") }
            lines.replaceAll { it.trim().replace("\\s{2,}".toRegex(), " ") }
        }

        private fun nextLine(line: String, lineNumber: Int?): MutableList<Token> {

            val tokenTable = TokenTable()

            val symbolSequences = ArrayList<String>()
            val matcher = Pattern.compile(CAPTURE_SENTENCES_INCLUDING_STRINGS).matcher(line)
            while (matcher.find()) {
                symbolSequences.add(matcher.group(1)) // Add .replace("\"", "") to remove surrounding quotes.
            }

            var column = 0
            for (sequence in symbolSequences) {
                tokenTable.createTokenAndPutInTable(Token.TokenClass.getClass(sequence), sequence, lineNumber, column)
                column += sequence.length + 1
            }
            return tokenTable.tokens
        }

        private fun addEndOfFile(tokenTable: TokenTable) {
            tokenTable.createTokenAndPutInTable(Token.TokenClass.EOF, "$", -1, -1)
        }
    }
}