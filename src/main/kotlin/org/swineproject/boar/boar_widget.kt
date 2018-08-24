package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.events.PaintEvent
import org.eclipse.swt.events.PaintListener
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.widgets.Canvas
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Display

class BoarWidget(display: Display, parent: Composite) : Canvas(parent, SWT.NONE) {
    val collisionType = "box"

    val nodeList = mutableListOf<Node>()

    var selectedNode: Node? = null

    init {
        when (collisionType) {
            "box" -> {
                for (i in 0..4) {
                    nodeList.add(Node(this))
                }
            }
        }

        this.addPaintListener(object : PaintListener {
            override fun paintControl(event: PaintEvent) {
                val size = 480
                val destX = (clientArea.width / 2) - (size / 2)
                val destY = (clientArea.height / 2) - (size / 2)

                val image = Image(display, javaClass.classLoader.getResource("sprites/pig/idle/pig_idle_0.png").path)
                event.gc.drawImage(image, 0, 0, image.bounds.width, image.bounds.height, destX, destY, size, size)
                image.dispose()

                val nodeSize = 24

                event.gc.background = display.getSystemColor(SWT.COLOR_BLACK)
                event.gc.drawRectangle(destX, destY, size, size)

                // Top Notch
                // event.gc.fillRectangle(destX + ((size - (nodeSize / 2)) / 2), destY - (nodeSize / 2), nodeSize, nodeSize)
                nodeList[0].draw(event, destX + ((size - (nodeSize / 2)) / 2), destY - (nodeSize / 2), nodeSize)

                // Bottom Notch
                // event.gc.fillRectangle(destX + ((size - (nodeSize / 2)) / 2), (destY + size) - (nodeSize / 2), nodeSize, nodeSize)
                nodeList[1].draw(event, destX + ((size - (nodeSize / 2)) / 2), (destY + size) - (nodeSize / 2), nodeSize)

                // Left Notch
                // event.gc.fillRectangle(destX - (nodeSize / 2), (destY + (size / 2)) - (nodeSize / 2), nodeSize, nodeSize)
                nodeList[2].draw(event, destX - (nodeSize / 2), (destY + (size / 2)) - (nodeSize / 2), nodeSize)

                // Right Notch
                // event.gc.fillRectangle((destX + size) - (nodeSize / 2), (destY + (size / 2)) - (nodeSize / 2), nodeSize, nodeSize)
                nodeList[3].draw(event, (destX + size) - (nodeSize / 2), (destY + (size / 2)) - (nodeSize / 2), nodeSize)
            }
        })

        val layout = GridData(GridData.FILL_BOTH)
        this.layoutData = layout
    }
}