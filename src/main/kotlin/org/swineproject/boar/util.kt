package org.swineproject.boar

object Util {
    fun isMouseIn(mouseX: Int, mouseY: Int, posX: Int, posY: Int, sizeX: Int, sizeY: Int): Boolean {
        return mouseX > posX && mouseX < posX + sizeX && mouseY > posY && mouseY < posY + sizeY
    }
}