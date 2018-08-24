package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.events.MouseEvent
import org.eclipse.swt.events.MouseListener
import org.eclipse.swt.events.PaintEvent
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Event
import org.eclipse.swt.widgets.Listener

class Node(parent: BoarWidget, var x: Int, var y: Int, val size: Int) {
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
                if (Util.isMouseIn(event.x, event.y, x, y, size, size)) {
                    parent.editingNode = self

                    mode = "editing"
                    parent.sideBar.showSideBar(mode == "editing")
                }
            }

            override fun mouseDown(event: MouseEvent) {
                if (Util.isMouseIn(event.x, event.y, x, y, size, size)) {
                    mode = "selected"
                    parent.selectedNode = self
                }
                else {
                    mode = "idle"
                    // parent.selectedNode = null
                }

                parent.setFocus()
            }

            override fun mouseUp(event: MouseEvent) {
                if (mode == "dragging") {
                    mode = "idle"
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
            "editing" -> {
                event.gc.background = Display.getDefault().getSystemColor(SWT.COLOR_RED)
                event.gc.foreground = Display.getDefault().getSystemColor(SWT.COLOR_RED)
            }
        }

        event.gc.drawRectangle(this.x, this.y, size, size)
        event.gc.fillRectangle(this.x, this.y, size, size)

        event.gc.background = Display.getDefault().getSystemColor(SWT.COLOR_BLACK)!!
    }
}