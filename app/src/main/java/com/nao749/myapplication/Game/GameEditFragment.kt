package com.nao749.myapplication.Game

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.nao749.myapplication.DB.DataDB
import com.nao749.myapplication.IntentKey
import com.nao749.myapplication.ModeInEdit

import com.nao749.myapplication.R
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_game_edit.*
import java.lang.RuntimeException
import java.text.ParseException
import java.text.SimpleDateFormat


private val ARG_PARAM1 =  IntentKey.GAMEDATE.name
private val ARG_PARAM2 =  IntentKey.SCORE.name
private val ARG_PARAM3 =  IntentKey.GAMEPLACE.name
private val ARG_PARAM4 =  IntentKey.GAMETODAY.name
private val ARG_PARAM5 =  IntentKey.GAMEREFLECTION.name
private val ARG_PARAM6 =  IntentKey.GAMENEXTPOINT.name
private val ARG_PARAM7 =  IntentKey.MODE.name


class GameEditFragment : Fragment() {
    //Activityから受け取る値を格納する変数の設定

    private var gamedate : String = ""
    private var score : String = ""
    private var place : String = ""
    private var gametoday : String = ""
    private var gamereflection : String = ""
    private var gamenextPoint : String = ""
    private var mode : ModeInEdit? = null

    private var listener : OnFragmentGameInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //変数の代入
        arguments?.let {
            gamedate = requireArguments().getString(ARG_PARAM1)!!
            score = requireArguments().getString(ARG_PARAM2)!!
            place = requireArguments().getString(ARG_PARAM3)!!
            gametoday = requireArguments().getString(ARG_PARAM4)!!
            gamereflection = requireArguments().getString(ARG_PARAM5)!!
            gamenextPoint = requireArguments().getString(ARG_PARAM6)!!
            mode = requireArguments().getSerializable(ARG_PARAM7) as ModeInEdit
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_edit, container, false)

        //Fragmentでtoolbarの操作を行う
        setHasOptionsMenu(true)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUi(mode!!)

        imageGameButton.setOnClickListener {
            //interfaceでEditActivityから呼び出してもらえる
            listener!!.onDatePick()
        }
    }

    private fun updateUi(mode: ModeInEdit) {
        when(mode){

            ModeInEdit.NEW->{
                inputGameDateText.setText("")
                inputGameScore.setText("")
                inputGamePlaceText.setText("")
                inputGameToday.setText("")
                inputGameReflection.setText("")
                inputGameNext.setText("")

            }

            ModeInEdit.EDIT->{
                inputGameDateText.setText(gamedate)
                inputGameScore.setText(score)
                inputGamePlaceText.setText(place)
                inputGameToday.setText(gametoday)
                inputGameReflection.setText(gamereflection)
                inputGameNext.setText(gamenextPoint)

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

                recordGameDB(mode!!)
            }

            else -> super.onOptionsItemSelected(item)

        }

        return true

    }

    private fun recordGameDB(mode: ModeInEdit) {

        //日付のエラー確認
        if(!inputDateCheck(inputGameDateText.text.toString())){
            inputGameDate.error = getString(R.string.error_date)
            return
        }

        when(mode){

            ModeInEdit.NEW->
                //新規登録
                addGameNewRecord()

            ModeInEdit.EDIT->
                //編集
                editGameRecord()
        }

        listener!!.onGameDateEditActivity()

        parentFragmentManager.beginTransaction().remove(this).commit()

    }

    private fun editGameRecord() {

        //編集
        val realm = Realm.getDefaultInstance()
        val result = realm.where(DataDB::class.java)
            .equalTo(DataDB::gameToday.name,gametoday)
            .equalTo(DataDB::gameNextPoint.name,gamenextPoint)
            .findFirst()
        realm.beginTransaction()

        result!!.apply {
            gameDate = inputGameDateText.text.toString()
            score = inputGameScore.text.toString()
            gamePlace = inputGamePlaceText.text.toString()
            gameToday = inputGameToday.text.toString()
            gameReflection = inputGameReflection.text.toString()
            gameNextPoint = inputGameNext.text.toString()
            fragmentFrag = "試合"
        }
        realm.commitTransaction()

        realm.close()

    }

    private fun addGameNewRecord() {
        //新規登録
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val gameRecord = realm.createObject(DataDB::class.java)
        gameRecord.apply {
            gameDate = inputGameDateText.text.toString()
            score = inputGameScore.text.toString()
            gamePlace = inputGamePlaceText.text.toString()
            gameToday = inputGameToday.text.toString()
            gameReflection = inputGameReflection.text.toString()
            gameNextPoint = inputGameNext.text.toString()
            fragmentFrag = "試合"
        }
        realm.commitTransaction()

        realm.close()
    }

    private fun inputDateCheck(inputGameDateText: String): Boolean {

        if(inputGameDateText == "")return false

        try {
            val format = SimpleDateFormat("yyyy/MM/dd")
            //日付がしっかりと正しいかの確認
            format.isLenient = false
            format.parse(inputGameDateText)
        }catch (e : ParseException){
            return false
        }
        return true

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentGameInteractionListener){
            listener = context
        }else{
            throw RuntimeException(context.toString() + "must implement OnFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentGameInteractionListener{

        fun onDatePick()

        fun onGameDateEditActivity()

    }

    companion object {

        @JvmStatic
        fun newInstance(gameDate: String,score:String,gamePlace : String, gameToday: String,gameReflection:String,gameNextPoint:String,mode: ModeInEdit) =
            GameEditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, gameDate)
                    putString(ARG_PARAM2, score)
                    putString(ARG_PARAM3, gamePlace)
                    putString(ARG_PARAM4, gameToday)
                    putString(ARG_PARAM5, gameReflection)
                    putString(ARG_PARAM6, gameNextPoint)
                    putSerializable(ARG_PARAM7,mode)

                }
            }
    }
}
