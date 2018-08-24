package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Button


fun main(args: Array<String>) {
    val display = Display()
    val shell = Shell(display)

    val layout = GridLayout()
    layout.numColumns = 2
    shell.layout = layout

    val boarWidget = BoarWidget(display, shell)
    // Button(shell, SWT.PUSH).text = "Button"

    shell.pack()
    shell.open()

    var count = 60

    while (!shell.isDisposed) {
        count -= 1

        if (count == 0) {
            count = 60
            boarWidget.redraw()
        }

        if (!display.readAndDispatch()) {
            display.sleep()
        }
    }
    display.dispose()
}