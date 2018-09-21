package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.events.SelectionListener
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Composite

class TimelineWidget(parent: Composite, val boarWidget: BoarWidget) : Composite(parent, SWT.BORDER) {
    init {
        val layoutData = GridData(GridData.FILL_BOTH)
        layoutData.horizontalSpan = 2
        this.layoutData = layoutData
    }

    fun addButtons() {
        for (i in boarWidget.spriteList) {
            // TODO: Center buttons
            val button = Button(this, SWT.PUSH or SWT.CENTER)
            button.image = i

            button.addSelectionListener(object : SelectionListener {
                override fun widgetSelected(event: SelectionEvent) {
                    boarWidget.image = i
                }

                override fun widgetDefaultSelected(event: SelectionEvent) {
                    return
                }
            })

            if (i == boarWidget.spriteList[0]) {
                button.setFocus()
            }
        }

        this.layout()
    }
}