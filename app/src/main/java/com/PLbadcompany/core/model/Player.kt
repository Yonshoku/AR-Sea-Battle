package com.PLbadcompany.core.model

import com.PLbadcompany.core.model.EnemyField
import com.PLbadcompany.core.model.OwnField

open class Player () {
    val ownField : OwnField = OwnField()
    val enemyField : EnemyField = EnemyField()
}