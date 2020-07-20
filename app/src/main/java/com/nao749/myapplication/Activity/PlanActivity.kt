package com.nao749.myapplication.Activity

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.nao749.myapplication.DataPickerPlanFragment
import com.nao749.myapplication.PlanFragment
import com.nao749.myapplication.R
import com.nao749.myapplication.Tag

import kotlinx.android.synthetic.main.activity_plan.*
import kotlinx.android.synthetic.main.fragment_plan.*

class PlanActivity : AppCompatActivity() ,PlanFragment.OnPlanFragmentListener,DataPickerPlanFragment.OnPlanDateSetListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)
        setSupportActionBar(toolbar)
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

            setNavigationOnClickListener {
                finish()
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.container_Plan,PlanFragment()).commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main, menu)
        menu.apply {
            findItem(R.id.menu_delete).isVisible = false //削除
            findItem(R.id.menu_edit).isVisible = false   //編集
            findItem(R.id.menu_done).isVisible = false   //完了
            findItem(R.id.menu_share).isVisible = false
            findItem(R.id.menu_search).isVisible = false
            findItem(R.id.menu_sort).isVisible = false
        }

        return true
    }

    //予定からのDatePickerFragment
    override fun onDatePickerPlan() {
        DataPickerPlanFragment().show(supportFragmentManager,Tag.DATE_PICKER_PLAN.name)
    }

    //カレンダーへの登録処理
    override fun onResisterCalender() {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE,editPlan.text.toString())
            putExtra(CalendarContract.Events.EVENT_LOCATION,editPlace.text.toString())
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,editBeginText.text.toString())
        }
        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }


    }

    //DatePickerからとってきたデータのセット
    override fun onDateSelected(dateString: String) {
        editDate.setText(dateString)
    }


}
