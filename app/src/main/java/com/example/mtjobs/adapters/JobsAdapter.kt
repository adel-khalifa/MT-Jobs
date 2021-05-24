package com.example.mtjobs.adapters

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mtjobs.R
import com.example.mtjobs.data.models.FavoriteItem
import com.example.mtjobs.data.models.JobsResponseItem
import com.example.mtjobs.databinding.ItemJobBinding
import kotlinx.android.synthetic.main.item_job.view.*

class JobsAdapter : RecyclerView.Adapter<JobsAdapter.JobsViewHolder>() {
    private lateinit var listener: OnJobClickListener
    private var favList = listOf<FavoriteItem>()
    private var queryText = ""




    inner class JobsViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class DiffImpl : DiffUtil.ItemCallback<JobsResponseItem>() {
        override fun areItemsTheSame(
            oldItem: JobsResponseItem,
            newItem: JobsResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: JobsResponseItem,
            newItem: JobsResponseItem
        ): Boolean {
            return oldItem == newItem
        }


    }

    private val diffCallBack = DiffImpl()


    val asyncListDiffer = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsAdapter.JobsViewHolder {
        val binding = ItemJobBinding
            .bind(LayoutInflater.from(parent.context).inflate(R.layout.item_job, parent, false))


        return JobsViewHolder(binding.root)
    }


    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: JobsAdapter.JobsViewHolder, position: Int) {


        asyncListDiffer.currentList.let { jobs ->
            val jobModel = jobs[position]

            holder.itemView.apply {

                Glide.with(context).load(jobModel.companyLogo)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(job_item_iv_image)


                item_job_company_name.text = jobModel.company
                item_job_title.text = jobModel.title
                highlightText(view = this)

                toggleFavorite(jobModel, job_item_ic_favorite)



                job_item_ic_favorite.setOnClickListener {
                    listener.onFavoriteClick(jobModel.id)
                }
                setOnClickListener {
                    listener.onJobClick(jobModel.id)
                }
            }


        }
    }
    fun setFavorites(favorites: List<FavoriteItem>) {
        favList = favorites
        notifyDataSetChanged()
    }

    fun setQuery(query: String) {
        queryText = query
        notifyDataSetChanged()
    }

    private fun toggleFavorite(jobModel: JobsResponseItem, favoriteIcon: ImageView) {
        if (favList.any { it.id == jobModel.id }) {
            favoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            favoriteIcon.setImageResource(R.drawable.ic_un_favorite_border_24)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun highlightText(view: View) = view.apply {

        val title = item_job_title.text.toString()
        val titleHtml = title.replace(queryText, "<font color='#C8963C'>$queryText</font>",true)
        item_job_title.text = Html.fromHtml(titleHtml, Html.FROM_HTML_OPTION_USE_CSS_COLORS)

        val company = item_job_company_name.text.toString()
        val companyHtml = company.replace(queryText, "<font color='#C8963C'>$queryText</font>",true)
        item_job_company_name.text =
            Html.fromHtml(companyHtml, Html.FROM_HTML_OPTION_USE_CSS_COLORS)

    }

    interface OnJobClickListener {
        fun onJobClick(jobId: String)
        fun onFavoriteClick(jobId: String)
    }

    fun setOnJobClickListener(onJobClickListener: OnJobClickListener) {
        listener = onJobClickListener
    }
}