package com.prava.arka.common.resource

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes

/**
 * Created by Arka Prava Basu<arkaprava94@gmail.com> on 2019-08-04
 **/
interface IResourceManager {
    fun getString(@StringRes id: Int): String

    fun getString(@StringRes id: Int, vararg obj: Any): String

    fun getDimensionPixelOffset(@DimenRes dimen: Int): Int

    @ColorInt
    fun getColor(@ColorRes colorRes: Int): Int
}
