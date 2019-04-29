package com.example.qiitasampleproject.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.qiitasampleproject.Api.Get.UserRepos
import com.example.qiitasampleproject.Util.Extension.removehtmlTag

//ViewHolder 表示する部品の一時保存場所
data class QiitaUserArticleViewHolder(val titleTextView: TextView,
                                      val descriptionTextView: TextView,
                                      val goodTextView: TextView,
                                      val commentTextView: TextView,
                                      val userImageView: ImageView,
                                      val webGoButton:Button
)

class QiitaUserArticleAdapter(context: Context, items: List<UserRepos>): ArrayAdapter<UserRepos>(context, 0, items)  {

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        var holder: QiitaUserArticleViewHolder

        //position: Int 行数
        //convertView: View? レイアウトのビュー
        //parent: ViewGroup? 親のビュー

        if (view == null) {
            view = layoutInflater.inflate(com.example.qiitasampleproject.R.layout.list_item_qiita_user_article, parent, false)
            holder = QiitaUserArticleViewHolder(
                view.findViewById(com.example.qiitasampleproject.R.id.titleTextView),
                view.findViewById(com.example.qiitasampleproject.R.id.descriptionTextView),
                view.findViewById(com.example.qiitasampleproject.R.id.goodTextView),
                view.findViewById(com.example.qiitasampleproject.R.id.commentTextView),
                view.findViewById(com.example.qiitasampleproject.R.id.userImageView),
                view.findViewById(com.example.qiitasampleproject.R.id.webGoButton)
            )
            view.tag = holder
        } else {
            holder = view.tag as QiitaUserArticleViewHolder
        }

        val userData = getItem(position) as UserRepos
        holder.titleTextView.text = userData.title
        holder.descriptionTextView.text = userData.rendered_body.removehtmlTag().subSequence(0, 150)
        holder.goodTextView.text = "❤ ️" + userData.likes_count.toString()
        holder.commentTextView.text = "✍ ️" + userData.comments_count.toString()

        //これはgif
//        Glide.with(context)
//            .asGif()
//            .load("https://s3.amazonaws.com/appsdeveloperblog/micky.gif")
//            .into(holder.userImageView)

        val userImageUrl = userData.user["profile_image_url"]
        Glide.with(context).load(userImageUrl).into(holder.userImageView);

        return view!!

    }

}






