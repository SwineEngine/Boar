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

    // var x = 0
    // var y = 0
    // var size = 0

    var colour = Display.getDefault().getSystemColor(SWT.COLOR_BLACK)!!
    var selected = false
    var dragging = false

    init {
        parent.addListener(SWT.MouseMove, object : Listener {
            override fun handleEvent(event: Event) {
                // event.x > x && event.x < x + size && event.y > y && event.y < y + size
                if (!selected && !dragging) {
                    colour = if (Util.isMouseIn(event.x, event.y, x, y, size, size)) {
                        Display.getDefault().getSystemColor(SWT.COLOR_GREEN)
                    }
                    else {
                        Display.getDefault().getSystemColor(SWT.COLOR_BLACK)
                    }
                }
                else if (selected && !dragging) {
                    dragging = true
                }
                else if (dragging) {
                    x = event.x - (size / 2)
                    y = event.y - (size / 2)
                }
            }
        })

        parent.addMouseListener(object : MouseListener {
            override fun mouseDoubleClick(e: MouseEvent?) {
                return
            }

            override fun mouseDown(event: MouseEvent) {
                if (Util.isMouseIn(event.x, event.y, x, y, size, size)) {
                    selected = true
                    parent.selectedNode = self
                }
                else {
                    selected = false
                    parent.selectedNode = null
                }
            }

            override fun mouseUp(e: MouseEvent?) {
                if (dragging) {
                    selected = false
                    dragging = false
                }
            }
        })
    }

    fun draw(event: PaintEvent) {
        // if (!dragging) {
        //     this.x = x
        //     this.y = y
        //     this.size = size
        // }

        if (!selected) {
            event.gc.background = colour
            event.gc.foreground = colour
        }
        else {
            event.gc.background = Display.getDefault().getSystemColor(SWT.COLOR_BLUE)
            event.gc.foreground = Display.getDefault().getSystemColor(SWT.COLOR_BLUE)
        }

        event.gc.drawRectangle(this.x, this.y, size, size)
        event.gc.fillRectangle(this.x, this.y, size, size)

        event.gc.background = Display.getDefault().getSystemColor(SWT.COLOR_BLACK)!!
    }
}