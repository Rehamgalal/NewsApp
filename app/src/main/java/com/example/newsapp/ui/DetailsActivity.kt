package com.example.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.db.ArticleEntity
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val article = intent.getParcelableExtra<ArticleEntity>("article")

        setArticleContents(article!!)
    }

    private fun setArticleContents(article: ArticleEntity) {
        article_title.text = article.title
        Glide.with(this)
            .load(article.urlToImage)
            .placeholder(R.drawable.placeholder)
            .into(image)
        description.text = article.description
        if (article.publishedAt!=null) {
            publish_at.text = article.publishedAt.replace("T", resources.getString(R.string.at)).replace("Z", "")
        }
        author.text = article.author
        val contentString: String = if (article.content != null) {
            Html.fromHtml(article.content,
                Html.FROM_HTML_MODE_LEGACY, null, { opening, tag, output, _ ->
                    if (tag.equals("ul") && !opening) output.append("")
                    if (tag.equals("li") && opening) output.append("")
                }).toString()
        } else {
            ""
        }
        content.text = contentString
        link.movementMethod = LinkMovementMethod.getInstance()
        link.text = article.url
        source.text = article.source?.name


    }
}