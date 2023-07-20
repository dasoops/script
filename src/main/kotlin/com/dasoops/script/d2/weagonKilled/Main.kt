package com.dasoops.script.d2.weagonKilled

import com.dasoops.common.json.core.dataenum.IntDataEnum
import com.dasoops.common.json.core.parse
import com.dasoops.script.*
import java.awt.Robot
import java.awt.event.KeyEvent.*
import java.io.File

val config = File("/temp/working/d2/config.json")

enum class WeaponIndex(
    val checkCode: Int,
    val waitTime: Int,
    val checkAxis: Config.() -> CheckAxisItem,
    val biu: Robot.() -> Unit,
) : IntDataEnum {
    FIRST(checkCode = VK_1, waitTime = 9000, checkAxis = {
        checkAxis.first
    }, biu = {
        //虚空箭需要左移避开集结旗碰撞箱
        keyHold(VK_A, 800)
        //相对移动,虚空箭准星
        translate(0, 15)
        //虚空箭
        keyClick(VK_F)
        delay(1000)
        //枯萎囤积偏移
        translate(30, -280)
        delay(300)
        //枯萎囤积发射
        mouseClick()
    }),
    SECOND(checkCode = VK_2, waitTime = 9000, checkAxis = {
        checkAxis.second
    }, biu = {
        TODO("not implement")
    }),
    THIRD(checkCode = VK_3, waitTime = 3000, checkAxis = {
        checkAxis.third
    }, biu = {
        //狼头瞄准 + 发射
        mouseHoldRight {
            mouseClick()
        }
    }),
    ;

    override val data = ordinal
}


fun loadConfig(): Config {
    return config.readText().parse(Config::class.java)
}


fun main(): Unit = Robot().run {
    autoDelay = 0
    val config = loadConfig()
    val (weaponIndex, weaponCount) = config
    val checkAxis = config.run(weaponIndex.checkAxis)

    for (i in 0..weaponCount) {
        //移动 + 插旗
        run {
            //切出武器
            keyClick(weaponIndex.checkCode)
            //移动到插旗点,左前方
            keyHold(VK_SHIFT){
                keyHold(VK_W){
                    keyHold(VK_A, 300)
                    delay(2200)
                }
            }
            //插旗
            keyHold(VK_E, 1200)
        }

        //射击
        run(weaponIndex.biu)

        //切武器 + 自杀
        run {
            delay(300)
            //开装备栏
            keyClick(VK_F1)
            delay(500)
            //移动到当前装备
            mouseMove(checkAxis.used)
            delay(200)
            //切换
            mouseMove(checkAxis.items[i])
            delay(200)
            mouseClick()
            //等怪死
            delay(weaponIndex.waitTime)
            //换回来
            mouseClick()
            delay(500)
            //关闭
            keyClick(VK_F1)
            delay(500)
            //切3号位,偏移,自杀
            keyClick(VK_3)
            translate(0, -1500)
            delay(800)
            mouseClick()
        }
    }
}