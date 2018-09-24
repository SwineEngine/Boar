package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.events.SelectionListener
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*

class TimelineWidget(parent: Composite, val boarWidget: BoarWidget) : Composite(parent, SWT.BORDER) {
    var actionWidget = Group(this, SWT.NULL)
    var framesWidget = Group(this, SWT.NULL)

    val buttonList = mutableListOf<Button>()
    var frame = 0

    init {
        var layoutData = GridData(GridData.FILL_BOTH)
        layoutData.horizontalSpan = 2
        this.layoutData = layoutData

        var gridLayout = GridLayout()
        gridLayout.numColumns = 30
        framesWidget.layout = gridLayout

        gridLayout = GridLayout()
        gridLayout.numColumns = 8
        actionWidget.layout = gridLayout

        layoutData = GridData(GridData.FILL_HORIZONTAL)
        layoutData.horizontalAlignment = SWT.CENTER
        actionWidget.layoutData = layoutData

        val lastBackwardButton = Button(actionWidget, SWT.PUSH)
        lastBackwardButton.text = "<<"

        val backwardButton = Button(actionWidget, SWT.PUSH)
        backwardButton.text = "<-"

        // val playButton = Button(actionWidget, SWT.PUSH)
        // playButton.text = "|>"

        val forwardButton = Button(actionWidget, SWT.PUSH)
        forwardButton.text = "->"

        val lastForwardButton = Button(actionWidget, SWT.PUSH)
        lastForwardButton.text = ">>"

        // val stopButton = Button(actionWidget, SWT.PUSH)
        // stopButton.text = "[]"

        // TODO: Add a separator

        // val fpsLabel = Label(actionWidget, SWT.NULL)
        // fpsLabel.text = "FPS:"

        // val fpsEntry = Spinner(actionWidget, SWT.BORDER)

        // Commands

        lastBackwardButton.addSelectionListener(object : SelectionListener {
            override fun widgetSelected(event: SelectionEvent) {
                frame = 0
                boarWidget.image = boarWidget.spriteList[frame]
                buttonList[frame].setFocus()
            }

            override fun widgetDefaultSelected(event: SelectionEvent) {
                return
            }
        })

        backwardButton.addSelectionListener(object : SelectionListener {
            override fun widgetSelected(event: SelectionEvent) {
                frame = if (frame > 0) {
                    frame - 1
                }
                else {
                    boarWidget.spriteList.size - 1
                }

                boarWidget.image = boarWidget.spriteList[frame]
                buttonList[frame].setFocus()
            }

            override fun widgetDefaultSelected(event: SelectionEvent) {
                return
            }
        })

        forwardButton.addSelectionListener(object : SelectionListener {
            override fun widgetSelected(event: SelectionEvent) {
                frame = if (frame < boarWidget.spriteList.size - 1) {
                    frame + 1
                }
                else {
                    0
                }
                
                boarWidget.image = boarWidget.spriteList[frame]
                buttonList[frame].setFocus()
            }

            override fun widgetDefaultSelected(event: SelectionEvent) {
                return
            }
        })

        lastForwardButton.addSelectionListener(object : SelectionListener {
            override fun widgetSelected(event: SelectionEvent) {
                frame = boarWidget.spriteList.size - 1
                boarWidget.image = boarWidget.spriteList[frame]
                buttonList[frame].setFocus()
            }

            override fun widgetDefaultSelected(event: SelectionEvent) {
                return
            }
        })
    }

    fun addButtons() {
        for (i in boarWidget.spriteList) {
            // TODO: Center buttons
            val button = Button(framesWidget, SWT.PUSH or SWT.CENTER)
            button.image = i
            buttonList.add(button)

            button.addSelectionListener(object : SelectionListener {
                override fun widgetSelected(event: SelectionEvent) {
                    boarWidget.image = i
                    frame = boarWidget.spriteList.indexOf(i)
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