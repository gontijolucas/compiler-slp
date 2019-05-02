package com.unitri.analyzers

import com.unitri.gramar.Keywords
import com.unitri.gramar.Operators
import com.unitri.table.Token
import com.unitri.tree.Node

class Syntatic(
    var tokens: MutableList<Token>
) {

    private lateinit var currentToken: Token
    private var position: Int = -1
    private val errorList = mutableListOf<String>()
    private lateinit var rootNode: Node

    init {
        nextToken()
    }

    fun errorLog(expected: List<String>, token: Token) {
        errorList.add(String.format("Expected %s at position %d:%d", expected, token.line, token.column))
    }

    private fun nextToken() {
        currentToken = tokens[++position]
    }

    private fun lookAhead(quantity: Int): Token {
        return tokens[position + quantity]
    }

    fun analyze(tokens: MutableList<Token>): Pair<Node, List<String>> {
        programa()
        return Pair(rootNode, errorList)
    }

    //    <programa> ::= <comando> <programa> | <funcao> <programa> | &
    fun programa() {
        if (currentToken.image == "(") {
            if (lookAhead(1).image == "fun") {
                this.funcao()
                this.programa()
            } else {
                this.comando()
                this.programa()
            }
        }
    }

    //    <funcao> ::= '(' <funcao-interna> ')'
    fun funcao() {
        if (currentToken.image == "(") {
            nextToken()
            this.funcaoInterna()
            if (currentToken.image == ")") {
                nextToken()
            } else {
                errorLog(listOf(")"), currentToken)
            }
        } else {
            errorLog(listOf("("), currentToken)
        }
    }

    //    <funcao-interna> ::= 'fun' id <params> ':' <tipo> <comandos>
    fun funcaoInterna() {
        if (currentToken.image == "fun") {
            nextToken()
            if (currentToken.tokenClass == Token.TokenClass.ID) {
                nextToken()
                params()
                if (currentToken.image == ":") {
                    nextToken()
                    tipo()
                    comandos()
                } else {
                    errorLog(listOf(":"), currentToken)
                }
            } else {
                errorLog(listOf(Token.TokenClass.ID.value), currentToken)
            }
        } else {
            errorLog(listOf("fun"), currentToken)
        }
    }

    //    <params> ::= <param> <params> | &
    fun params() {
        if (lookAhead(1).image == "(") {
            param()
            params()
        }
    }

    //    <param> ::= '(' <tipo> id ')'
    fun param() {
        if (currentToken.image == "(") {
            nextToken()
            tipo()
            if (currentToken.tokenClass == Token.TokenClass.ID) {
                nextToken()
                if (currentToken.image == ")") {
                    nextToken()
                } else {
                    errorLog(listOf(")"), currentToken)
                }
            } else {
                errorLog(listOf(Token.TokenClass.ID.value), currentToken)
            }
        } else {
            errorLog(listOf("("), currentToken)
        }
    }

    //    <tipo> ::= 'int' | 'real' | 'texto' | 'logico' | 'nada'
    fun tipo() {
        val tipos = Keywords.types()
        if (tipos.contains(currentToken.image)) {
            nextToken()
        } else {
            errorLog(tipos, currentToken)
        }
    }

    //    <comandos> ::= <comando> <comandos> | &
    fun comandos() {
        if (lookAhead(1).image == "(") {
            comando()
            comandos()
        }
    }

    //    <comando> ::= '(' <comando-interno> ')'
    fun comando() {
        if (currentToken.image == "(") {
            nextToken()
            comandoInterno()
            if (currentToken.image == ")") {
                nextToken()
            } else {
                errorLog(listOf(")"), currentToken)
            }
        } else {
            errorLog(listOf("("), currentToken)
        }
    }

    //    <comando-interno> ::= <decl> | <atrib> | <invoca> | <se> | <leitura> | <enquanto> | <para> | <retorno> | <mostrar>
    fun comandoInterno() {
        when (currentToken.image) {
            in Keywords.types() -> decl()
            "=" -> atrib()
            "se" -> se()
            "le" -> leitura()
            "enquanto" -> enquanto()
            "para" -> para()
            "ret" -> retorno()
            "mostra" -> mostrar()
            else -> {
                if (currentToken.tokenClass == Token.TokenClass.ID) {
                    invoca()
                } else {
                    val errors = Keywords.types().toMutableList()
                    errors += listOf("=", "se", "enquanto", "para", "ret", "mostrar")
                    errorLog(errors, currentToken)
                }
            }
        }
    }

    //    <decl> ::= <tipo> <ids>
    fun decl() {
        tipo()
        ids()
    }

    //    <ids> ::= id <ids2>
    fun ids() {
        if (currentToken.tokenClass == Token.TokenClass.ID) {
            nextToken()
            ids2()
        } else {
            errorLog(listOf(Token.TokenClass.ID.value), currentToken)
        }
    }

    //    <ids2> ::= id <ids2> | &
    fun ids2() {
        if (currentToken.tokenClass == Token.TokenClass.ID) {
            nextToken()
            ids2()
        }
    }

    //    <atrib> ::= '=' id <expr>
    fun atrib() {
        if (currentToken.image == "=") {
            nextToken()
            if (currentToken.tokenClass == Token.TokenClass.ID) {
                nextToken()
                expr()
            } else {
                errorLog(listOf(Token.TokenClass.ID.value), currentToken)
            }
        } else {
            errorLog(listOf("="), currentToken)
        }
    }

    //    <expr> ::= <operan> | '(' <op2> <expr> <expr> ')' | '(' <op1> id ')'
    fun expr() {
        TODO("")
//        if (Token.TokenClass.literalConstants().contains(currentToken.image)) {
//            operan()
//        } else {
//
//        }
    }

    //    <op2> ::= '&&' | '||' | '>' | '>=' | '<' | '<=' | '==' | '!=' | '.' | '+' | '-' | '*' | '/'
    fun op2() {
        if (Operators.operators().contains(currentToken.image)) {
            nextToken()
        } else {
            errorLog(Operators.operators(), currentToken)
        }
    }

    //    <op1> ::= '++' | '--'
    fun op1() {
        if (Operators.unaryOperators().contains(currentToken.image)) {
            nextToken()
        } else {
            errorLog(Operators.unaryOperators(), currentToken)
        }
    }

    //    <invoca> ::= id <args>
    fun invoca() {
        if (currentToken.tokenClass == Token.TokenClass.ID) {
            nextToken()
            args()
        } else {
            errorLog(listOf(Token.TokenClass.ID.value), currentToken)
        }
    }

    //    <args> ::= <expr> <args> | &
    fun args() {
        expr()
        args()
    }

    //    <operan> ::= id | cli | clr | cll | cls | '(' <invoca> ')'
    fun operan() {
        if (Token.TokenClass.literalConstants().contains(currentToken.image)) {
            nextToken()
        } else if (currentToken.image == "(") {
            nextToken()
            invoca()
            if (currentToken.image == ")") {
                nextToken()
            } else {
                errorLog(listOf(")"), currentToken)
            }
        }
    }

    //    <se> ::= 'se' <expr> '(' <comandos> ')' <senao>
    fun se() {
        if (currentToken.image == "se") {
            nextToken()
            expr()
            if (currentToken.image == "(") {
                nextToken()
                comandos()
                if (currentToken.image == ")") {
                    nextToken()
                    seNao()
                } else {
                    errorLog(listOf(")"), currentToken)
                }
            } else {
                errorLog(listOf("("), currentToken)
            }
        } else {
            errorLog(listOf("se"), currentToken)
        }
    }

    //    <senao> ::= '(' <comandos> ')' | &
    fun seNao() {
        if (currentToken.image == "(") {
            nextToken()
            comandos()
            if (currentToken.image == ")") {
                nextToken()
            } else {
                errorLog(listOf(")"), currentToken)
            }
        }
    }

    //    <leitura> ::= 'le' id
    fun leitura() {
        if (currentToken.image == "le") {
            nextToken()
            if (currentToken.tokenClass == Token.TokenClass.ID) {
                nextToken()
            } else {
                errorLog(listOf(Token.TokenClass.ID.value), currentToken)
            }
        } else {
            errorLog(listOf("le"), currentToken)
        }
    }

    //    <mostrar> ::= 'mostra '<expr>
    fun mostrar() {
        if (currentToken.image == "mostra") {
            nextToken()
            expr()
        } else {
            errorLog(listOf("mostra"), currentToken)
        }
    }

    //    <enquanto> ::= 'enquanto' <expr> <comandos>
    fun enquanto() {
        if (currentToken.image == "enquanto") {
            expr()
            comandos()
        } else {
            errorLog(listOf("enquanto"), currentToken)
        }
    }

    //    <para> ::= 'para' <atrib> <expr> <atrib> <comandos>
    fun para() {
        if (currentToken.image == "para") {
            nextToken()
            atrib()
            expr()
            atrib()
            comandos()
        } else {
            errorLog(listOf("para"), currentToken)
        }
    }

    //    <retorno> ::= 'ret' <expr>
    fun retorno() {
        if (currentToken.image == "ret") {
            nextToken()
            expr()
        } else {
            errorLog(listOf("ret"), currentToken)
        }
    }

}