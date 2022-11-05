package com.divyanshu_in.kakcho_iconfinder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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


        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.categoriesList.collect{
                rvadapter?.updateAdapter(it)
            }
        }

        viewModel.getCategories()

    }

}