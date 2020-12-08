package com.example.loginapp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OffersActivity : AppCompatActivity() {

    private lateinit var offersAdapter: OffersAdapter;
    private var displayDetailCount: Int = 1;
    private var position: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books_list)

        val listRefrence = findViewById<ListView>(R.id.offers_list)

        offersAdapter = OffersAdapter(this, 3, OfferService().getOffers())
        listRefrence.adapter = offersAdapter

        registerForContextMenu(listRefrence)

        listRefrence.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, OfferDetail::class.java)
            val offer: String = Gson().toJson(offersAdapter.getItem(position));
            intent.putExtra("offer", offer);
            intent.putExtra("count", displayDetailCount++);
            this.position = position;
            this.startActivityForResult(intent, 0);
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                val returnedFavorite: Boolean = data!!.getBooleanExtra("favorite", false);
                offersAdapter.getItem(this.position)!!.favorite = returnedFavorite

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        if (v!!.id == R.id.offers_list) {
            val info = menuInfo as AdapterView.AdapterContextMenuInfo

            menu!!.setHeaderTitle(offersAdapter.getItem(info.position)!!.title)
            getMenuInflater().inflate(R.menu.context_list, menu)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    private fun alertLogout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.alertTitle)
            .setMessage(R.string.alertMessage)
            .setPositiveButton(R.string.cancel){ dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton(R.string.confirm) { _, _ ->
                logoutRequest()
                finish()
            }
        builder.create().show()

    }
    private fun logoutRequest(){

        var chatAPI = ChatApi.create()

        val token = UserInfoSingleton.token

        chatAPI.logout("Bearer " + token).enqueue(
            object : Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    if (!response.isSuccessful) {
//                        Log.e("xyz", token)
                    } else {
//                        Log.e("xyz", "altceva")
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
//                    Log.e("xyz", "fail")
                }
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.signOut -> {
                alertLogout();

            }
            R.id.clearFavourites -> {
                for (i: Int in 0 until offersAdapter.count) {
                    offersAdapter.getItem(i)!!.favorite = false;
                }
            }
            R.id.resetList -> {
                this.displayDetailCount = 1;
                offersAdapter.clear();
                offersAdapter.addAll(OfferService().getOffers());
                offersAdapter.notifyDataSetChanged();
                Toast.makeText(this, R.string.resetListMessage, Toast.LENGTH_LONG).show();
            }
            R.id.chatWithUs -> {
                val intent = Intent(this, MessageActivity::class.java)
                this.startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val menuInfo = item.menuInfo as AdapterView.AdapterContextMenuInfo

        if (item.itemId == R.id.context_add) {
            offersAdapter.insert(
                Offer(
                    "Title added",
                    "Description for the added offer.",
                    0,
                    R.drawable.offer_3
                ), menuInfo.position
            )
            offersAdapter.notifyDataSetChanged();

        } else if (item.itemId == R.id.context_remove) {
            offersAdapter.remove(offersAdapter.getItem(menuInfo.position))
            offersAdapter.notifyDataSetChanged();
        }

        return super.onContextItemSelected(item)
    }

    override fun onBackPressed() {
        alertLogout();
    }

}