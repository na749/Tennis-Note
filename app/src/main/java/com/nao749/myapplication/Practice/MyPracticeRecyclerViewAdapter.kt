package com.nao749.myapplication.Practice

import android.content.ClipData
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.nao749.myapplication.DB.DataDB


import com.nao749.myapplication.Practice.PracticeFragment.OnListFragmentInteractionListener
import com.nao749.myapplication.R
import io.realm.Realm
import io.realm.RealmResults

import kotlinx.android.synthetic.main.fragment_practice.view.*
import org.w3c.dom.Text
import kotlin.math.log


class MyPracticeRecyclerViewAdapter(
    private var mValues: List<DataDB>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyPracticeRecyclerViewAdapter.ViewHolder>() ,Filterable {



    private val mOnClickListener: View.OnClickListener

    internal var filterListResult : List<DataDB>

    init {
        this.filterListResult = mValues
        Log.d("key2",filterListResult.size.toString())
    }

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as DataDB
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListItemClick(item)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charString: CharSequence): FilterResults {

                val charSearch = charString.toString()

                if (charString.isEmpty()){

                    filterListResult = mValues
                    Log.d("key5",filterListResult.size.toString())

                }
                else{
                    val resultList = ArrayList<DataDB>()
                    val realm = Realm.getDefaultInstance()
                    val q = realm.where(DataDB::class.java).findAll()

                    Log.d("key",charSearch)

                    for (i in filterListResult.indices) {

                        Log.d("key", filterListResult.size.toString())
                        Log.d("key", q[i]!!.date.toString())
                        Log.d("key", q[i]!!.today.toString())
                        Log.d("key", q[i]!!.reflection.toString())


                        if (q[i]!!.date.toLowerCase().contains(charSearch.toLowerCase())) {
                            resultList.add(q[i]!!)
                        } else if (q[i]!!.today.toLowerCase().contains(charSearch.toLowerCase())) {
                            resultList.add(q[i]!!)
                        } else if (q[i]!!.reflection.toLowerCase()
                                .contains(charSearch.toLowerCase())
                        ) {
                            resultList.add(q[i]!!)
                        } else if (q[i]!!.reflection.toLowerCase()
                                .contains(charSearch.toLowerCase())
                        ) {
                            resultList.add(q[i]!!)
                        }

                        Log.d("key3", resultList.size.toString())


                        filterListResult = resultList

                        Log.d("key4", resultList.size.toString())

                        Log.d("key4.1", filterListResult.size.toString())
                    }
                    realm.commitTransaction()

                }

                val filterResults = Filter.FilterResults()
                filterResults.values = filterListResult
                return filterResults

            }

            override fun publishResults(charSquence: CharSequence?, filterResults: FilterResults?) {
                Log.d("key",filterResults!!.count.toString())

                if(filterResults!!.count == 0){
                    notifyDataSetChanged()
                }else{
                    filterListResult = filterResults.values as List<DataDB>
                    notifyDataSetChanged()
                }
            }

        }

    }

    //ViewHolderの生成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_practice, parent, false)
        return ViewHolder(view)
    }


    //ViewHolderにくっつけ
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filterListResult[position]
        holder.date.text = filterListResult[position]!!.date
        holder.textNext.text = filterListResult[position]!!.nextPoint

        //RecyclerViewのクリックリスナー
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    //アダプターが保持している数
    override fun getItemCount(): Int = filterListResult.size


    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val date: TextView = mView.txtPracriceDate
        val textNext: TextView = mView.txtNextChallenge
        val practice: TextView = mView.PracticeTextView
    }


}




