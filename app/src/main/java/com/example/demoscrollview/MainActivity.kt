package com.example.demoscrollview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    var productList: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        productList = findViewById<LinearLayout>(R.id.linearLayout)
        val queryInput = findViewById<EditText>(R.id.editText)
        queryInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                makeRequest("https://search.phongvu.vn/api?channelId=offline&userId=demoandroid&order=stock&orderBy=desc&branchCode=CP09&query=${p0}")
            }
        })
    }

    fun makeRequest(url: String) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val psResponse: PSResponse = gson.fromJson(response.body()?.string(), PSResponse::class.java)
                println(psResponse)

                runOnUiThread {
                    this@MainActivity.productList?.removeAllViews()
                    for (product in psResponse.data.data.products) {
                        val productText = TextView(this@MainActivity)
                        productText.text = "${product.name} - ${product.pv_sku}"
                        productText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20.toFloat())
                        this@MainActivity.productList?.addView(productText)
                    }
                }
            }
        })
    }
}
