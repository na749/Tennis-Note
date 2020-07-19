package com.nao749.myapplication.Activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class PageAdapter(fragmentManager: FragmentManager, private val fragmentList:List<Fragment>):
        FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    //表示するフラグメントの制御
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    //viewPagerにセットするコンテンツのサイズ
    override fun getCount(): Int {
        return fragmentList.size
    }

}