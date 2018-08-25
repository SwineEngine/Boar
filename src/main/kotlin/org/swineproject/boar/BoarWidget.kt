package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.events.MouseEvent
import org.eclipse.swt.events.MouseListener
import org.eclipse.swt.events.PaintEvent
import org.eclipse.swt.events.PaintListener
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.widgets.Canvas
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Display
import kotlin.math.max
import kotlin.math.min

class BoarWidget(display: Display, parent: Composite) : Canvas(parent, SWT.BORDER) {
    val self = this
    lateinit var sideBar: SideBar

    val size = 480
    val collisionType = "polygon"

    val nodeSize = 24
    val nodeList = mutableListOf<Node>()

    var selectedNode: Node? = null
    var editingNode: Node? = null

    var createNodes = true

    init {
        this.addPaintListener(object : PaintListener {
            override fun paintControl(event: PaintEvent) {
                val destX = (clientArea.width / 2) - (size / 2)
                val destY = (clientArea.height / 2) - (size / 2)

                val halfSize = (nodeSize / 2)
                val pattern = listOf(listOf(destX - halfSize, destY - halfSize), listOf(destX - halfSize + size, destY - halfSize),
                        listOf(destX - halfSize + size, destY - halfSize + size), listOf(destX - halfSize, destY - halfSize + size))

                if (createNodes) {
                    createNodes = false

                    for (i in 0 until 4) {
                        val pat = pattern[i]
                        nodeList.add(Node(self, "Node ${nodeList.size + 1}", pat[0], pat[1], nodeSize))
                    }
                }

                val image = Image(display, javaClass.classLoader.getResource("sprites/pig/idle/pig_idle_0.png").path)
                event.gc.drawImage(image, 0, 0, image.bounds.width, image.bounds.height, destX, destY, size, size)
                image.dispose()

                event.gc.background = display.getSystemColor(SWT.COLOR_BLACK)
                // event.gc.drawRectangle(destX, destY, size, size)

                for (i in 0 until nodeList.size) {
                    nodeList[i].draw(event)

                    if (i != nodeList.size - 1) {
                        val x1 = nodeList[i].x + halfSize
                        val y1 = nodeList[i].y + halfSize
                        val x2 = nodeList[i + 1].x + halfSize
                        val y2 = nodeList[i + 1].y + halfSize

                        event.gc.drawLine(x1, y1, x2, y2)
                    }
                    else {
                        val x1 = nodeList[i].x + halfSize
                        val y1 = nodeList[i].y + halfSize
                        val x2 = nodeList[0].x + halfSize
                        val y2 = nodeList[0].y + halfSize

                        event.gc.drawLine(x1, y1, x2, y2)
                    }
                }
            }
        })

        val layout = GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1)
        layout.widthHint = size * 2
        layout.heightHint = size * 2
        layout.verticalSpan = 2

        this.layoutData = layout
    }
}