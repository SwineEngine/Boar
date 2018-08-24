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
    init {
        this.addPaintListener(object : PaintListener {
            override fun paintControl(event: PaintEvent) {
                val size = 480
                val destX = (clientArea.width / 2) - (size / 2)
                val destY = (clientArea.height / 2) - (size / 2)
                
                val image = Image(display, javaClass.classLoader.getResource("sprites/pig/idle/pig_idle_0.png").path)
                event.gc.drawImage(image, 0, 0, image.bounds.width, image.bounds.height, destX, destY, size, size)
                image.dispose()
            }
        })

        val layout = GridData(GridData.FILL_BOTH)
        this.layoutData = layout
    }
}