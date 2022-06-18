package com.daurenbek.hackdayapp

import android.content.Context

class ResourceHelper(private val context: Context) {

    fun getString(resId: Int) = context.resources.getString(resId)
}