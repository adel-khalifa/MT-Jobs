package com.example.mtjobs.ui.fragments

import android.graphics.Color.green
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mtjobs.R
import com.example.mtjobs.adapters.JobsAdapter
import com.example.mtjobs.data.models.FavoriteItem
import com.example.mtjobs.databinding.FragmentListBinding
import com.example.mtjobs.repo.JobsViewModel
import com.example.mtjobs.utils.NetworkState
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ListFragment : Fragment(R.layout.fragment_list), JobsAdapter.OnJobClickListener {
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



        jobsViewModel.cashedJobs.observe(viewLifecycleOwner) { cashedStatus ->
            when (cashedStatus) {
                is NetworkState.OnLoading -> {
                    binding.listFragmentErrorTv.text = getString(R.string.loading)
                    binding.wipeRefreshLayout.isRefreshing = true
                }
                is NetworkState.OnFailure -> {
                    binding.wipeRefreshLayout.isRefreshing = false

                    binding.listFragmentErrorTv.text =
                        cashedStatus.failureMessage
                }
                is NetworkState.OnSuccess -> {
                    binding.wipeRefreshLayout.isRefreshing = false

                    binding.listFragmentErrorTv.text = getString(R.string.success)
                    binding.listFragmentErrorTv.setTextColor(
                        resources.getColor(
                            R.color.green,
                            resources.newTheme()
                        )
                    )
                    jobsAdapter.asyncListDiffer.submitList(cashedStatus.bodyData?.toList())
                }

            }
        }
        jobsViewModel.favoritesLiveData.observe(viewLifecycleOwner) {
            jobsAdapter.setFavorites(it)
        }

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