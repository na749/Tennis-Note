package com.nao749.myapplication.Game

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nao749.myapplication.DB.DataDB
import com.nao749.myapplication.R


import com.nao749.myapplication.Game.GameFragment.OnListFragmentInteractionListener

import io.realm.RealmResults

import kotlinx.android.synthetic.main.fragment_game.view.*


class MyGameRecyclerViewAdapter(
    private val mValues: RealmResults<DataDB>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyGameRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as DataDB
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onItemClick(item)
        }
    }

    //ViewHolderの作成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_game, parent, false)
        return ViewHolder(view)
    }

    //ViewHolderをくっつけ
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.gameDate.text = mValues[position]!!.gameDate
        holder.place.text = mValues[position]!!.gamePlace
        holder.score.text = mValues[position]!!.score


        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    //ViewHolderの生成
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val gameDate : TextView = mView.txtGameDate
        val place :TextView = mView.txtGamePlace
        val score : TextView = mView.txtGameScore
    }
}
