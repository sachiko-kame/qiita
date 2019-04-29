package com.example.qiitasampleproject.Activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import com.example.qiitasampleproject.Api.APIClient
import com.example.qiitasampleproject.Api.Get.UserRepos
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread


/*
*
* searchUserNameTextView
* serchButton
* serchResultListView
*
*/

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qiitasampleproject.R.layout.activity_main)

        serchButton.setOnClickListener {
            val userName = searchUserNameTextView.text.toString()
            this.hideKeyboard()
            this.searchArticle(userName)
        }
    }

    fun searchArticle(userName:String){

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        var firstRepos:List<UserRepos> = arrayListOf()

        val handler = Handler()

        thread {

            try {
                val response = APIClient.fetchUserReposList(userName, "1")
                firstRepos = response.body()!!

                handler.post(Runnable {
                    val count = firstRepos.size
                    for (i in 0..count - 1) {
                        arrayAdapter.add(firstRepos[i].title)
                    }

                    serchResultListView.adapter = arrayAdapter
                })

            } catch (e: Exception) {
                handler.post(Runnable {
                    AlertDialog.Builder(this)
                        .setTitle(e.localizedMessage)
                        .setPositiveButton("ok") { dialog, which ->
                        }.show()
                })
            }

        }

    }

    fun hideKeyboard() {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}
