package com.nao749.myapplication.Practice

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.nao749.myapplication.DB.DataDB
import com.nao749.myapplication.IntentKey
import com.nao749.myapplication.ModeInEdit
import com.nao749.myapplication.R
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_practice_result.*
import java.lang.RuntimeException
import java.text.ParseException
import java.text.SimpleDateFormat

//Activityから渡されるインテントのキーの修正
private val ARG_PARAM1 =  IntentKey.DATE.name
private val ARG_PARAM2 =  IntentKey.TODAY.name
private val ARG_PARAM3 =  IntentKey.REFLECTION.name
private val ARG_PARAM4 =  IntentKey.NEXT_POINT.name
private val ARG_PARAM5 =  IntentKey.MODE.name



class PracticeEditFragment : Fragment() {

    //Activityから受け取る値を格納する変数の設定
    private var date : String = ""
    private var today : String = ""
    private var reflection : String = ""
    private var nextPoint : String = ""
    private var mode : ModeInEdit? = null

    private var mListener : OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //変数への値の代入
        arguments?.let {
            date = requireArguments().getString(ARG_PARAM1)!!
            today = requireArguments().getString(ARG_PARAM2)!!
            reflection = requireArguments().getString(ARG_PARAM3)!!
            nextPoint = requireArguments().getString(ARG_PARAM4)!!
            mode = requireArguments().getSerializable(ARG_PARAM5) as ModeInEdit
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_practice_result, container, false)
        //Fragmentでオプションメニューの操作を行う
        setHasOptionsMenu(true)

        return view
    }

    //画面の更新を行う
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUi(mode!!)

        imageButton.setOnClickListener {
            //インターフェースでEditActivityから呼び出ししてもらう
            mListener!!.onDatePicker()
        }
    }

    private fun updateUi(mode: ModeInEdit) {
        when(mode){

            ModeInEdit.NEW -> {
                //新規登録の場合は画面に何もなし
                inputTextDate.setText("")
                inputTextToday.setText("")
                inputTextReflection.setText("")
                inputTextNext.setText("")
            }

            ModeInEdit.EDIT -> {
                //編集の場合は画面に内容を引き継ぐ
                inputTextDate.setText(date)
                inputTextToday.setText(today)
                inputTextReflection.setText(reflection)
                inputTextNext.setText(nextPoint)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        menu.apply {
            findItem(R.id.menu_delete).isVisible = false //削除
            findItem(R.id.menu_edit).isVisible = false   //編集
            findItem(R.id.menu_done).isVisible = true   //完了
        }
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            //DBへの登録処理
            R.id.menu_done -> {

                recordDB(mode!!)
            }

            else -> super.onOptionsItemSelected(item)

        }

        return true

    }

    //データベースへの登録
    private fun recordDB(mode: ModeInEdit) {

        //日付のエラー確認
        if(!inputDateCheck(inputTextDate.text.toString())){
            inputDate.error = getString(R.string.error_date)
            return
        }

        when(mode){

            ModeInEdit.NEW->
                //新規登録
                addNewRecord()

            ModeInEdit.EDIT->
                //編集
                editRecord()
        }

        //インターフェースの呼び出し
        mListener!!.onDataEditActivity()

        //使い終わったから離れる
        parentFragmentManager.beginTransaction().remove(this).commit()

    }

    //日付エラーの確認
    private fun inputDateCheck(inputDate: String): Boolean {

        if(inputDate == "")return false

        try {
            val format = SimpleDateFormat("yyyy/MM/dd")
            //日付がしっかりと正しいかの確認
            format.isLenient = false
            format.parse(inputDate)
        }catch (e : ParseException){
            return false
        }
        return true
    }

    private fun editRecord() {
        //編集

        val realm = Realm.getDefaultInstance()
        val result = realm.where(DataDB::class.java)
            .equalTo(DataDB::today.name,today)
            .equalTo(DataDB::reflection.name,reflection)
            .equalTo(DataDB::nextPoint.name,nextPoint)
            .findFirst()

        realm.beginTransaction()
        result!!.apply {
            date = inputTextDate.text.toString()
            today = inputTextToday.text.toString()
            reflection = inputTextReflection.text.toString()
            nextPoint = inputTextNext.text.toString()
            fragmentFrag = "練習"
        }
        realm.commitTransaction()

        realm.close()

    }

    private fun addNewRecord() {
        //新規登録
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val practiceRecord = realm.createObject(DataDB::class.java)
        practiceRecord.apply {
            date = inputTextDate.text.toString()
            today = inputTextToday.text.toString()
            reflection = inputTextReflection.text.toString()
            nextPoint = inputTextNext.text.toString()
            fragmentFrag = "練習"
        }
        realm.commitTransaction()

        realm.close()
    }

    //Fragment生成時はonAttach,onDetachをともに記載すること
    //FragmentがActivityにくっつく
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


    interface OnFragmentInteractionListener{
        //登録が終わったら呼び出される
        fun onDataEditActivity()

        //Fragment内のカレンダーボタンが押されたらEditActivityに呼び出してもらう
        fun onDatePicker()
    }


    //Fragment生成時に金庫に保管する情報
    //ファクトリーメソッドの修正
    companion object {
        @JvmStatic
        fun newInstance(date: String, today: String,reflection:String,nextPoint:String,mode: ModeInEdit) =
            PracticeEditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, date)
                    putString(ARG_PARAM2, today)
                    putString(ARG_PARAM3, reflection)
                    putString(ARG_PARAM4, nextPoint)
                    putSerializable(ARG_PARAM5, mode)
                }
            }
    }
}
