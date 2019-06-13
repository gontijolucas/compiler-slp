package com.unitri.analyzers

import com.unitri.table.Token
import com.unitri.table.TokenTable
import com.unitri.tree.Node
import java.util.ArrayList


class Semantic(table: TokenTable, node: Node) {

    private var tokens: List<Token> = table.tokens
    private var token: Token? = null
    private var pos: Int = -1
    private var root: Node = node
    private var errorList: MutableList<String> = ArrayList()

    init {
        readToken()
    }

    private fun readToken() {
        token = tokens[++pos]
    }

    private fun lookAhead(): Token {
        return tokens[pos + 1]
    }

    private fun addError(s: String, token: Token) {
        //        throw RuntimeException("Reason: $expected, received: $token")
        errorList.add(String.format("%s at position %d:%d", s, token.line, token.column))
    }

    fun analyze(): Unit {
        TODO("START")
    }

    //    <programa> ::= <comando> <programa> | <funcao> <programa> | &
    fun programa(): Any {
        TODO("Not implemented")
    }

    //    <funcao> ::= '(' <funcao-interna> ')'
    fun funcao(): Any {
        TODO("Not implemented")
    }

    //    <funcao-interna> ::= 'fun' id <params> ':' <tipo> <comandos>
    fun funcaoInterna(): Any {
        TODO("Not implemented")
    }

    //    <params> ::= <param> <params> | &
    fun params(): Any {
        TODO("Not implemented")
    }

    //    <param> ::= '(' <tipo> id ')'
    fun param(): Any {
        TODO("Not implemented")
    }

    //    <tipo> ::= 'int' | 'real' | 'texto' | 'logico' | 'nada'
    fun tipo(): Any {
        TODO("Not implemented")
    }

    //    <comandos> ::= <comando> <comandos> | &
    fun comandos(): Any {
        TODO("Not implemented")
    }

    //    <comando> ::= '(' <comando-interno> ')'
    fun comando(): Any {
        TODO("Not implemented")
    }

    //    <comando-interno> ::= <decl> | <atrib> | <invoca> | <se> | <leitura> | <enquanto> | <para> | <retorno> | <mostrar>
    fun comandoInterno(): Any {
        TODO("Not implemented")
    }

    //    <decl> ::= <tipo> <ids>
    fun decl(): Any {
        TODO("Not implemented")
    }

    //    <ids> ::= id <ids2>
    fun ids(): Any {
        TODO("Not implemented")
    }

    //    <ids2> ::= id <ids2> | &
    fun ids2(): Any {
        TODO("Not implemented")
    }

    //    <atrib> ::= '=' id <expr>
    fun atrib(): Any {
        TODO("Not implemented")
    }

    //    <expr> :: <operan> | '(' <op2> <expr> <expr> ')' | '(' <op1> id ')' | '(' <invoca> ')'
    fun expr(): Any {
        TODO("Not implemented")
    }

    //    <op2> ::= '&&' | '||' | '>' | '>=' | '<' | '<=' | '==' | '!=' | '.' | '+' | '-' | '*' | '/'
    fun op2(): Any {
        TODO("Not implemented")
    }

    //    <op1> ::= '++' | '--'
    fun op1(): Any {
        TODO("Not implemented")
    }

    //    <invoca> ::= id <args>
    fun invoca(): Any {
        TODO("Not implemented")
    }

    //    <args> ::= <expr> <args> | &
    fun args(): Any {
        TODO("Not implemented")
    }

    //    <operan> ::= id | cli | clr | cll | cls
    fun operan(): Any {
        TODO("Not implemented")
    }

    //    <se> ::= 'se' <expr> '(' <comandos> ')' <senao>
    fun se(): Any {
        TODO("Not implemented")
    }

    //    <senao> ::= '(' <comandos> ')' | &
    fun seNao(): Any {
        TODO("Not implemented")
    }

    //    <leitura> ::= 'le' id
    fun leitura(): Any {
        TODO("Not implemented")
    }

    //    <mostrar> ::= 'mostra '<expr>
    fun mostrar(): Any {
        TODO("Not implemented")
    }

    //    <enquanto> ::= 'enquanto' <expr> <comandos>
    fun enquanto(): Any {
        TODO("Not implemented")
    }

    //    <para> ::= 'para' '(' <atrib> ')' <expr> '(' <atrib> ')' <comandos>
    fun para(): Any {
        TODO("Not implemented")
    }

    //    <retorno> ::= 'ret' <expr>
    fun retorno(): Any {
        TODO("Not implemented")
    }

}