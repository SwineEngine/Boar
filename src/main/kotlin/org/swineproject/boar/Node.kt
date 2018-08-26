package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.events.MouseEvent
import org.eclipse.swt.events.MouseListener
import org.eclipse.swt.events.PaintEvent
import org.eclipse.swt.graphics.Cursor
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Event
import org.eclipse.swt.widgets.Listener


class Node(val parent: BoarWidget, val name: String, var x: Int, var y: Int, val size: Int) {
    var self = this

    val cursorArrow = Cursor(parent.display, SWT.CURSOR_ARROW)
    val cursorHand = Cursor(parent.display, SWT.CURSOR_HAND)
    val cursorMove = Cursor(parent.display, SWT.CURSOR_SIZEALL)

    var colour = Display.getDefault().getSystemColor(SWT.COLOR_BLACK)!!

    // idle, selected, dragging, editing
    var mode = "idle"

    init {
        parent.addListener(SWT.MouseMove, object : Listener {
            override fun handleEvent(event: Event) {
                // event.x > x && event.x < x + size && event.y > y && event.y < y + size
                when (mode) {
                    "idle" -> {
                        if (Util.isMouseIn(event.x, event.y, x, y, size, size)) {
                            colour = Display.getDefault().getSystemColor(SWT.COLOR_GREEN)

                            // parent.cursor = cursorHand
                        }
                        else {
                            colour = Display.getDefault().getSystemColor(SWT.COLOR_BLACK)

                            // if (parent.cursor != cursorMove) {
                            //     parent.cursor = cursorArrow
                            // }
                        }
                    }
                    "dragging" -> {
                        x = event.x - (size / 2)
                        y = event.y - (size / 2)
                    }
                }
            }
        })

        parent.addMouseListener(object : MouseListener {
            override fun mouseDoubleClick(event: MouseEvent) {
                return
            }

            override fun mouseDown(event: MouseEvent) {
                when (event.button) {
                    1 -> {
                        if (Util.isMouseIn(event.x, event.y, x, y, size, size)) {
                            mode = "dragging"
                            parent.selectedNode = self

                            parent.cursor = cursorMove
                        }
                    }
                    3 -> {
                        if (Util.isMouseIn(event.x, event.y, x, y, size, size)) {
                            parent.nodeList.remove(self)
                        }
                    }
                }

                parent.setFocus()
            }

            override fun mouseUp(event: MouseEvent) {
                mode = "idle"

                parent.cursor = cursorArrow
            }
        })

        this.parent.addListener(SWT.Dispose, object : Listener {
            override fun handleEvent(event: Event?) {
                if (!cursorArrow.isDisposed) {
                    cursorArrow.dispose()
                }

                if (!cursorHand.isDisposed) {
                    cursorHand.dispose()
                }

                if (!cursorMove.isDisposed) {
                    cursorMove.dispose()
                }

                if (!colour.isDisposed) {
                    colour.dispose()
                }
            }
        })
    }

    fun draw(event: PaintEvent) {
        when (mode) {
            "idle" -> {
                event.gc.background = colour
                event.gc.foreground = colour
            }
            "dragging" -> {
                event.gc.background = Display.getDefault().getSystemColor(SWT.COLOR_BLUE)
                event.gc.foreground = Display.getDefault().getSystemColor(SWT.COLOR_BLUE)
            }
        }

        event.gc.drawRectangle(this.x, this.y, size, size)
        event.gc.fillRectangle(this.x, this.y, size, size)

        event.gc.background = Display.getDefault().getSystemColor(SWT.COLOR_BLACK)!!
    }
}