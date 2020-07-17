package com.nao749.myapplication.Game

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class GameDatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener  {

    var  mListener : OnDateSetListener? = null

    //Fragment生成時はonAttach,onDetachをともに記載すること
    //FragmentがActivityにくっつく
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnDateSetListener){
            mListener = context
        }else{
            throw RuntimeException(context.toString() + "must implement OnFragmentListener")
        }
    }

    //FragmentがActivityから離れる
    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnDateSetListener{
        fun onGameDateSelected(dateString: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)//年の取得
        val month = calender.get(Calendar.MONTH)//月の取得
        val day = calender.get(Calendar.DAY_OF_MONTH)//日にちの取得

        return DatePickerDialog(requireActivity(),this,year,month,day)
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {

        val dateString = getDateString(year,month,day)
        mListener!!.onGameDateSelected(dateString)

    }

    private fun getDateString(year: Int, month: Int, day: Int): String {
        val calender = Calendar.getInstance()
        calender.set(year,month,day)
        val dateFormat = SimpleDateFormat("yyyy/MM/dd") //表示形式の変換
        return  dateFormat.format(calender.time)
    }

}