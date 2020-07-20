package com.nao749.myapplication

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class DataPickerPlanFragment: DialogFragment(),DatePickerDialog.OnDateSetListener {

    var listener : OnPlanDateSetListener? = null

    //Fragment生成時はonAttach,onDetachをともに記載すること
    //FragmentがActivityにくっつく
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnPlanDateSetListener){
            listener = context
        }else{
            throw RuntimeException(context.toString() + "must implement OnFragmentListener")
        }
    }

    //FragmentがActivityから離れる
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnPlanDateSetListener{

        fun onDateSelected(dateString : String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)//年の取得
        val month = calender.get(Calendar.MONTH)//月の取得
        val day = calender.get(Calendar.DAY_OF_MONTH)//日にちの取得

        //Dialogを返す
        return DatePickerDialog(requireActivity(),this,year,month,day)

    }

    //数字を選んだ時に返すコールバックメソッド（テキストに日付を入れたい！！）
    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        val dateString = getDateString(year,month,day)
        listener!!.onDateSelected(dateString)

    }

    private fun getDateString(year: Int, month: Int, day: Int): String {
        val calender = Calendar.getInstance()
        calender.set(year,month,day)
        val dateFormat = SimpleDateFormat("yyyy/MM/dd") //表示形式の変換
        return  dateFormat.format(calender.time)
    }

}