package com.nao749.myapplication.Practice

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.nao749.myapplication.DB.DataDB


import com.nao749.myapplication.Practice.PracticeFragment.OnListFragmentInteractionListener
import com.nao749.myapplication.R
import io.realm.RealmResults

import kotlinx.android.synthetic.main.fragment_practice.view.*
import org.w3c.dom.Text


class MyPracticeRecyclerViewAdapter(
    private val mValues: RealmResults<DataDB>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyPracticeRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as DataDB
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListItemClick(item)
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
        val item = mValues[position]
        holder.date.text = mValues[position]!!.date
        holder.textNext.text = mValues[position]!!.nextPoint

        //RecyclerViewのクリックリスナー
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    //アダプターが保持している数
    override fun getItemCount(): Int = mValues.size


    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val date : TextView = mView.txtPracriceDate
        val textNext : TextView = mView.txtNextChallenge
        val practice : TextView = mView.PracticeTextView
    }
}
