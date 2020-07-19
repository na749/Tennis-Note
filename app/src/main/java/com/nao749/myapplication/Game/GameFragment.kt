package com.nao749.myapplication.Game

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nao749.myapplication.DB.DataDB
import com.nao749.myapplication.Helper.MyButtonGame
import com.nao749.myapplication.Helper.MySwiperHelperGame
import com.nao749.myapplication.Helper.Mybutton
import com.nao749.myapplication.R


import io.realm.Realm


class GameFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    private var mListener : OnListGameFragmentInreractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    //LayoutManagerのセット
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)

        setHasOptionsMenu(true)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    //一行の場合はリニアレイアウト
                    columnCount <= 1 -> LinearLayoutManager(context)

                    //2行以上の場合はグリッドレイアウト
                    else -> GridLayoutManager(context, columnCount)
                }

                setHasFixedSize(true)

                val realm = Realm.getDefaultInstance()
                val result = realm.where(DataDB::class.java)
                    .equalTo(DataDB::fragmentFrag.name,"試合")
                    .findAll()

                adapter = MyGameRecyclerViewAdapter(result, listener)

                val swiper = object : MySwiperHelperGame(context,this,400){
                    override fun instantiateMyButton(
                        viewHolder: RecyclerView.ViewHolder,
                        buffer: MutableList<MyButtonGame>
                    ) {
                        buffer.add(MyButtonGame(context,
                            "削除",
                            30,
                        R.drawable.ic_delete_black_24dp,
                        Color.parseColor("#FF3c30"),
                        object : OnListGameFragmentInreractionListener{
                            override fun onClick(pos: Int) {

                                //削除処理
                                val resultDelete = result[pos]

                                realm.beginTransaction()
                                resultDelete!!.deleteFromRealm()
                                realm.commitTransaction()
                                realm.close()

                                listener!!.onDeleteGame()

                                Snackbar.make(this@with,"削除しました", Snackbar.LENGTH_SHORT).show()
                            }

                        }))
                    }
                }
            }
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        menu.apply {
            findItem(R.id.menu_delete).isVisible = false //削除
            findItem(R.id.menu_edit).isVisible = false   //編集
            findItem(R.id.menu_done).isVisible = false   //完了
            findItem(R.id.menu_share).isVisible = true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.menu_share -> {
                val shareTitle = "ノートリストの共有"

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,"練習や試合の記録をつけられるテニスノートアプリの決定版！！")
                    type = "text/*"

                }
                startActivity(Intent.createChooser(intent,shareTitle))

            }

            else -> return super.onOptionsItemSelected(item)

        }

        return true

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnListFragmentInteractionListener {

        //リストを押したときにおこること
        fun onItemClick(item : DataDB)

        fun onDeleteGame()

    }

    interface OnListGameFragmentInreractionListener{

        fun onClick(pos : Int)

    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
