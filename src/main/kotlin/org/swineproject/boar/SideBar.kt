package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.events.ModifyEvent
import org.eclipse.swt.events.ModifyListener
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.events.SelectionListener
import org.eclipse.swt.internal.win32.OS
import org.eclipse.swt.internal.win32.TCHAR
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*

class SideBar(parent: Composite, val boarWidget: BoarWidget) {
    // lateinit var boarWidget: BoarWidget

    var nodeGroup = Group(parent, SWT.NULL)
    private var spinnerX: Spinner
    private var spinnerY: Spinner
    private var nameLabel: Label

    private var nodePosition: Array<Int>? = null

    var projectGroup = Group(parent, SWT.NULL)
    private var textWidth: Text
    private var textHeight: Text

    init {
        // Node Group
        
        val nodeGroupLayout = GridLayout()
        nodeGroupLayout.numColumns = 5
        nodeGroup.layout = nodeGroupLayout
        val nodeGroupData = GridData(GridData.FILL_BOTH)
        nodeGroupData.verticalSpan = 1
        nodeGroup.layoutData = nodeGroupData

        Label(nodeGroup, SWT.NONE).text = "Selected:"
        nameLabel = Label(nodeGroup, SWT.NONE)
        nameLabel.text = "None"
        val nameData = GridData(GridData.FILL_HORIZONTAL)
        nameData.horizontalSpan = 4
        nameLabel.layoutData = nameData

        val spinnerData = GridData(GridData.FILL_HORIZONTAL)

        Label(nodeGroup, SWT.NONE).text = "Position:"
        Label(nodeGroup, SWT.NONE).text = "X"
        spinnerX = Spinner(nodeGroup, SWT.BORDER)
        spinnerX.digits = 0
        spinnerX.minimum = -boarWidget.size
        spinnerX.maximum = boarWidget.size
        spinnerX.layoutData = spinnerData
        Label(nodeGroup, SWT.NONE).text = "Y"
        spinnerY = Spinner(nodeGroup, SWT.BORDER)
        spinnerY.digits = 0
        spinnerY.minimum = -boarWidget.size
        spinnerY.maximum = boarWidget.size
        spinnerY.layoutData = spinnerData

        spinnerX.addModifyListener(object : ModifyListener {
            override fun modifyText(event: ModifyEvent) {
                if (boarWidget.selectedNode != null) {
                    boarWidget.selectedNode!!.x = spinnerX.text.toInt() + (boarWidget.clientArea.width / 2)
                }
            }
        })

        spinnerY.addModifyListener(object : ModifyListener {
            override fun modifyText(event: ModifyEvent) {
                if (boarWidget.selectedNode != null) {
                    boarWidget.selectedNode!!.y = spinnerY.text.toInt() + (boarWidget.clientArea.height / 2)
                }
            }
        })
        
        // Project Group
        val fillButton = GridData()
        fillButton.horizontalAlignment = SWT.CENTER
        fillButton.horizontalSpan = 5
        
        val projectGroupLayout = GridLayout()
        projectGroupLayout.numColumns = 5
        projectGroup.layout = projectGroupLayout
        val projectGroupData = GridData(GridData.FILL_BOTH)
        projectGroupData.verticalSpan = 1
        projectGroup.layoutData = projectGroupData

        Label(projectGroup, SWT.NONE).text = "Ratio:"
        Label(projectGroup, SWT.NONE).text = "Width"
        textWidth = Text(projectGroup, SWT.BORDER or SWT.SINGLE)
        Label(projectGroup, SWT.NONE).text = "Height"
        textHeight = Text(projectGroup, SWT.BORDER or SWT.SINGLE)

        Label(projectGroup, SWT.NONE).text = "Scale:"
        val scaleData = GridData(GridData.FILL_HORIZONTAL)
        scaleData.horizontalSpan = 4
        val textScale = Text(projectGroup, SWT.BORDER or SWT.SINGLE)
        textScale.layoutData = scaleData
    }

    fun update() {
        if (boarWidget.selectedNode != null) {
            nameLabel.text = boarWidget.selectedNode!!.name

            nodePosition = Util.centerPosition(boarWidget.selectedNode!!.x, boarWidget.selectedNode!!.y, 0, boarWidget.clientArea.width, boarWidget.clientArea.height)

            if (!spinnerX.isFocusControl) {
                spinnerX.setText(nodePosition!![0].toString())
            }

            if (!spinnerY.isFocusControl) {
                spinnerY.setText(nodePosition!![1].toString())
            }
        }
    }

    fun Spinner.setText(value: String) {
        val checkWidget = Widget::class.java.getDeclaredMethod("checkWidget")
        checkWidget.isAccessible = true
        val getCodePage = Control::class.java.getDeclaredMethod("getCodePage")
        getCodePage.isAccessible = true

        val hwndText = Spinner::class.java.getDeclaredField("hwndText")
        hwndText.isAccessible = true

        checkWidget.invoke(this)
        val buffer = TCHAR(getCodePage.invoke(this).toString().toInt(), value, true)

        OS.SetWindowText(hwndText.getLong(this), buffer)
    }
}