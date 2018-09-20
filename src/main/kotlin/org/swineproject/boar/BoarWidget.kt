package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.dnd.*
import org.eclipse.swt.events.MouseEvent
import org.eclipse.swt.events.MouseListener
import org.eclipse.swt.events.PaintEvent
import org.eclipse.swt.events.PaintListener
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.widgets.*


class BoarWidget(display: Display, parent: Composite) : Canvas(parent, SWT.BORDER or SWT.H_SCROLL or SWT.V_SCROLL) {
    val self = this
    lateinit var sideBar: SideBar

    val layout = GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1)

    // val size = 480 / 2
    var width: Int = 240
    var height: Int = 240
    var image: Image? = null

    val zoom = 1

    val nodeSize = 24 / 2
    val nodeList = mutableListOf<Node>()

    var selectedNode: Node? = null
    var editingNode: Node? = null

    var firstNode: Node? = null
    var secondNode: Node? = null

    var createNodes = true

    // TODO: Measure internal angles from vertex shapes

    init {
        this.background = Display.getDefault().getSystemColor(SWT.COLOR_WHITE)

        this.addPaintListener(object : PaintListener {
            override fun paintControl(event: PaintEvent) {
                val destX = (clientArea.width / 2) - (width / 2)
                val destY = (clientArea.height / 2) - (height / 2)

                val halfSize = (nodeSize / 2)
                val pattern = listOf(listOf(destX - halfSize, destY - halfSize), listOf(destX - halfSize + width, destY - halfSize),
                        listOf(destX - halfSize + width, destY - halfSize + height), listOf(destX - halfSize, destY - halfSize + height))

                if (createNodes) {
                    createNodes = false

                    for (i in 0 until 4) {
                        val pat = pattern[i]
                        nodeList.add(Node(self, "Node ${nodeList.size + 1}", pat[0], pat[1], nodeSize))
                    }
                }

                // val image = Image(display, javaClass.classLoader.getResource("sprites/pig/idle/pig_idle_0.png").path)
                if (image != null) {
                    width = image!!.bounds.width * 15
                    height = image!!.bounds.height * 15
                    event.gc.drawImage(image, 0, 0, image!!.bounds.width, image!!.bounds.height, destX, destY, width, height)
                    // image!!.dispose()
                }

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

        this.addMouseListener(object : MouseListener {
            override fun mouseDoubleClick(event: MouseEvent) {
                val mousePosition = Display.getCurrent().focusControl.toControl(Display.getCurrent().cursorLocation)

                val dupeList = nodeList.toMutableList()

                val width = (self.clientArea.width / nodeList.size) * 1.5
                val height = (self.clientArea.height / nodeList.size) * 1.5

                firstNode = nodeList[0]
                secondNode = nodeList[0]

                for (i in nodeList) {
                    if (i in dupeList) {
                        if (-width < i.x - mousePosition.x && i.x - mousePosition.x < width && -height < i.y - mousePosition.y && i.y - mousePosition.y < height) {
                            firstNode = i
                        }
                    }
                }
                dupeList.remove(firstNode!!)

                for (i in nodeList) {
                    if (i in dupeList) {
                        if (-width < i.x - mousePosition.x && i.x - mousePosition.x < width && -height < i.y - mousePosition.y && i.y - mousePosition.y < height) {
                            secondNode = i
                        }
                    }
                }
                dupeList.remove(secondNode!!)

                nodeList.add(nodeList.indexOf(firstNode!!), Node(self, "Node ${nodeList.size + 1}", mousePosition.x, mousePosition.y, nodeSize))
            }

            override fun mouseDown(event: MouseEvent?) {
                return
            }

            override fun mouseUp(event: MouseEvent?) {
                return
            }
        })

        this.addListener(SWT.Dispose, object : Listener {
            override fun handleEvent(event: Event?) {
                if (image != null) {
                    if (!image!!.isDisposed) {
                        image!!.dispose()
                    }
                }
            }
        })

        val dropTarget = DropTarget(this, DND.DROP_COPY or DND.DROP_DEFAULT)
        val fileTransfer = FileTransfer.getInstance()
        dropTarget.setTransfer(fileTransfer)

        dropTarget.addDropListener(object : DropTargetListener {
            override fun dragLeave(event: DropTargetEvent) {
            }

            override fun drop(event: DropTargetEvent) {
                if (fileTransfer.isSupportedType(event.currentDataType)) {
                    val fileNames = event.data as Array<*>

                    for (name in fileNames) {
                        image = Image(self.display, name.toString())
                    }

                    width = image!!.bounds.width
                    height = image!!.bounds.height
                }
            }

            override fun dropAccept(event: DropTargetEvent) {
            }

            override fun dragOver(event: DropTargetEvent) {
            }

            override fun dragEnter(event: DropTargetEvent) {
                if (fileTransfer.isSupportedType(event.currentDataType)) {
                    if (event.detail != DND.DROP_COPY) {
                        event.detail = DND.DROP_COPY

                        val files = (fileTransfer.nativeToJava(event.currentDataType) as Array<String>).toList()

                        if (files.size != 1) {
                            event.detail = DND.DROP_NONE
                        }
                        else {
                            for (i in files) {
                                if (i.split(".")[1] !in listOf("png", "jpg", "jpeg")) {
                                    event.detail = DND.DROP_NONE
                                }
                            }
                        }
                    }
                }
            }

            override fun dragOperationChanged(event: DropTargetEvent?) {
                return
            }
        })

        layout.widthHint = width * 2
        layout.heightHint = height * 2
        layout.verticalSpan = 2

        this.horizontalBar.maximum = width
        this.horizontalBar.thumb = width

        this.verticalBar.maximum = height
        this.verticalBar.thumb = width

        this.layoutData = layout
    }
}