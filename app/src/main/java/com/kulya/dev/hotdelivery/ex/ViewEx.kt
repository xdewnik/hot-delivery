package com.kulya.dev.hotdelivery.ex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

infix fun ViewGroup.inflate(layoutResId: Int): View =
    LayoutInflater.from(context).inflate(layoutResId, this, false)

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}
