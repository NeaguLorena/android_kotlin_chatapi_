package com.example.loginapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class OfferDetail : AppCompatActivity() {

    private lateinit var offer: Offer;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.offer_detail)

        offer = Gson().fromJson(intent.getStringExtra("offer"), Offer::class.java);
        var count = intent.getIntExtra("count", 0);

        findViewById<TextView>(R.id.offer_title).setText(offer.title)
        findViewById<TextView>(R.id.offer_description).setText(offer.description)
        findViewById<ImageView>(R.id.offer_image).setImageResource(offer.image)
        findViewById<TextView>(R.id.offer_price).setText(offer.price.toString() + " EUR")
        findViewById<TextView>(R.id.displaed_info).setText("Details page displayed:  $count  times")

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.option_detail_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val favourite: MenuItem = menu.findItem(R.id.favorite);

        if (offer.favorite) {
            favourite.setTitle(R.string.removeFavorite);
        } else {
            favourite.setTitle(R.string.addFavorite);
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.favorite) {
            if (offer.favorite) {
                offer.favorite = false;
                item.setTitle(R.string.addFavorite);
                Toast.makeText(this, R.string.removeFavoriteMessage, Toast.LENGTH_LONG)
                    .show();
            } else {
                offer.favorite = true;
                item.setTitle(R.string.removeFavorite);
                Toast.makeText(this, R.string.addFavoriteMessage, Toast.LENGTH_LONG)
                    .show();
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed(){
        val intent = Intent()
        intent.putExtra("favorite", offer.favorite);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}