package org.swineproject.boar

object Util {
    fun isMouseIn(mouseX: Int, mouseY: Int, posX: Int, posY: Int, sizeX: Int, sizeY: Int): Boolean {
        return mouseX > posX && mouseX < posX + sizeX && mouseY > posY && mouseY < posY + sizeY
    }

    fun centerPosition(x: Int, y: Int, width: Int, height: Int): Array<Int> {
        return arrayOf(x - (width / 2), y - (height / 2))
    }

    fun uncenterPosition(x: Int, y: Int, width: Int, height: Int): Array<Int> {
        return arrayOf(x + (width / 2), y + (height / 2))
    }
}