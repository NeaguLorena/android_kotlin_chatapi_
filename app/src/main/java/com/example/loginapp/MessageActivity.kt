package com.example.loginapp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Handler

class MessageActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private var handler: android.os.Handler = android.os.Handler()
    var runnable: Runnable? = null
    private var delay = 10000

    private val messageList = LinkedList<Message>()

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        linearLayoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(this, messageList)

        setContentView(R.layout.activity_message)

        val usernameRef: TextView = findViewById(R.id.username_which)
        var display_user = "Username: " + UserInfoSingleton.display_username
        usernameRef.text = display_user

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater

        inflater.inflate(R.menu.message_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = messageAdapter

        when (item.itemId) {
            R.id.design1 -> {
                messageAdapter.style = 1
            }
            R.id.design2 -> {
                messageAdapter.style = 2
            }
            R.id.designBoth -> {
                messageAdapter.style = 3
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun sendMessageToServer(message: MessageData) {

        var chatAPI = ChatApi.create()

        val token = UserInfoSingleton.token

        chatAPI.sendMessage("Bearer " + token, message).enqueue(
            object : Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    if (!response.isSuccessful) {
                        Log.e("xyz", token)
                    } else {
                        messageList.push(
                            Message(
                                UserInfoSingleton.display_username, message.message,
                                LocalDateTime.now(), false
                            )
                        )
                        Log.e("xyz", "altceva " + token)
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("xyz", "fail")
                }
            }
        )
    }

    override fun onResume() {

//        setContentView(R.layout.activity_message)

        val sendButton: Button = findViewById(R.id.sendButton)
        val inputField: EditText = findViewById(R.id.editText)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = messageAdapter

        val sendMessage = View.OnClickListener {
            var messageData = MessageData(inputField.text.toString())
            sendMessageToServer(messageData)
            messageAdapter.notifyDataSetChanged()
        }

        sendButton.setOnClickListener(sendMessage)

        handler.postDelayed(Runnable {

            handler.postDelayed(runnable!!, delay.toLong())

            var chatAPI = ChatApi.create()

            val token = UserInfoSingleton.token

            chatAPI.readMessage("Bearer " + token).enqueue(
                object : Callback<ReadMessagesResponse> {
                    override fun onResponse(
                        call: Call<ReadMessagesResponse>,
                        response: Response<ReadMessagesResponse>
                    ) {
                        if (!response.isSuccessful) {
                            Log.e("xyz", "read " + token)
                        } else {
                            val readMessagesResponse = response.body() as ReadMessagesResponse
                            for (message in readMessagesResponse.messages) {
                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                val timestamp = LocalDateTime.parse(message.timestamp, formatter);
                                messageList.push(
                                    Message(
                                        message.sender, message.text,
                                        timestamp, false
                                    )
                                )
                            }
                            messageAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<ReadMessagesResponse>, t: Throwable) {
                        Log.e("xyz", "fail2")
                    }
                }
            )
        }.also { runnable = it }, delay.toLong())
        super.onResume()

    }
}