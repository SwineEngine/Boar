package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.custom.ScrolledComposite
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.events.SelectionListener
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*

class TimelineWidget(parent: Composite, val boarWidget: BoarWidget) : Composite(parent, SWT.BORDER) {
    var actionWidget = Group(this, SWT.NULL)

    var frameScroll = ScrolledComposite(this, SWT.H_SCROLL)
    var framesWidget = Group(frameScroll, SWT.NULL)

    val buttonList = mutableListOf<Button>()
    var frame = 0

    init {
        frameScroll.expandHorizontal = true
        frameScroll.expandVertical = true
        frameScroll.setMinSize(800, 90)
        frameScroll.content = framesWidget

        var layoutData = GridData(GridData.FILL_BOTH)
        frameScroll.layoutData = layoutData

        layoutData = GridData(GridData.FILL_BOTH)
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

        val playButton = Button(actionWidget, SWT.CHECK)
        playButton.text = ">"

        val stopButton = Button(actionWidget, SWT.PUSH)
        stopButton.text = "[ ]"

        val forwardButton = Button(actionWidget, SWT.PUSH)
        forwardButton.text = "->"

        val lastForwardButton = Button(actionWidget, SWT.PUSH)
        lastForwardButton.text = ">>"

        // val loopButton = Button(actionWidget, SWT.CHECK)
        // loopButton.text = "<>"

        // TODO: Add a separator

        // val fpsLabel = Label(actionWidget, SWT.NULL)
        // fpsLabel.text = "FPS:"

        // val fpsEntry = Spinner(actionWidget, SWT.BORDER)

        // Commands

        lastBackwardButton.addSelectionListener(object : SelectionListener {
            override fun widgetSelected(event: SelectionEvent) {
                if (boarWidget.spriteList.size > 0) {
                    frame = 0
                    boarWidget.image = boarWidget.spriteList[frame]
                    buttonList[frame].setFocus()
                }
            }

            override fun widgetDefaultSelected(event: SelectionEvent) {
                return
            }
        })

        backwardButton.addSelectionListener(object : SelectionListener {
            override fun widgetSelected(event: SelectionEvent) {
                if (boarWidget.spriteList.size > 0) {
                    frame = if (frame > 0) {
                        frame - 1
                    }
                    else {
                        boarWidget.spriteList.size - 1
                    }

                    boarWidget.image = boarWidget.spriteList[frame]
                    buttonList[frame].setFocus()
                }
            }

            override fun widgetDefaultSelected(event: SelectionEvent) {
                return
            }
        })

        playButton.addSelectionListener(object : SelectionListener {
            override fun widgetSelected(event: SelectionEvent) {
                if (boarWidget.spriteList.size > 0) {
                }
            }

            override fun widgetDefaultSelected(event: SelectionEvent) {
                return
            }
        })

        forwardButton.addSelectionListener(object : SelectionListener {
            override fun widgetSelected(event: SelectionEvent) {
                if (boarWidget.spriteList.size > 0) {
                    frame = if (frame < boarWidget.spriteList.size - 1) {
                        frame + 1
                    }
                    else {
                        0
                    }

                    boarWidget.image = boarWidget.spriteList[frame]
                    buttonList[frame].setFocus()
                }
            }

            override fun widgetDefaultSelected(event: SelectionEvent) {
                return
            }
        })

        lastForwardButton.addSelectionListener(object : SelectionListener {
            override fun widgetSelected(event: SelectionEvent) {
                if (boarWidget.spriteList.size > 0) {
                    frame = boarWidget.spriteList.size - 1
                    boarWidget.image = boarWidget.spriteList[frame]
                    buttonList[frame].setFocus()
                }
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
            button.image = Image(display, i.imageData.scaledTo(64, 64))
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