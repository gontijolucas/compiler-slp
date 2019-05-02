package com.unitri.tree

import com.unitri.table.Token

data class Node(
    val token: Token,
    var parent: Node,
    val childrens: MutableList<Node>
) {

    fun insertChildren(children: Node) {
        this.childrens.add(children)
        children.parent = this
    }

    fun showNodeTree() {
        mostraNo(this, "")
    }

    private fun mostraNo(no: Node, space: String) {
        println(space + no)
        for (children in no.childrens) {
            mostraNo(children, "$space   ")
        }
    }

}