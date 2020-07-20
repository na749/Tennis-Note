package com.nao749.myapplication.Activity

import android.os.Bundle
import android.view.Menu
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.nao749.myapplication.*
import com.nao749.myapplication.Game.GameDatePickerFragment
import com.nao749.myapplication.Game.GameEditFragment
import com.nao749.myapplication.Practice.*

import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit.toolbar
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_game_edit.*
import kotlinx.android.synthetic.main.fragment_practice_result.*

class EditActivity : AppCompatActivity() ,PracticeEditFragment.OnFragmentInteractionListener,
    DatePickerFragment.OnDateSetListener,GameEditFragment.OnFragmentGameInteractionListener,
    GameDatePickerFragment.OnDateSetListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(toolbar)
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
            setNavigationOnClickListener {
                finish()
            }
        }

        //モード関係を先に受け取る
        val bundle : Bundle = intent.extras!!
        val mode : ModeInEdit = bundle.getSerializable(
            IntentKey.MODE.name) as ModeInEdit
        val modeFragment : ModeFragment = bundle.getSerializable(IntentKey.MODEFRAGMENT.name) as ModeFragment

        if(modeFragment == ModeFragment.PRACTICE){
            //練習でのインテントの受取
            val date : String? = bundle.getString(IntentKey.DATE.name)
            val today: String? = bundle.getString(IntentKey.TODAY.name)
            val reflection : String? = bundle.getString(IntentKey.REFLECTION.name)
            val nextPoint : String? = bundle.getString(IntentKey.NEXT_POINT.name)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_edit,PracticeEditFragment.newInstance(date!!, today!!, reflection!!, nextPoint!!, mode)).commit()
        }else{
            //試合関係の情報を受け取り
            val gameDate : String? = bundle.getString(IntentKey.GAMEDATE.name)
            val score : String? = bundle.getString(IntentKey.SCORE.name)
            val place : String? = bundle.getString(IntentKey.GAMEPLACE.name)
            val gameToday : String? = bundle.getString(IntentKey.GAMETODAY.name)
            val gameReflection : String? = bundle.getString(IntentKey.GAMEREFLECTION.name)
            val gameNextPoint : String? = bundle.getString(IntentKey.GAMENEXTPOINT.name)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_edit, GameEditFragment.newInstance(gameDate!!,score!!,place!!,gameToday!!,gameReflection!!,gameNextPoint!!,mode)).commit()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        menu.apply {
            findItem(R.id.menu_delete).isVisible = false //削除
            findItem(R.id.menu_edit).isVisible = false   //編集
            findItem(R.id.menu_done).isVisible = true   //完了
            findItem(R.id.menu_share).isVisible = false
            findItem(R.id.menu_search).isVisible = false
            findItem(R.id.menu_sort).isVisible = false
        }
        return true
    }


    //PracticeEditFragmentからのコールバックメソッド
    override fun onDataEditActivity() {
        finish()
    }

    //Practiceから
    //DatePickerの呼び出しメソッド
    override fun onDatePicker() {

        DatePickerFragment()
            .show(supportFragmentManager, Tag.DATE_PICKER_PRACTICE.name)

    }

    //Gameから
    //DatePickerの呼び出し
    override fun onDatePick() {
        GameDatePickerFragment()
            .show(supportFragmentManager,Tag.DATE_PICKER_GAME.name)
    }

    //GameEditFragmentからの呼び出しメソッド
    override fun onGameDateEditActivity() {
        finish()
    }


    //PracticeDatePickerからテキストへの書き込み
    override fun onDateSelected(dateString: String) {
        inputTextDate.setText(dateString)
    }

    //GameDatePickerからテキストへの書き込み
    override fun onGameDateSelected(dateString: String) {
        inputGameDateText.setText(dateString)
    }


}
