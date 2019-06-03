package com.unitri.analyzers

import com.unitri.gramar.Keywords.Companion.types
import com.unitri.gramar.Operators.Companion.isDefaultOperator
import com.unitri.gramar.Operators.Companion.isUnaryOperator
import com.unitri.gramar.Operators.Companion.operators
import com.unitri.gramar.Operators.Companion.unaryOperators
import com.unitri.table.Token
import com.unitri.table.Token.TokenClass.Companion.isLiteralConstant
import com.unitri.table.Token.TokenClass.Companion.literalConstants
import com.unitri.table.Token.TokenClass.EOF
import com.unitri.table.Token.TokenClass.ID
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
        startCurrentToken()
    }

    private fun addError(expected: List<String>, token: Token) {
        throw RuntimeException("Reason: $expected, received: $token")
//        errorList.add(String.format("Expected %s at position %d:%d", expected, token.line, token.column))
    }

    private fun startCurrentToken() {
        currentToken = tokens[++position]
    }

    private fun nextToken() {
        if (currentToken.tokenClass != EOF) {
            currentToken = tokens[++position]
        }
    }

    private fun lookAhead(quantity: Int): Token {
        return tokens[position + quantity]
    }

    fun analyze(): Node {
        rootNode = Node("ROOT")
        rootNode.insertChildren(programa())

        if (errorList.isEmpty()) {
            rootNode.showTree()
        } else {
            println(errorList)
        }

        return rootNode
    }

    //    <programa> ::= <comando> <programa> | <funcao> <programa> | &
    fun programa(): Node {
        val nodePrograma = Node("<programa>")
        if (currentToken.image == "(") {
            if (lookAhead(1).image == "fun") {
                nodePrograma.insertChildren(this.funcao())
                nodePrograma.insertChildren(this.programa())
            } else {
                nodePrograma.insertChildren(this.comando())
                nodePrograma.insertChildren(this.programa())
            }
        }
        return nodePrograma
    }

    //    <funcao> ::= '(' <funcao-interna> ')'
    fun funcao(): Node {
        val noFuncao = Node("<funcao>")
        if (currentToken.image == "(") {
            noFuncao.insertChildren(Node(currentToken))
            nextToken()
            noFuncao.insertChildren(this.funcaoInterna())
            if (currentToken.image == ")") {
                noFuncao.insertChildren(Node(currentToken))
                nextToken()
            } else {
                addError(listOf(")"), currentToken)
            }
        } else {
            addError(listOf("("), currentToken)
        }
        return noFuncao
    }

    //    <funcao-interna> ::= 'fun' id <params> ':' <tipo> <comandos>
    fun funcaoInterna(): Node {
        val nodeFuncaoInterna = Node("<funcao-interna>")
        if (currentToken.image == "fun") {
            nodeFuncaoInterna.insertChildren(Node(currentToken))
            nextToken()
            if (currentToken.tokenClass == ID) {
                nodeFuncaoInterna.insertChildren(Node(currentToken))
                nextToken()
                nodeFuncaoInterna.insertChildren(params())
                if (currentToken.image == ":") {
                    nodeFuncaoInterna.insertChildren(Node(currentToken))
                    nextToken()
                    nodeFuncaoInterna.insertChildren(tipo())
                    nodeFuncaoInterna.insertChildren(comandos())
                } else {
                    addError(listOf(":"), currentToken)
                }
            } else {
                addError(listOf(ID.value), currentToken)
            }
        } else {
            addError(listOf("fun"), currentToken)
        }
        return nodeFuncaoInterna
    }

    //    <params> ::= <param> <params> | &
    fun params(): Node {
        val nodeParams = Node("<params>")
        if (currentToken.image == "(") {
            nodeParams.insertChildren(param())
            nodeParams.insertChildren(params())
        }
        return nodeParams
    }

    //    <param> ::= '(' <tipo> id ')'
    fun param(): Node {
        val nodeParam = Node("<param>")
        if (currentToken.image == "(") {
            nodeParam.insertChildren(Node(currentToken))
            nextToken()
            nodeParam.insertChildren(tipo())
            if (currentToken.tokenClass == ID) {
                nodeParam.insertChildren(Node(currentToken))
                nextToken()
                if (currentToken.image == ")") {
                    nodeParam.insertChildren(Node(currentToken))
                    nextToken()
                } else {
                    addError(listOf(")"), currentToken)
                }
            } else {
                addError(listOf(ID.value), currentToken)
            }
        } else {
            addError(listOf("("), currentToken)
        }
        return nodeParam
    }

    //    <tipo> ::= 'int' | 'real' | 'texto' | 'logico' | 'nada'
    fun tipo(): Node {
        val nodeTipo = Node("<tipo>")
        val tipos = types()
        if (tipos.contains(currentToken.image)) {
            nodeTipo.insertChildren(Node(currentToken))
            nextToken()
        } else {
            addError(tipos, currentToken)
        }
        return nodeTipo
    }

    //    <comandos> ::= <comando> <comandos> | &
    fun comandos(): Node {
        val nodeComandos = Node("<comandos>")
        if (currentToken.image == "(") {
            nodeComandos.insertChildren(comando())
            nodeComandos.insertChildren(comandos())
        }
        return nodeComandos
    }

    //    <comando> ::= '(' <comando-interno> ')'
    fun comando(): Node {
        val nodeComando = Node("<comando>")
        if (currentToken.image == "(") {
            nodeComando.insertChildren(Node(currentToken))
            nextToken()
            nodeComando.insertChildren(comandoInterno())
            if (currentToken.image == ")") {
                nodeComando.insertChildren(Node(currentToken))
                nextToken()
            } else {
                addError(listOf(")"), currentToken)
            }
        } else {
            addError(listOf("("), currentToken)
        }
        return nodeComando
    }

    //    <comando-interno> ::= <decl> | <atrib> | <invoca> | <se> | <leitura> | <enquanto> | <para> | <retorno> | <mostrar>
    fun comandoInterno(): Node {
        val nodeComandoInterno = Node("<comando-interno>")
        when (currentToken.image) {
            in types() -> nodeComandoInterno.insertChildren(decl())
            "=" -> nodeComandoInterno.insertChildren(atrib())
            "se" -> nodeComandoInterno.insertChildren(se())
            "le" -> nodeComandoInterno.insertChildren(leitura())
            "enquanto" -> nodeComandoInterno.insertChildren(enquanto())
            "para" -> nodeComandoInterno.insertChildren(para())
            "ret" -> nodeComandoInterno.insertChildren(retorno())
            "mostra" -> nodeComandoInterno.insertChildren(mostrar())
            else -> {
                if (currentToken.tokenClass == ID) {
                    nodeComandoInterno.insertChildren(invoca())
                } else {
                    val errors = types().toMutableList()
                    errors += listOf("=", "se", "enquanto", "para", "ret", "mostrar")
                    addError(errors, currentToken)
                }
            }
        }
        return nodeComandoInterno
    }

    //    <decl> ::= <tipo> <ids>
    fun decl(): Node {
        val nodeDecl = Node("<decl>")
        nodeDecl.insertChildren(tipo())
        nodeDecl.insertChildren(ids())
        return nodeDecl
    }

    //    <ids> ::= id <ids2>
    fun ids(): Node {
        val nodeIds = Node("<ids>")
        if (currentToken.tokenClass == ID) {
            nodeIds.insertChildren(Node(currentToken))
            nextToken()
            nodeIds.insertChildren(ids2())
        } else {
            addError(listOf(ID.value), currentToken)
        }
        return nodeIds
    }

    //    <ids2> ::= id <ids2> | &
    fun ids2(): Node {
        val nodeIds2 = Node("<ids2>")
        if (currentToken.tokenClass == ID) {
            nodeIds2.insertChildren(Node(currentToken))
            nextToken()
            nodeIds2.insertChildren(ids2())
        }
        return nodeIds2
    }

    //    <atrib> ::= '=' id <expr>
    fun atrib(): Node {
        val nodeAtrib = Node("<atrib>")
        if (currentToken.image == "=") {
            nodeAtrib.insertChildren(Node(currentToken))
            nextToken()
            if (currentToken.tokenClass == ID) {
                nodeAtrib.insertChildren(Node(currentToken))
                nextToken()
                nodeAtrib.insertChildren(expr())
            } else {
                addError(listOf(ID.value), currentToken)
            }
        } else {
            addError(listOf("="), currentToken)
        }
        return nodeAtrib
    }

    //    <expr> :: <operan> | '(' <op2> <expr> <expr> ')' | '(' <op1> id ')' | '(' <invoca> ')'
    fun expr(): Node {
        val nodeExpr = Node("<expr>")
        val lookAhead = lookAhead(1)

        // <operan>
        if (isLiteralConstant(currentToken.tokenClass.name)) {
            nodeExpr.insertChildren(operan())
        }
        // '(' <invoca> ')'
        else if (currentToken.image == "(" && lookAhead.tokenClass == ID) {
            nodeExpr.insertChildren(Node(currentToken))
            nextToken()
            nodeExpr.insertChildren(invoca())
            if (currentToken.image == ")") {
                nodeExpr.insertChildren(Node(currentToken))
                nextToken()
            } else {
                addError(listOf(")"), currentToken)
            }
        }
        // '(' <op2> <expr> <expr> ')'
        else if (currentToken.image == "(" && isDefaultOperator(lookAhead.image)) {
            nodeExpr.insertChildren(Node(currentToken))
            nextToken()
            nodeExpr.insertChildren(op2())
            nodeExpr.insertChildren(expr())
            nodeExpr.insertChildren(expr())
            if (currentToken.image == ")") {
                nodeExpr.insertChildren(Node(currentToken))
                nextToken()
            } else {
                addError(listOf(")"), currentToken)
            }
        }
        // '(' <op1> id ')'
        else if (currentToken.image == "(" && isUnaryOperator(lookAhead.image)) {
            nodeExpr.insertChildren(Node(currentToken))
            nextToken()
            nodeExpr.insertChildren(op1())
            if (currentToken.tokenClass == ID) {
                nodeExpr.insertChildren(Node(currentToken))
                nextToken()
                if (currentToken.image == ")") {
                    nodeExpr.insertChildren(Node(currentToken))
                    nextToken()
                } else {
                    addError(listOf(")"), currentToken)
                }
            } else {
                addError(listOf(ID.value), currentToken)
            }
        } else {
            addError(listOf("("), currentToken)
        }
        return nodeExpr
    }

    //    <op2> ::= '&&' | '||' | '>' | '>=' | '<' | '<=' | '==' | '!=' | '.' | '+' | '-' | '*' | '/'
    fun op2(): Node {
        val nodeOp2 = Node("<op2>")
        if (operators().contains(currentToken.image)) {
            nodeOp2.insertChildren(Node(currentToken))
            nextToken()
        } else {
            addError(operators(), currentToken)
        }
        return nodeOp2
    }

    //    <op1> ::= '++' | '--'
    fun op1(): Node {
        val nodeOp1 = Node("<op1>")
        if (unaryOperators().contains(currentToken.image)) {
            nodeOp1.insertChildren(Node(currentToken))
            nextToken()
        } else {
            addError(unaryOperators(), currentToken)
        }
        return nodeOp1
    }

    //    <invoca> ::= id <args>
    fun invoca(): Node {
        val nodeInvoca = Node("<invoca>")
        if (currentToken.tokenClass == ID) {
            nodeInvoca.insertChildren(Node(currentToken))
            nextToken()
            nodeInvoca.insertChildren(args())
        } else {
            addError(listOf(ID.value), currentToken)
        }
        return nodeInvoca
    }

    //    <args> ::= <expr> <args> | &
    fun args(): Node {
        val nodeArgs = Node("<args>")
        nodeArgs.insertChildren(expr())
        nodeArgs.insertChildren(args())
        return nodeArgs
    }

    //    <operan> ::= id | cli | clr | cll | cls
    fun operan(): Node {
        val nodeOperan = Node("<operan>")

        if (isLiteralConstant(currentToken.tokenClass.toString())) {
            nodeOperan.insertChildren(Node(currentToken))
            nextToken()
        } else {
            addError(listOf(literalConstants().toString()), currentToken)
        }
        return nodeOperan
    }

    //    <se> ::= 'se' <expr> '(' <comandos> ')' <senao>
    fun se(): Node {
        val nodeSe = Node("<se>")
        if (currentToken.image == "se") {
            nodeSe.insertChildren(Node(currentToken))
            nextToken()
            nodeSe.insertChildren(expr())
            if (currentToken.image == "(") {
                nodeSe.insertChildren(Node(currentToken))
                nextToken()
                nodeSe.insertChildren(comandos())
                if (currentToken.image == ")") {
                    nodeSe.insertChildren(Node(currentToken))
                    nextToken()
                    nodeSe.insertChildren(seNao())
                } else {
                    addError(listOf(")"), currentToken)
                }
            } else {
                addError(listOf("("), currentToken)
            }
        } else {
            addError(listOf("se"), currentToken)
        }
        return nodeSe
    }

    //    <senao> ::= '(' <comandos> ')' | &
    fun seNao(): Node {
        val nodeSeNao = Node("<senao>")
        if (currentToken.image == "(") {
            nodeSeNao.insertChildren(Node(currentToken))
            nextToken()
            nodeSeNao.insertChildren(comandos())
            if (currentToken.image == ")") {
                nodeSeNao.insertChildren(Node(currentToken))
                nextToken()
            } else {
                addError(listOf(")"), currentToken)
            }
        }
        return nodeSeNao
    }

    //    <leitura> ::= 'le' id
    fun leitura(): Node {
        val nodeLeitura = Node("<leitura>")
        if (currentToken.image == "le") {
            nodeLeitura.insertChildren(Node(currentToken))
            nextToken()
            if (currentToken.tokenClass == ID) {
                nodeLeitura.insertChildren(Node(currentToken))
                nextToken()
            } else {
                addError(listOf(ID.value), currentToken)
            }
        } else {
            addError(listOf("le"), currentToken)
        }
        return nodeLeitura
    }

    //    <mostrar> ::= 'mostra '<expr>
    fun mostrar(): Node {
        val nodeMostrar = Node("<mostrar>")
        if (currentToken.image == "mostra") {
            nodeMostrar.insertChildren(Node(currentToken))
            nextToken()
            nodeMostrar.insertChildren(expr())
        } else {
            addError(listOf("mostra"), currentToken)
        }
        return nodeMostrar
    }

    //    <enquanto> ::= 'enquanto' <expr> <comandos>
    fun enquanto(): Node {
        val nodeEnquanto = Node("<enquanto>")
        if (currentToken.image == "enquanto") {
            nodeEnquanto.insertChildren(expr())
            nodeEnquanto.insertChildren(comandos())
        } else {
            addError(listOf("enquanto"), currentToken)
        }
        return nodeEnquanto
    }

    //    <para> ::= 'para' '(' <atrib> ')' <expr> '(' <atrib> ')' <comandos>
    fun para(): Node {
        val nodePara = Node("<para>")
        if (currentToken.image == "para") {
            nodePara.insertChildren(Node(currentToken))
            nextToken()
            if (currentToken.image == "(") {
                nodePara.insertChildren(Node(currentToken))
                nextToken()
                nodePara.insertChildren(atrib())
                if (currentToken.image == ")") {
                    nodePara.insertChildren(Node(currentToken))
                    nextToken()
                    nodePara.insertChildren(expr())
                    if (currentToken.image == "(") {
                        nodePara.insertChildren(Node(currentToken))
                        nextToken()
                        nodePara.insertChildren(atrib())
                        if (currentToken.image == ")") {
                            nodePara.insertChildren(Node(currentToken))
                            nextToken()
                            nodePara.insertChildren(comandos())
                        } else addError(listOf(")"), currentToken)
                    } else addError(listOf("("), currentToken)
                } else addError(listOf(")"), currentToken)
            } else addError(listOf("("), currentToken)
        } else addError(listOf("para"), currentToken)

        return nodePara
    }

    //    <retorno> ::= 'ret' <expr>
    fun retorno(): Node {
        val nodeRetorno = Node("<retorno>")
        if (currentToken.image == "ret") {
            nodeRetorno.insertChildren(Node(currentToken))
            nextToken()
            nodeRetorno.insertChildren(expr())
        } else {
            addError(listOf("ret"), currentToken)
        }
        return nodeRetorno
    }

}