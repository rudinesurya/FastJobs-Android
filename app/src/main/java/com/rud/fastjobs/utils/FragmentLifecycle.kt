package com.rud.fastjobs.utils

/***
 * Interface meant to provide lifecycle callbacks for fragments that live in viewpager
 */
interface FragmentLifecycle {
    fun onPauseFragment()
    fun onResumeFragment()
}