package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.events.SelectionListener
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Group

class Hotbar (parent: Composite) {
    lateinit var boarWidget: BoarWidget

    private var group = Group(parent, SWT.NULL)

    init {
        val lowerLayout = GridLayout()
        lowerLayout.numColumns = 2
        group.layout = lowerLayout
        group.layoutData = GridData(GridData.FILL_HORIZONTAL)
        val layoutData = GridData(GridData.FILL_HORIZONTAL)
        layoutData.horizontalSpan = 4
        group.layoutData = layoutData

        // Button(group, SWT.PUSH).text = "Open Image"
        val collisionButton = Button(group, SWT.NONE)
        collisionButton.text = "Export Collision Box"

        collisionButton.addSelectionListener(object : SelectionListener {
            override fun widgetSelected(event: SelectionEvent) {
                println(Util.exportVertices(boarWidget.nodeList, boarWidget.clientArea.width, boarWidget.clientArea.height).toList())
            }

            override fun widgetDefaultSelected(event: SelectionEvent) {
                return
            }
        })
    }
}