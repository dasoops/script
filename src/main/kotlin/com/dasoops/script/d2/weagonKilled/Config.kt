package com.dasoops.script.d2.weagonKilled

import com.dasoops.script.Axis

data class Config(
    val weaponIndex: WeaponIndex,
    val weaponCount: Int,
    val checkAxis: CheckAxis,
)

data class CheckAxis(
    val first: CheckAxisItem,
    val second: CheckAxisItem,
    val third: CheckAxisItem,
)

data class CheckAxisItem(
    val used: Axis,
    val items: List<Axis>
)