package com.nao749.myapplication.DB

import io.realm.RealmObject

open class DataDB : RealmObject(){

    //日付
    var date : String = ""


    //今日の内容
    var today : String = ""

    //反省
    var reflection : String = ""

    //次に生かすこと
    var nextPoint : String = ""

    //試合の日程
    var gameDate : String = ""

    //試合の会場
    var gamePlace : String = ""

    //スコア
    var score : String = ""

    //試合の内容
    var gameToday : String = ""

    //試合の反省
    var gameReflection : String = ""

    //次生かすこと
    var gameNextPoint : String = ""

    //FragmentFrag
    var fragmentFrag : String = ""


}