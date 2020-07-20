package com.nao749.myapplication.Plan

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class TimePickerFragment:DialogFragment(),TimePickerDialog.OnTimeSetListener {

    var mListener : OnPlanTimeSetListener? = null

    //Fragment生成時はonAttach,onDetachをともに記載すること
    //FragmentがActivityにくっつく
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnPlanTimeSetListener){
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


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calender = Calendar.getInstance()
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val min = calender.get(Calendar.MINUTE)

        //Dialogの返却
        return TimePickerDialog(requireContext(),this,hour,min,true)
    }

    override fun onTimeSet(timePicker: TimePicker?, hour: Int, min: Int) {

        mListener!!.onTimeSelected(hour,min)
    }


    interface OnPlanTimeSetListener{

        fun onTimeSelected(hour: Int,min: Int)

    }

}