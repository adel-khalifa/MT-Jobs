package com.example.mtjobs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    fun setFavorites(favorites : List<FavoriteItem>){
        favList= favorites
        notifyDataSetChanged()
    }

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


    // List of com.a.papersamwish.pojo.Data place holder / the object which can access from outside the adapter to add/update data
    val asyncListDiffer = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsAdapter.JobsViewHolder {
            val binding = ItemJobBinding.bind(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_job, parent, false))


        return JobsViewHolder(binding.root)
    }



    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: JobsAdapter.JobsViewHolder, position: Int) {

        asyncListDiffer.currentList.let {jobs ->
            val jobModel = jobs[position]

            holder.itemView.apply {
                item_job_company_name.text = jobModel.company
                item_job_title.text = jobModel.title
                if(favList.any { it.id==jobModel.id }){
                    job_item_ic_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                }else{
                    job_item_ic_favorite.setImageResource(R.drawable.ic_un_favorite_border_24)
                }



                Glide.with(context).load(jobModel.companyLogo)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(job_item_iv_image)

                job_item_ic_favorite.setOnClickListener{
                    listener.onFavoriteClick(jobModel.id)
                }
                setOnClickListener {
                    listener.onJobClick(jobModel.id)
                }
            }


        }
    }

    interface OnJobClickListener {
        fun onJobClick(jobId: String)
        fun onFavoriteClick(jobId: String)
    }

    fun setOnJobClickListener(onJobClickListener: OnJobClickListener) {
        listener = onJobClickListener
    }
}