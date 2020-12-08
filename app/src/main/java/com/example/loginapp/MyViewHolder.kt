package com.example.loginapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import java.time.format.DateTimeFormatter

class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener {

    private var nameRef:TextView =itemView.findViewById(R.id.name)
    private var contentRef:TextView =itemView.findViewById(R.id.content)
    private var timeRef:TextView =itemView.findViewById(R.id.time)
    private var starred: ImageView =itemView.findViewById(R.id.imageViewStar)

    lateinit var message: Message

    fun bindData(data: Message){
        nameRef.text=data.name
        contentRef.text=data.content

//        var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

        timeRef.text = data.time.toString() //formatter.format(data.time)
        starred.isVisible=data.starred

        message=data
    }

    init {
        itemView.setOnClickListener(this)
    }
    override fun onClick(view: View?) {
        message.starred = !message.starred
        starred.isVisible = message.starred
    }

}