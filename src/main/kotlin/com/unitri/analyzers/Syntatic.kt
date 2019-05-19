package com.unitri.analyzers

import com.unitri.gramar.Keywords
import com.unitri.gramar.Operators
import com.unitri.table.Token
import com.unitri.tree.Node

@Suppress("SpellCheckingInspection")
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

    private fun addError(expected: List<String>, token: Token) {
        errorList.add(String.format("Expected %s at position %d:%d", expected, token.line, token.column))
    }

    private fun nextToken() {
        currentToken = tokens[++position]
    }

    private fun lookAhead(quantity: Int): Token {
        return tokens[position + quantity]
    }

    fun analyze(): Pair<Node, List<String>> {
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
                addError(listOf(")"), currentToken)
            }
        } else {
            addError(listOf("("), currentToken)
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
                    addError(listOf(":"), currentToken)
                }
            } else {
                addError(listOf(Token.TokenClass.ID.value), currentToken)
            }
        } else {
            addError(listOf("fun"), currentToken)
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
                    addError(listOf(")"), currentToken)
                }
            } else {
                addError(listOf(Token.TokenClass.ID.value), currentToken)
            }
        } else {
            addError(listOf("("), currentToken)
        }
    }

    //    <tipo> ::= 'int' | 'real' | 'texto' | 'logico' | 'nada'
    fun tipo() {
        val tipos = Keywords.types()
        if (tipos.contains(currentToken.image)) {
            nextToken()
        } else {
            addError(tipos, currentToken)
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
                addError(listOf(")"), currentToken)
            }
        } else {
            addError(listOf("("), currentToken)
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
                    addError(errors, currentToken)
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
            addError(listOf(Token.TokenClass.ID.value), currentToken)
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
                addError(listOf(Token.TokenClass.ID.value), currentToken)
            }
        } else {
            addError(listOf("="), currentToken)
        }
    }

    //    <expr> :: <operan> | '(' <op2> <expr> <expr> ')' | '(' <op1> id ')' | '(' <invoca> ')'
    fun expr() {
        if (Token.TokenClass.literalConstants().contains(currentToken.image)) {
            operan()
        } else if (currentToken.image == "(") {

            val lookAhead = lookAhead(1)

            when {
                Operators.isDefaultOperator(lookAhead.image) -> op2()
                Operators.isUnaryOperator(lookAhead.image) -> op1()
                lookAhead.tokenClass == Token.TokenClass.ID -> invoca()
                else -> {
                    val expected =
                        listOf(
                            Operators.operators().toString(),
                            Operators.unaryOperators().toString(),
                            Token.TokenClass.ID.value
                        )
                    addError(expected, lookAhead)
                }
            }

        }
    }

    //    <op2> ::= '&&' | '||' | '>' | '>=' | '<' | '<=' | '==' | '!=' | '.' | '+' | '-' | '*' | '/'
    fun op2() {
        if (Operators.operators().contains(currentToken.image)) {
            nextToken()
        } else {
            addError(Operators.operators(), currentToken)
        }
    }

    //    <op1> ::= '++' | '--'
    fun op1() {
        if (Operators.unaryOperators().contains(currentToken.image)) {
            nextToken()
        } else {
            addError(Operators.unaryOperators(), currentToken)
        }
    }

    //    <invoca> ::= id <args>
    fun invoca() {
        if (currentToken.tokenClass == Token.TokenClass.ID) {
            nextToken()
            args()
        } else {
            addError(listOf(Token.TokenClass.ID.value), currentToken)
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
                addError(listOf(")"), currentToken)
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
                    addError(listOf(")"), currentToken)
                }
            } else {
                addError(listOf("("), currentToken)
            }
        } else {
            addError(listOf("se"), currentToken)
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
                addError(listOf(")"), currentToken)
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
                addError(listOf(Token.TokenClass.ID.value), currentToken)
            }
        } else {
            addError(listOf("le"), currentToken)
        }
    }

    //    <mostrar> ::= 'mostra '<expr>
    fun mostrar() {
        if (currentToken.image == "mostra") {
            nextToken()
            expr()
        } else {
            addError(listOf("mostra"), currentToken)
        }
    }

    //    <enquanto> ::= 'enquanto' <expr> <comandos>
    fun enquanto() {
        if (currentToken.image == "enquanto") {
            expr()
            comandos()
        } else {
            addError(listOf("enquanto"), currentToken)
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
            addError(listOf("para"), currentToken)
        }
    }

    //    <retorno> ::= 'ret' <expr>
    fun retorno() {
        if (currentToken.image == "ret") {
            nextToken()
            expr()
        } else {
            addError(listOf("ret"), currentToken)
        }
    }

}