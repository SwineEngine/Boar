package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.internal.win32.OS
import org.eclipse.swt.internal.win32.TCHAR
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*

class SideBar(parent: Composite) {
    lateinit var boarWidget: BoarWidget

    private var group = Group(parent, SWT.NULL)
    private var spinnerX: Spinner
    private var spinnerY: Spinner

    init {
        val groupLayout = GridLayout()
        groupLayout.numColumns = 5
        group.layout = groupLayout
        group.layoutData = GridData(GridData.FILL_BOTH)

        Label(group, SWT.NONE).text = "Position:"
        Label(group, SWT.NONE).text = "X"
        spinnerX = Spinner(group, SWT.NONE)
        spinnerX.digits = 2
        Label(group, SWT.NONE).text = "Y"
        spinnerY = Spinner(group, SWT.NONE)
        spinnerY.digits = 2
    }

    fun showSideBar(show: Boolean) {
        group.visible = show
    }

    fun update() {
        // val xField = Spinner::class.java.getDeclaredField("text")
        // xField.isAccessible = true
        // xField.set(spinnerX, boarWidget.selectedNode!!.x.toString())

        // val yField = Spinner::class.java.getField("text")
        // yField.isAccessible = true

        if (boarWidget.selectedNode != null) {
            spinnerX.setText(boarWidget.selectedNode!!.x.toString())
            spinnerY.setText(boarWidget.selectedNode!!.y.toString())
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