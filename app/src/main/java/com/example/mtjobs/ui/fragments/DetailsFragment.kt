package com.example.mtjobs.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mtjobs.R
import com.example.mtjobs.data.models.FavoriteItem
import com.example.mtjobs.data.models.JobsResponseItem
import com.example.mtjobs.databinding.FragmentDetailsBinding
import com.example.mtjobs.repo.JobsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel


class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding
    private val args: DetailsFragmentArgs by navArgs()
    private val jobsViewModel: JobsViewModel by sharedViewModel()
    private lateinit var jobModel: JobsResponseItem
    private lateinit var favoriteStatus: FavoriteItem


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailsBinding.bind(view)
        jobModel = getJobModelById(args.jobId)!!
        favoriteStatus = FavoriteItem(jobModel.id)

        setListeners()
        setFavoriteStatusObserver()

        bindViews()
        setUpHtmlFunctionality()

    }

    private fun bindViews() {
        Glide.with(requireContext()).load(jobModel.companyLogo)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.detailsJobImage)


        binding.detailsCompanyName.text = jobModel.company
        binding.detailsJobTitle.text = jobModel.title
        binding.detailsCompanySite.text = jobModel.companyUrl
        binding.detailsJobLocation.text = jobModel.location
        binding.detailsJobType.text = jobModel.type
        binding.detailsJobSource.text = jobModel.url

    }



    private fun setFavoriteStatusObserver() =
        jobsViewModel.favoritesLiveData.observe(viewLifecycleOwner) { favList ->
            if (favList.any { it.id == jobModel.id }) binding.detailsIcFavorite
                .setImageResource(R.drawable.ic_baseline_favorite_24)
            else binding.detailsIcFavorite
                .setImageResource(R.drawable.ic_un_favorite_border_24)
        }


    private fun setListeners() {
        binding.detailsIcBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.detailsIcFavorite.setOnClickListener {
            jobsViewModel.toggleIsFavorite(favoriteStatus)
        }

        binding.detailsJobSource.setOnClickListener {
            startActivity(Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(binding.detailsJobSource.text.toString())
            })
        }

        binding.detailsCompanySite.setOnClickListener {
            startActivity(Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(binding.detailsCompanySite.text.toString())
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setUpHtmlFunctionality() {
        binding.detailsHowToApply.text = Html.fromHtml(
            jobModel.howToApply,
            Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL
        )

        binding.detailsJobDescription.text = Html.fromHtml(
            jobModel.description,
            Html.FROM_HTML_SEPARATOR_LINE_BREAK_HEADING
        )

        binding.detailsHowToApply.movementMethod = LinkMovementMethod.getInstance()
        binding.detailsJobDescription.movementMethod = LinkMovementMethod.getInstance()
    }


    private fun getJobModelById(jobId: String) =
        jobsViewModel.cashedJobs.value?.bodyData?.first { it.id == jobId }




}