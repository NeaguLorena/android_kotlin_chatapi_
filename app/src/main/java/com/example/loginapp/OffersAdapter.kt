package com.example.loginapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class OffersAdapter(context: Context, resource:  Int,  private var dataSource: ArrayList<Offer>): ArrayAdapter<Offer>(context, resource,  dataSource) {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.offer_view, parent, false)

        val offerTitle = rowView.findViewById(R.id.offer_title) as TextView
        offerTitle.text = dataSource.get(position).title

        val offerDescription = rowView.findViewById(R.id.offer_description) as TextView
        offerDescription.text = dataSource.get(position).description

        val offerImage = rowView.findViewById(R.id.offer_image) as ImageView
        offerImage.setImageResource(dataSource.get(position).image)

        val offerPrice = rowView.findViewById(R.id.offer_price) as TextView
        offerPrice.text = dataSource.get(position).price.toString() + " EUR"

        return rowView
    }

    override fun getItem(position: Int): Offer? {
        return dataSource.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }

}