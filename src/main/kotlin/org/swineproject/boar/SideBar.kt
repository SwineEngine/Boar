package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.events.ModifyEvent
import org.eclipse.swt.events.ModifyListener
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.events.SelectionListener
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.internal.win32.OS
import org.eclipse.swt.internal.win32.TCHAR
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*


class SideBar(parent: Composite, val boarWidget: BoarWidget) {
    var nodeGroup = Group(parent, SWT.NULL)
    private var spinnerX: Spinner
    private var spinnerY: Spinner
    private var nameLabel: Label

    private var nodePosition: Array<Int>? = null

    var projectGroup = Group(parent, SWT.NULL)
    private var spinnerWidth: Spinner
    private var spinnerHeight: Spinner
    private var spinnerScale: Spinner

    init {
        val spinnerData = GridData(GridData.FILL_HORIZONTAL)

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

        Label(nodeGroup, SWT.NONE).text = "Position:"
        Label(nodeGroup, SWT.NONE).text = "X"
        spinnerX = Spinner(nodeGroup, SWT.BORDER)
        spinnerX.digits = 0
        spinnerX.minimum = -boarWidget.width
        spinnerX.maximum = boarWidget.height
        spinnerX.layoutData = spinnerData
        Label(nodeGroup, SWT.NONE).text = "Y"
        spinnerY = Spinner(nodeGroup, SWT.BORDER)
        spinnerY.digits = 0
        spinnerY.minimum = -boarWidget.width
        spinnerY.maximum = boarWidget.height
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

        val buttonImage = Button(projectGroup, SWT.PUSH)
        buttonImage.text = "Open Image"
        buttonImage.layoutData = fillButton

        buttonImage.addSelectionListener(object : SelectionListener {
            override fun widgetSelected(event: SelectionEvent) {
                val dialog = FileDialog(parent.shell, SWT.OPEN)
                dialog.filterExtensions = arrayOf("*.png", "*.jpg;*.jpeg")
                dialog.filterNames = arrayOf("PNG (${dialog.filterExtensions[0]})", "JPEG (${dialog.filterExtensions[1].split(";").joinToString("; ")})")
                val result = dialog.open()

                if (result != null) {
                    boarWidget.image = Image(boarWidget.display, result)
                }
            }

            override fun widgetDefaultSelected(event: SelectionEvent) {
                return
            }
        })

        Label(projectGroup, SWT.NONE).text = "Ratio:"
        Label(projectGroup, SWT.NONE).text = "Width"
        spinnerWidth = Spinner(projectGroup, SWT.BORDER)
        spinnerWidth.minimum = 1
        spinnerWidth.layoutData = spinnerData
        Label(projectGroup, SWT.NONE).text = "Height"
        spinnerHeight = Spinner(projectGroup, SWT.BORDER)
        spinnerHeight.minimum = 1
        spinnerHeight.layoutData = spinnerData

        Label(projectGroup, SWT.NONE).text = "Scale:"
        val scaleData = GridData(GridData.FILL_HORIZONTAL)
        scaleData.horizontalSpan = 4

        spinnerScale = Spinner(projectGroup, SWT.BORDER)
        spinnerScale.minimum = 1
        spinnerScale.layoutData = scaleData

        val buttonCollision = Button(projectGroup, SWT.NONE)
        buttonCollision.text = "Export Collision Box"
        buttonCollision.layoutData = fillButton

        buttonCollision.addSelectionListener(object : SelectionListener {
            override fun widgetSelected(event: SelectionEvent) {
                println(Util.exportVertices(boarWidget.nodeList, boarWidget.clientArea.width, boarWidget.clientArea.height, spinnerWidth.text.toInt(), spinnerHeight.text.toInt(), spinnerScale.text.toFloat()).toList())
            }

            override fun widgetDefaultSelected(event: SelectionEvent) {
                return
            }
        })
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