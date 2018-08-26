package org.swineproject.boar


object Util {
    fun isMouseIn(mouseX: Int, mouseY: Int, posX: Int, posY: Int, sizeX: Int, sizeY: Int): Boolean {
        return mouseX > posX && mouseX < posX + sizeX && mouseY > posY && mouseY < posY + sizeY
    }

    fun centerPosition(x: Int, y: Int, size: Int, width: Int, height: Int): Array<Int> {
        return arrayOf(x - (size / 2) - (width / 2), y - (size / 2) - (height / 2))
    }

    fun uncenterPosition(x: Int, y: Int, size: Int, width: Int, height: Int): Array<Int> {
        return arrayOf(x - (size / 2) + (width / 2), y - (size / 2) + (height / 2))
    }

    fun exportVertices(nodeList: List<Node>, width: Int, height: Int, imageWidth: Int, imageHeight: Int, imageScale: Float): Array<Pair<Float, Float>> {
        val vertices: MutableList<Pair<Float, Float>> = mutableListOf()

        for (i in nodeList) {
            val nodeSize = i.size
            val position = Util.centerPosition(i.x, i.y, i.size, width, height)

            vertices.add(Pair((((position[0] + nodeSize) / (width / 2f)) * imageWidth) * imageScale, -(((position[1] + nodeSize) / (height / 2f)) * imageHeight) * imageScale))
        }

        return vertices.toTypedArray()
    }
}