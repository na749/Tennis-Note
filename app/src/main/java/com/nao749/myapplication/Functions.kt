package com.nao749.myapplication

import android.content.Context
import android.view.View
import android.widget.Toast

//トースト関数省力版
fun makeToast(context: Context,message : String){

    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()

}

//すご
//連続タップ防止関数
private var clickTime : Long = 0

fun <T : View>T.setOnSafeClickListener(block:(T) -> Unit){
    this.setOnClickListener { view ->
        if (System.currentTimeMillis() - clickTime < 1000){
            return@setOnClickListener
        }
        @Suppress("UNCHECKED_CAST")
        block(view as T)
        clickTime = System.currentTimeMillis()
    }
}