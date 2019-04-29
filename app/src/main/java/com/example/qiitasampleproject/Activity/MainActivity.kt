package com.example.qiitasampleproject.Activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.transition.Fade
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.bumptech.glide.Glide
import com.example.qiitasampleproject.Adapter.QiitaUserArticleAdapter
import com.example.qiitasampleproject.Api.APIClient
import com.example.qiitasampleproject.Api.Get.UserRepos
import com.example.qiitasampleproject.R
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


        serchResultListView.setOnItemClickListener { adapterView, view, position, id ->
            println(firstRepos[position].url)

            val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.popup_about_article,null)

            val popupWindow = PopupWindow(
                view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                val fade = Fade()
                fade.setDuration(1200);
                popupWindow.enterTransition = fade
                popupWindow.exitTransition = fade

            }

            val userImageView = view.findViewById<ImageView>(R.id.userImageView)
            val followeesCountTextView = view.findViewById<TextView>(R.id.followeesCountTextView)
            val followersCountTextView = view.findViewById<TextView>(R.id.followersCountTextView)
            val idTextView = view.findViewById<TextView>(R.id.idTextView)

            val closeButton = view.findViewById<Button>(R.id.closeButton)

            val userImageUrl = firstRepos[position].user["profile_image_url"]
            Glide.with(this).load(userImageUrl).into(userImageView);

            followeesCountTextView.text = "フォローしている数 :" + firstRepos[position].user["followees_count"].toString()
            followersCountTextView.text = "フォローされている数 : " + firstRepos[position].user["followers_count"].toString()
            idTextView.text =  "id : " + firstRepos[position].user["id"].toString()


            TransitionManager.beginDelayedTransition(this.serchResultListView)
            popupWindow.showAtLocation(
                this.serchResultListView, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )

            closeButton.setOnClickListener{
                popupWindow.dismiss()
            }
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

