package com.example.loginapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MessageAdapter(context:Context, private val dataSource:Queue<Message>)
    :RecyclerView.Adapter<MyViewHolder>(){

    var style = 1

    private val inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = inflater.inflate(viewType,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(dataSource.elementAt(position))
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun getItemViewType(position:Int):Int{
        if(style == 1){
            return  R.layout.message_item
        }
        if(style==2){
            return R.layout.message_itemv2
        }

        return if(position%2==0){
            R.layout.message_item
        }else{
            R.layout.message_itemv2
        }
    }
}