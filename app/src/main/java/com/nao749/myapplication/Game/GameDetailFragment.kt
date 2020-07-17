package com.nao749.myapplication.Game

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.nao749.myapplication.*
import com.nao749.myapplication.DB.DataDB

import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_game_detail.*
import java.lang.RuntimeException

private val ARG_PARAM1 =  IntentKey.GAMEDATE.name
private val ARG_PARAM2 =  IntentKey.SCORE.name
private val ARG_PARAM3 =  IntentKey.GAMEPLACE.name
private val ARG_PARAM4 =  IntentKey.GAMETODAY.name
private val ARG_PARAM5 =  IntentKey.GAMEREFLECTION.name
private val ARG_PARAM6 =  IntentKey.GAMENEXTPOINT.name
private val ARG_PARAM7 =  IntentKey.MODE.name
private val ARG_PARAM8 =  IntentKey.MODEFRAGMENT.name


class GameDetailFragment : Fragment() {
    private var gamedate : String = ""
    private var score : String = ""
    private var place : String = ""
    private var gametoday : String = ""
    private var gamereflection : String = ""
    private var gamenextPoint : String = ""
    private var mode : ModeInEdit? = null
    private var modeFragment : ModeFragment? = null

    private var listener : OnFragmentGameInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gamedate = requireArguments().getString(ARG_PARAM1)!!
            score = requireArguments().getString(ARG_PARAM2)!!
            place = requireArguments().getString(ARG_PARAM3)!!
            gametoday = requireArguments().getString(ARG_PARAM4)!!
            gamereflection = requireArguments().getString(ARG_PARAM5)!!
            gamenextPoint = requireArguments().getString(ARG_PARAM6)!!
            mode = requireArguments().getSerializable(ARG_PARAM7) as ModeInEdit
            modeFragment = requireArguments().getSerializable(ARG_PARAM8) as ModeFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_detail, container, false)

        //fragmentでオプションメニューを操作するなら必要！！
        setHasOptionsMenu(true)

        return view
    }

    //値の設定
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        txtGameDate.text = gamedate
        txtGameScore.text = score
        txtGamePlace.text = place
        txtGameToday.text = gametoday
        txtGameReflection.text = gamereflection
        txtGameNext.text = gamenextPoint
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.apply {
            findItem(R.id.menu_delete).isVisible = true //削除
            findItem(R.id.menu_edit).isVisible = true   //編集
            findItem(R.id.menu_done).isVisible = false //完了
        }
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.menu_delete->{
                val dialog = AlertDialog.Builder(activity).apply {
                    setTitle("削除")
                    setMessage("削除してもよろしいですか？")
                    setPositiveButton("はい"){dialogInterface, i ->
                        deleteDB()
                        makeToast(requireContext(),"ノートが削除されました")
                    }
                    setNegativeButton("いいえ"){dialogInterface, i ->
                        makeToast(requireContext(),"削除が実行されませんでした")
                    }
                    show()
                }
            }

            R.id.menu_edit->{

                listener!!.onGoEdit(gamedate,score,place,gametoday,gamereflection,gamenextPoint,mode!!,modeFragment!!)

            }

            else -> return super.onOptionsItemSelected(item)

        }

        return true
    }

    private fun deleteDB() {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(DataDB::class.java)
            .equalTo(DataDB::gameToday.name,gametoday)
            .equalTo(DataDB::gameNextPoint.name,gamenextPoint)
            .findFirst()
        realm.beginTransaction()
        result!!.deleteFromRealm()
        realm.commitTransaction()
        listener!!.onGameDelete()

        parentFragmentManager.beginTransaction().remove(this).commit()

        realm.close()
    }

    interface OnFragmentGameInteractionListener{

        fun onGameDelete()

        fun onGoEdit(gamedate:String,score:String,place:String,gametoday:String,gamereflection:String,gamenextPoint:String,mode:ModeInEdit,modeFragment: ModeFragment)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is OnFragmentGameInteractionListener){
            listener = context
        }else{
            throw RuntimeException(context.toString() + "must implement OnFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    companion object {

        @JvmStatic
        fun newInstance(gameDate: String,score:String,gamePlace : String, gameToday: String,gameReflection:String,gameNextPoint:String,mode: ModeInEdit,modeFragment: ModeFragment) =
            GameDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, gameDate)
                    putString(ARG_PARAM2, score)
                    putString(ARG_PARAM3, gamePlace)
                    putString(ARG_PARAM4, gameToday)
                    putString(ARG_PARAM5, gameReflection)
                    putString(ARG_PARAM6, gameNextPoint)
                    putSerializable(ARG_PARAM7,mode)
                    putSerializable(ARG_PARAM8,modeFragment)
                }
            }
    }
}
