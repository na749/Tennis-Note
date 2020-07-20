package com.nao749.myapplication.Plan

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.nao749.myapplication.R
import kotlinx.android.synthetic.main.fragment_plan.*


class PlanFragment : Fragment() {

    private var listener : OnPlanFragmentListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_plan, container, false)

        setHasOptionsMenu(true)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        imageBtnDate.setOnClickListener {
            listener!!.onDatePickerPlan()
        }

        imageBeginBtn.setOnClickListener {
            listener!!.onTimePickerPlan()
        }

        buttonRegister.setOnClickListener {
            listener!!.onResisterCalender()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        menu.apply {
            findItem(R.id.menu_delete).isVisible = false //削除
            findItem(R.id.menu_edit).isVisible = false   //編集
            findItem(R.id.menu_done).isVisible = true   //完了
            findItem(R.id.menu_share).isVisible = false  //シェア
            findItem(R.id.menu_search).isVisible = false //検索
            findItem(R.id.menu_sort).isVisible = false   //並べ替え
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.menu_done -> {



            }

            else -> {
                super.onOptionsItemSelected(item)
            }

        }

        return true
    }

    //Fragment生成時はonAttach,onDetachをともに記載すること
    //FragmentがActivityにくっつく
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnPlanFragmentListener){
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


    interface OnPlanFragmentListener{

        fun onDatePickerPlan()


        fun onResisterCalender()


        fun onTimePickerPlan()
    }


}
