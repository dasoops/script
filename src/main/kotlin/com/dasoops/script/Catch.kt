package com.dasoops.script

import cn.hutool.core.swing.ScreenUtil
import com.dasoops.common.json.jackson.Jackson
import com.fasterxml.jackson.databind.SerializationFeature
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import kotlinx.coroutines.runBlocking
import java.awt.MouseInfo
import java.awt.event.KeyEvent
import java.io.File
import kotlin.system.exitProcess

fun catch(func: (eventCode: Int, unhook: () -> Unit) -> Unit) {
    var hhk: WinUser.HHOOK? = null
    val lib = User32.INSTANCE
    val hMod = Kernel32.INSTANCE.GetModuleHandle(null)
    val keyboardHook = WinUser.LowLevelKeyboardProc { nCode, wParam, info ->
        if (nCode >= 0 && wParam.toInt() == WinUser.WM_KEYDOWN) {
            func.invoke(info.vkCode) {
                lib.UnhookWindowsHookEx(hhk)
            }
        }
        lib.CallNextHookEx(hhk, nCode, wParam, WinDef.LPARAM(Pointer.nativeValue(info.pointer)))
    }
    hhk = lib.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyboardHook, hMod, 0)

    val msg = WinUser.MSG()
    lib.GetMessage(msg, null, 0, 0)
}

fun main() = runBlocking {
    val catchTarget = File("/temp/working/d2/catch.json")
    val axisList = mutableListOf<ColorAxis>()
    catch { eventCode, unHook ->
        when (eventCode) {
            KeyEvent.VK_BACK_SPACE -> {
                MouseInfo.getPointerInfo().location.run {
                    axisList.add(ColorAxis(axis, ScreenUtil.captureScreen().rgb(axis)))
                }
            }

            0x0D -> {
                val serializer = Jackson.INSTANCE.serializer.copy()
                serializer.enable(SerializationFeature.INDENT_OUTPUT)
                catchTarget.writeText(serializer.writeValueAsString(axisList))
                unHook()
                exitProcess(0)
            }
        }
    }
}