package com.nao749.myapplication.Activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.nao749.myapplication.Game.GameDetailFragment
import com.nao749.myapplication.IntentKey
import com.nao749.myapplication.ModeFragment
import com.nao749.myapplication.ModeInEdit
import com.nao749.myapplication.Practice.PracticeDetailFragment
import com.nao749.myapplication.R

import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() ,PracticeDetailFragment.OnFragmentInteractionListener
,GameDetailFragment.OnFragmentGameInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
            setNavigationOnClickListener {
                finish()
            }
        }

        //最初にモード関係の受取
        val bundle : Bundle = intent.extras!!
        val mode : ModeInEdit = bundle.getSerializable(IntentKey.MODE.name) as ModeInEdit
        val fragmentMode : ModeFragment = bundle.getSerializable(IntentKey.MODEFRAGMENT.name) as ModeFragment

        if(fragmentMode == ModeFragment.PRACTICE){

            //MainActivityからの受取 練習
            val date : String = bundle.getString(IntentKey.DATE.name)!!
            val today: String = bundle.getString(IntentKey.TODAY.name)!!
            val reflection : String = bundle.getString(IntentKey.REFLECTION.name)!!
            val nextPoint : String = bundle.getString(IntentKey.NEXT_POINT.name)!!

            //Fragmentの生成 練習
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_detail,PracticeDetailFragment.newInstance(date, today, reflection, nextPoint, mode,fragmentMode)).commit()
        }else{

            //MainActivityから受け取り 試合
            val gameDate : String = bundle.getString(IntentKey.GAMEDATE.name)!!
            val score : String = bundle.getString(IntentKey.SCORE.name)!!
            val place : String = bundle.getString(IntentKey.GAMEPLACE.name)!!
            val gameToday : String = bundle.getString(IntentKey.GAMETODAY.name)!!
            val gameReflection : String = bundle.getString(IntentKey.GAMEREFLECTION.name)!!
            val gameNextPoint : String = bundle.getString(IntentKey.GAMENEXTPOINT.name)!!

            //Fragmentの生成 試合
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_detail,GameDetailFragment.newInstance(gameDate,score,place,gameToday,gameReflection,gameNextPoint,mode,fragmentMode)).commit()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        menu.apply {
            findItem(R.id.menu_delete).isVisible = true //削除
            findItem(R.id.menu_edit).isVisible = true   //編集
            findItem(R.id.menu_done).isVisible = false //完了
            findItem(R.id.menu_share).isVisible = false


        }
        return true
    }

    //PracticeDetailから戻ってくる
    override fun onDateDeleted() {
        finish()
    }

    //FragmentからEditActivityへ
    override fun onDateEdit(
        date: String,
        today: String,
        reflection: String,
        nextPoint: String,
        mode: ModeInEdit,
        modeFragment: ModeFragment
    ) {

        val intent = Intent(this@DetailActivity,EditActivity::class.java)
        intent.apply {
            putExtra(IntentKey.DATE.name,date)
            putExtra(IntentKey.TODAY.name,today)
            putExtra(IntentKey.REFLECTION.name,reflection)
            putExtra(IntentKey.NEXT_POINT.name,nextPoint)
            putExtra(IntentKey.MODE.name,mode)
            putExtra(IntentKey.MODEFRAGMENT.name,modeFragment)
        }
        startActivity(intent)

        finish()
    }

    //GameDetailから戻ってくる
    override fun onGameDelete() {
        finish()
    }

    override fun onGoEdit(
        gamedate: String,
        score: String,
        place: String,
        gametoday: String,
        gamereflection: String,
        gamenextPoint: String,
        mode: ModeInEdit,
        modeFragment: ModeFragment
    ) {
        val intent = Intent(this@DetailActivity,EditActivity::class.java)
        intent.apply {
            putExtra(IntentKey.GAMEDATE.name,gamedate)
            putExtra(IntentKey.SCORE.name,score)
            putExtra(IntentKey.GAMEPLACE.name,place)
            putExtra(IntentKey.GAMETODAY.name,gametoday)
            putExtra(IntentKey.GAMEREFLECTION.name,gamereflection)
            putExtra(IntentKey.GAMENEXTPOINT.name,gamenextPoint)
            putExtra(IntentKey.MODE.name,mode)
            putExtra(IntentKey.MODEFRAGMENT.name,modeFragment)
        }
        startActivity(intent)

        finish()
    }


}
