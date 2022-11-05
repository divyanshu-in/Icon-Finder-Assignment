package com.divyanshu_in.kakcho_iconfinder.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.divyanshu_in.kakcho_iconfinder.R
import com.divyanshu_in.kakcho_iconfinder.databinding.FragmentHomeBinding
import com.divyanshu_in.kakcho_iconfinder.ui.adapters.CategoriesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var rvadapter: CategoriesAdapter? = null
    var binding: FragmentHomeBinding? = null

    val viewModel: MainViewModel by viewModels()

    var isEndReached = false

    var totalCategoryCount = 0

    var lastCategoryIdentifier: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        rvadapter = CategoriesAdapter(requireContext()){categoryName, categoryIdentifier ->
            findNavController().navigate(HomeFragmentDirections.actionFirstFragmentToIconSetsFragment(categoryIdentifier, categoryName))
        }

        childFragmentManager.beginTransaction()
            .add(R.id.list_icons_fragment_container, SearchIconFragment())
            .commit()


        binding!!.rvCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = rvadapter
        }

        binding!!.rvCategories.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && !isEndReached) {
                    viewModel.getCategories(lastCategoryIdentifier)
                }
            }
        })

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.categoryData.collect{

                totalCategoryCount += (it?.categories?.size ?: 0)
                isEndReached = ((it?.totalCount != null) && (totalCategoryCount == it.totalCount))
                lastCategoryIdentifier = it?.categories?.last()?.identifier
                rvadapter?.updateAdapter(it?.categories ?: emptyList())

                if(isEndReached){
                    rvadapter?.endReached()
                }
            }
        }

        viewModel.getCategories(null)

    }

}