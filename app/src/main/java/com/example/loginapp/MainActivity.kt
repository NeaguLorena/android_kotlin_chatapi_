package com.example.loginapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usernameInput = findViewById<EditText>(R.id.username)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        var usernameErrorInput = findViewById<TextView>(R.id.usernameErrorInput)
        var passwordErrorInput = findViewById<TextView>(R.id.passwordErrorInput)
        var loginTextInput = findViewById<TextView>(R.id.loginTextInput)

        var res: LoginResponse? //api response variable
        val intent = Intent(this, OffersActivity::class.java)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString();
            var password = passwordInput.text.toString();

            var ok = 0;

            usernameErrorInput.setTextColor(Color.parseColor("#FF0000"))
            passwordErrorInput.setTextColor(Color.parseColor("#FF0000"))

            if (username.trim().length == 0) {
                usernameErrorInput.setText(getString(R.string.username_empty))
                ok = 1
            } else if (username.trim().length < 3) {
                usernameErrorInput.setText(getString(R.string.username_short))
                ok = 1
            } else {
                usernameErrorInput.setText("")
            }

            if (password.trim().length == 0) {
                passwordErrorInput.setText((getString(R.string.password_empty)))
                ok = 1
            } else if (password.trim().length < 3) {
                passwordErrorInput.setText((getString(R.string.password_short)))
                ok = 1
            } else {
                passwordErrorInput.setText("")
            }

            if (ok == 0) {
                //chatapi call for credentials verification
                val userData = UserInfo(
                    username = username,
                    password = password
                )
                var chatAPI = ChatApi.create()

                chatAPI.login(userData).enqueue(
                    object : Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            res = response.body()
                            if (!response.isSuccessful) {
                                loginTextInput.setTextColor(Color.parseColor("#FF0000"))
                                loginTextInput.setText(getString(R.string.login_err))
                            } else {
                                loginTextInput.setTextColor(Color.parseColor("#00FF00"))
                                loginTextInput.setText(getString(R.string.login_ok))
                                var token = res!!.token
                                UserInfoSingleton.token = token
                                UserInfoSingleton.display_username = res!!.display
                                UserInfoSingleton.username = username
                                UserInfoSingleton.password = password
                                startActivity(intent)
                            }
                        }
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        }
                    }
                )
                //chatapi ends here
            } else {
                loginTextInput.setText("")
            }
        }
    }
}