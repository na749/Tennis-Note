package com.nao749.myapplication.Practice

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nao749.myapplication.DB.DataDB
import com.nao749.myapplication.Helper.MySwipeHelper
import com.nao749.myapplication.Helper.Mybutton
import com.nao749.myapplication.Helper.MybuttonClickListener
import com.nao749.myapplication.R
import com.nao749.myapplication.makeToast

import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_practice_list.*


class PracticeFragment : Fragment() {

    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    private var mListener : OnListPracticeFragmentInteractionListener? = null


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
        val view = inflater.inflate(R.layout.fragment_practice_list, container, false)


        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {

                    //行が1列の場合はリニアレイアウトマネージャー
                    columnCount <= 1 -> LinearLayoutManager(context)

                    //二列以上の場合はグリッドレイアウトマネージャー
                    else -> GridLayoutManager(context, columnCount)
                }

                setHasFixedSize(true)


                val realm = Realm.getDefaultInstance()
                val result = realm.where(DataDB::class.java)
                    .equalTo(DataDB::fragmentFrag.name, "練習")
                    .findAll()

                adapter = MyPracticeRecyclerViewAdapter(result, listener)

                val swipe = object : MySwipeHelper(context,this,200){
                    override fun instantiateMyButton(
                        viewHolder: RecyclerView.ViewHolder,
                        buffer: MutableList<Mybutton>
                    ) {
                        buffer.add(Mybutton(context,"Delete",30,0,Color.parseColor("#FF3C30"),
                            object : OnListPracticeFragmentInteractionListener {
                                override fun onClick(pos: Int) {
                                    //削除処理
                                    makeToast(context,"け")
                                }

                            }))

                    }

                }

            }
        }
        return view
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


    //インターフェース
    interface OnListFragmentInteractionListener {

        //リストの項目を押したときに行う処理
        fun onListItemClick(item: DataDB)

    }

    interface OnListPracticeFragmentInteractionListener{
        fun onClick(pos:Int)
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        //ファクトリーメソッド
        @JvmStatic
        fun newInstance(columnCount: Int) =
            PracticeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

}
