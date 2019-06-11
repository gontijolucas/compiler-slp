package com.unitri.tree

import com.unitri.table.Token

class Node {

    var token: Token? = null
    var type: String = ""
    var parent: Node? = null
    var childrens: MutableList<Node> = mutableListOf()

    constructor(type: String) {
        this.type = type
    }

    constructor(token: Token?) {
        this.token = token
    }

    constructor(token: Token?, type: String) {
        this.token = token
        this.type = type
    }


    fun insertChildren(children: Node) {
        this.childrens.add(children)
        children.parent = this
    }

    fun showTree() {
        showTree("", true)
    }

    private fun showTree(prefix: String, isTail: Boolean) {
        println(prefix + (if (isTail) "└── " else "├── ") + "type:$type - token: $token")
        for (i in 0 until childrens.size - 1) {
            childrens[i].showTree(prefix + if (isTail) "    " else "│   ", false)
        }
        if (childrens.size > 0) {
            childrens[childrens.size - 1]
                .showTree(prefix + if (isTail) "    " else "│   ", true)
        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node

        if (token != other.token) return false
        if (type != other.type) return false
        if (parent != other.parent) return false
        if (childrens != other.childrens) return false

        return true
    }

    override fun hashCode(): Int {
        var result = token?.hashCode() ?: 0
        result = 31 * result + type.hashCode()
        result = 31 * result + (parent?.hashCode() ?: 0)
        result = 31 * result + childrens.hashCode()
        return result
    }

    override fun toString(): String {
        return "Node(token=$token, type='$type', parent=$parent)"
    }


}