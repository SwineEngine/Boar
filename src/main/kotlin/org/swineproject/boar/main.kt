package org.swineproject.boar

import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.layout.GridLayout


fun main(args: Array<String>) {
    val display = Display()
    val shell = Shell(display)

    val layout = GridLayout()
    layout.numColumns = 2
    shell.layout = layout

    val hotbar = Hotbar(shell)

    val boarWidget = BoarWidget(display, shell)
    hotbar.boarWidget = boarWidget

    val sideBar = SideBar(shell, boarWidget)
    // sideBar.boarWidget = boarWidget
    boarWidget.sideBar = sideBar

    boarWidget.setFocus()

    shell.pack()
    shell.open()

    val delay = 24

    val timer = object : Runnable {
        override fun run() {
            if (!boarWidget.isDisposed) {
                boarWidget.redraw()
            }

            if (!sideBar.nodeGroup.isDisposed) {
                sideBar.update()
            }

            display.timerExec(delay, this)
        }
    }

    display.timerExec(delay, timer)

    while (!shell.isDisposed) {
        if (!display.readAndDispatch()) {
            display.sleep()
        }
    }
    display.dispose()
}