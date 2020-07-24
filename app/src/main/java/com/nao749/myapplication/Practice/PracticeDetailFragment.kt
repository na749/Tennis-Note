package com.nao749.myapplication.Practice

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.nao749.myapplication.*
import com.nao749.myapplication.DB.DataDB
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_practice_detail.*
import java.lang.RuntimeException


private val ARG_PARAM1 =  IntentKey.DATE.name
private val ARG_PARAM2 =  IntentKey.TODAY.name
private val ARG_PARAM3 =  IntentKey.REFLECTION.name
private val ARG_PARAM4 =  IntentKey.NEXT_POINT.name
private val ARG_PARAM5 =  IntentKey.MODE.name
private val ARG_PARAM6 =  IntentKey.MODEFRAGMENT.name



class PracticeDetailFragment : Fragment() {

    private var date : String = ""
    private var today : String = ""
    private var reflection : String = ""
    private var nextPoint : String = ""
    private var mode : ModeInEdit? = null
    private var modeFragment : ModeFragment? = null

    private var mListener : OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = requireArguments().getString(ARG_PARAM1)!!
            today = requireArguments().getString(ARG_PARAM2)!!
            reflection = requireArguments().getString(ARG_PARAM3)!!
            nextPoint = requireArguments().getString(ARG_PARAM4)!!
            mode = requireArguments().getSerializable(ARG_PARAM5) as ModeInEdit
            modeFragment = requireArguments().getSerializable(ARG_PARAM6) as ModeFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_practice_detail, container, false)

        setHasOptionsMenu(true)

        return view
    }

    //持ってきた値の設定
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        txtPracriceDate.text = date
        txtPracticeToday.text = today
        txtPracticeReflection.text = reflection
        txtPracticeNext.text = nextPoint
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        menu.apply {
            findItem(R.id.menu_delete).isVisible = true //削除
            findItem(R.id.menu_edit).isVisible = true   //編集
            findItem(R.id.menu_done).isVisible = false //完了
            findItem(R.id.menu_share).isVisible = false

        }
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            //DBへの削除処理
            R.id.menu_delete -> {

                val dialog = AlertDialog.Builder(activity).apply {
                    setTitle(R.string.delete_is_ok)
                    setMessage(R.string.delete_check)
                    setPositiveButton("はい"){dialogInterface, i ->
                        deleteDB()
                        makeToast(requireActivity(),"ノートが削除されました")
                    }
                    setNegativeButton("いいえ"){dialogInterface, i ->
                        makeToast(requireActivity(),"削除が実行されませんでした")
                    }
                    show()
                }

            }

            R.id.menu_edit -> {
                //情報を保持したままEdit画面へ
                mListener?.onDateEdit(date,today,reflection,nextPoint,mode!!,modeFragment!!)
            }
            else -> super.onOptionsItemSelected(item)
        }

        return true
    }

    //リストの削除
    private fun deleteDB() {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(DataDB::class.java)
            .equalTo(DataDB::today.name,today)
            .equalTo(DataDB::reflection.name,reflection)
            .equalTo(DataDB::nextPoint.name,nextPoint)
            .findFirst()
        realm.beginTransaction()
        result!!.deleteFromRealm()
        realm.commitTransaction()
        mListener?.onDateDeleted()

        parentFragmentManager.beginTransaction().remove(this).commit()

        realm.close()
    }


    interface OnFragmentInteractionListener{

        fun onDateDeleted()

        fun onDateEdit(date: String,today: String,reflection: String,nextPoint: String,mode: ModeInEdit,modeFragment: ModeFragment)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener){
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


    companion object {
        @JvmStatic
        fun newInstance(date: String, today: String,reflection:String,nextPoint:String,mode: ModeInEdit,modeFragment: ModeFragment) =
            PracticeDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, date)
                    putString(ARG_PARAM2, today)
                    putString(ARG_PARAM3, reflection)
                    putString(ARG_PARAM4, nextPoint)
                    putSerializable(ARG_PARAM5, mode)
                    putSerializable(ARG_PARAM6,modeFragment)
                }
            }
    }
}
