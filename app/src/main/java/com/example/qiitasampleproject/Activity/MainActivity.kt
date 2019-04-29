package com.example.qiitasampleproject.Activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import com.example.qiitasampleproject.Adapter.QiitaUserArticleAdapter
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
    var firstRepos:List<UserRepos> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qiitasampleproject.R.layout.activity_main)

        serchButton.setOnClickListener {
            val userName = searchUserNameTextView.text.toString()?: ""
            this.hideKeyboard()
            this.searchArticle(userName)
        }

        serchResultListView.setOnItemClickListener {parent, view, position, id ->
            println(firstRepos[position].url)
        }
    }

    fun searchArticle(userName:String){

        val handler = Handler()

        thread {

            try {

                if(userName != ""){
                    val response = APIClient.fetchUserReposList(userName, "1")
                    firstRepos = response.body()!!
                }else{
                    val response = APIClient.fetchUser_allReposList()
                    firstRepos = response.body()!!
                }

                handler.post(Runnable {
                    val adapter = QiitaUserArticleAdapter(this, firstRepos)
                    adapter.onClickWeb = {
                        this.webOpen(it)
                    }
                    serchResultListView.adapter = adapter

                })

            } catch (e: Exception) {
                handler.post(Runnable {
                    AlertDialog.Builder(this)
                        .setTitle("エラーが起きました。")
                        .setPositiveButton("ok") { dialog, which ->
                        }.show()
                })
            }

        }

    }

    fun webOpen(url: String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    fun hideKeyboard() {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}
