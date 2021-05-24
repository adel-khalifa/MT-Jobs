package com.example.mtjobs.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.children
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mtjobs.R
import com.example.mtjobs.adapters.JobsAdapter
import com.example.mtjobs.data.models.FavoriteItem
import com.example.mtjobs.data.models.JobsResponseItem
import com.example.mtjobs.databinding.FragmentListBinding
import com.example.mtjobs.repo.JobsViewModel
import com.example.mtjobs.utils.NetworkState
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*

class ListFragment : Fragment(R.layout.fragment_list), JobsAdapter.OnJobClickListener {
    private var jobsList: List<JobsResponseItem> = listOf()
    private lateinit var binding: FragmentListBinding
    private lateinit var navController: NavController
    private lateinit var jobsAdapter: JobsAdapter

    private val jobsViewModel: JobsViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListBinding.bind(view)
        navController = findNavController()

        initRecyclerView()
        binding.wipeRefreshLayout.setOnRefreshListener {
            jobsViewModel.reloadData()
        }

        handleSearchMechanism()

        observeJobStatus()


        observeFavoritesList()


    }

    private fun handleSearchMechanism() =
        binding.searchEdt.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return if (!query.isNullOrBlank()) {
                    filterJobsList(query)
                    false
                } else {
                    populateFullList()
                    true
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return if (!newText.isNullOrBlank()) {
                    filterJobsList(newText)
                    false
                } else {
                    populateFullList()
                    true
                }
            }

        })

    private fun observeJobStatus() =
        jobsViewModel.cashedJobs.observe(viewLifecycleOwner) { cashedStatus ->
            when (cashedStatus) {
                is NetworkState.OnLoading -> handleLoadingStatus()
                is NetworkState.OnFailure -> handleFailureStatus(cashedStatus.failureMessage)
                is NetworkState.OnSuccess -> handleSuccessStatus(cashedStatus)
            }
        }

    private fun observeFavoritesList() =
        jobsViewModel.favoritesLiveData.observe(viewLifecycleOwner) {
            jobsAdapter.setFavorites(it)
        }

    private fun handleSuccessStatus(cashedStatus: NetworkState.OnSuccess<List<JobsResponseItem>>) {
        binding.wipeRefreshLayout.isRefreshing = false
        binding.listFragmentErrorTv.text = getString(R.string.empty_text)
        jobsList = cashedStatus.bodyData!!
        jobsAdapter.asyncListDiffer.submitList(jobsList)

    }

    private fun handleFailureStatus(failureMessage: String?) {
        binding.wipeRefreshLayout.isRefreshing = false
        binding.listFragmentErrorTv.text = failureMessage

    }

    private fun handleLoadingStatus() {
//        binding.listFragmentErrorTv.text = getString(R.string.loading)


        binding.wipeRefreshLayout.isRefreshing = true
    }


    private fun populateFullList() {
        jobsAdapter.setQuery("")
        jobsAdapter.asyncListDiffer.submitList(jobsList)
    }

    private fun filterJobsList(newText: String) {
        val filteredList = jobsList.filter { jobModel ->
            jobModel.company.toLowerCase(Locale.ROOT).contains(newText) or
                    jobModel.title.toLowerCase(Locale.ROOT).contains(newText)
        }
        jobsAdapter.setQuery(newText)
        jobsAdapter.asyncListDiffer.submitList(filteredList)
    }

    private fun initRecyclerView() {
        jobsAdapter = JobsAdapter()
        jobsAdapter.setOnJobClickListener(this)
        binding.listRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.listRv.adapter = jobsAdapter
    }


    override fun onJobClick(jobId: String) {
        navController.navigate(ListFragmentDirections.actionListFragmentToDetailsFragment(jobId))

    }


    override fun onFavoriteClick(jobId: String) {
        jobsViewModel.toggleIsFavorite(FavoriteItem(jobId))
    }


}