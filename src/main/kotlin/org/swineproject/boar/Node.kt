package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.events.MouseEvent
import org.eclipse.swt.events.MouseListener
import org.eclipse.swt.events.PaintEvent
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Event
import org.eclipse.swt.widgets.Listener

class Node(parent: BoarWidget, val name: String, var x: Int, var y: Int, val size: Int) {
    var self = this

    var colour = Display.getDefault().getSystemColor(SWT.COLOR_BLACK)!!
    // var selected = false
    // var dragging = false
    // var editing = false

    // idle, selected, dragging, editing
    var mode = "idle"

    init {
        parent.addListener(SWT.MouseMove, object : Listener {
            override fun handleEvent(event: Event) {
                // event.x > x && event.x < x + size && event.y > y && event.y < y + size
                when (mode) {
                    "idle" -> colour = if (Util.isMouseIn(event.x, event.y, x, y, size, size)) {
                        Display.getDefault().getSystemColor(SWT.COLOR_GREEN)
                    }
                    else {
                        Display.getDefault().getSystemColor(SWT.COLOR_BLACK)
                    }
                    "selected" -> mode = "dragging"
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
                            mode = "selected"
                            parent.selectedNode = self
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