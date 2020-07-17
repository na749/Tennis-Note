package com.nao749.myapplication.Activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.nao749.myapplication.*
import com.nao749.myapplication.DB.DataDB
import com.nao749.myapplication.Game.GameFragment
import com.nao749.myapplication.Helper.MySwipeHelper
import com.nao749.myapplication.IntentKey
import com.nao749.myapplication.ModeInEdit
import com.nao749.myapplication.Practice.PracticeFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener ,PracticeFragment.OnListFragmentInteractionListener
,GameFragment.OnListFragmentInteractionListener{


    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var modefragment : ModeFragment

    var isOpen = false


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open)
        val fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close)
        val fabRClockWise = AnimationUtils.loadAnimation(this,R.anim.rotate_clockwise)
        val fabRAntiClockWise = AnimationUtils.loadAnimation(this,R.anim.rotate_anticlockwise)

        modefragment = ModeFragment.PRACTICE

        //Fragmentの生成
        supportFragmentManager.beginTransaction()
            .add(R.id.container_List,PracticeFragment.newInstance(1)).commit()



        //Fabのコード
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {

            if(isOpen){
                practice_button.startAnimation(fabClose)
                game_button.startAnimation(fabClose)
                fab.startAnimation(fabRClockWise)
                textPracticeNoteMake.startAnimation(fabClose)
                textGameMeke.startAnimation(fabClose)

                isOpen = false
            }else{

                practice_button.startAnimation(fabOpen)
                game_button.startAnimation(fabOpen)
                fab.startAnimation(fabRAntiClockWise)
                textPracticeNoteMake.startAnimation(fabOpen)
                textGameMeke.startAnimation(fabOpen)

                practice_button.isClickable
                game_button.isClickable

                isOpen = true

            }

            practice_button.setOnSafeClickListener {

                val intent = Intent(this@MainActivity,
                    EditActivity::class.java)
                intent.putExtra(IntentKey.DATE.name,"")
                intent.putExtra(IntentKey.TODAY.name,"")
                intent.putExtra(IntentKey.REFLECTION.name,"")
                intent.putExtra(IntentKey.NEXT_POINT.name,"")
                intent.putExtra(IntentKey.MODE.name, ModeInEdit.NEW)
                intent.putExtra(IntentKey.MODEFRAGMENT.name,ModeFragment.PRACTICE)
                startActivity(intent)


            }

            game_button.setOnSafeClickListener {


                val intent = Intent(this@MainActivity,EditActivity::class.java)
                intent.apply {
                    putExtra(IntentKey.GAMEDATE.name,"")
                    putExtra(IntentKey.SCORE.name,"")
                    putExtra(IntentKey.GAMEPLACE.name,"")
                    putExtra(IntentKey.GAMETODAY.name,"")
                    putExtra(IntentKey.GAMEREFLECTION.name,"")
                    putExtra(IntentKey.GAMENEXTPOINT.name,"")
                    putExtra(IntentKey.MODE.name,ModeInEdit.NEW)
                    putExtra(IntentKey.MODEFRAGMENT.name,ModeFragment.GAME)
                }
                startActivity(intent)

            }

        }



        //Drawerが閉じた、開いたを担当してくれる
        val toggle = ActionBarDrawerToggle(
            this,drawer_layout,toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //Nav_viewのアイテムリスナー
        nav_view.setNavigationItemSelectedListener(this)
    }

    //端末の戻るボタンを押したときの処理
    override fun onBackPressed() {
        //drawerが開いてるなら閉じてください
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            //開いていないなら元の処理を行ってください
            super.onBackPressed()
        }
    }

    //EditActivityから戻ってきた時の画面の更新作業
    override fun onResume() {
        super.onResume()

        updateList()
    }

    private fun updateList() {

        if (modefragment == ModeFragment.PRACTICE){
            //Fragmentの交換でリストの更新
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_List,PracticeFragment.newInstance(1)).commit()
        }else{
            //Fragmentの交換でリストの更新
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_List,GameFragment.newInstance(1)).commit()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        menu.apply {
            findItem(R.id.menu_delete).isVisible = false //削除
            findItem(R.id.menu_edit).isVisible = false   //編集
            findItem(R.id.menu_done).isVisible = false   //完了
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)

    }

    //navメニュー内のアイテムリスナー
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_practice ->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_List,PracticeFragment.newInstance(1)).commit()
                modefragment = ModeFragment.PRACTICE
            }

            R.id.nav_game ->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_List,GameFragment.newInstance(1)).commit()
                modefragment = ModeFragment.GAME
            }
        }


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    //Practiceから
    //リストの項目を押したときに行う処理(項目も持っていく)
    override fun onListItemClick(item: DataDB) {

        //編集モードでDetailActivityへ！！
        val intent = Intent(this@MainActivity,
            DetailActivity::class.java)
        intent.putExtra(IntentKey.DATE.name,item.date)
        intent.putExtra(IntentKey.TODAY.name,item.today)
        intent.putExtra(IntentKey.REFLECTION.name,item.reflection)
        intent.putExtra(IntentKey.NEXT_POINT.name,item.nextPoint)
        intent.putExtra(IntentKey.MODE.name, ModeInEdit.EDIT)
        intent.putExtra(IntentKey.MODEFRAGMENT.name,ModeFragment.PRACTICE)
        startActivity(intent)

    }

    //GameFragmentから
    //リストの項目が押されたときに行う処理
    override fun onItemClick(item: DataDB) {

        val intent = Intent(this@MainActivity,DetailActivity::class.java)
        intent.apply {
            putExtra(IntentKey.GAMEDATE.name,item.gameDate)
            putExtra(IntentKey.SCORE.name,item.score)
            putExtra(IntentKey.GAMEPLACE.name,item.gamePlace)
            putExtra(IntentKey.GAMETODAY.name,item.gameToday)
            putExtra(IntentKey.GAMEREFLECTION.name,item.gameReflection)
            putExtra(IntentKey.GAMENEXTPOINT.name,item.gameNextPoint)
            putExtra(IntentKey.MODE.name,ModeInEdit.EDIT)
            putExtra(IntentKey.MODEFRAGMENT.name,ModeFragment.GAME)
        }
        startActivity(intent)

    }

}

