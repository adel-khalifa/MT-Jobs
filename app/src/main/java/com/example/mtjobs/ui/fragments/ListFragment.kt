package com.example.mtjobs.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mtjobs.R
import com.example.mtjobs.adapters.JobsAdapter
import com.example.mtjobs.databinding.FragmentListBinding
import com.example.mtjobs.repo.JobsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var binding: FragmentListBinding
    private lateinit var navController: NavController
    private lateinit var jobsAdapter: JobsAdapter

    private val jobsViewModel: JobsViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)
        navController = findNavController()

        jobsAdapter = JobsAdapter()
        binding.listRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.listRv.adapter = jobsAdapter

        jobsViewModel.fetchCashedJobsFromDataBase()
        jobsViewModel.allJobs.observe(viewLifecycleOwner) {
            jobsAdapter.asyncListDiffer.submitList(it.bodyData?.toList())
        }

        /////
        binding.icic.setOnClickListener { navController.navigate(R.id.detailsFragment) }

    }


}