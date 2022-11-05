package com.divyanshu_in.kakcho_iconfinder.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.divyanshu_in.kakcho_iconfinder.databinding.FragmentIconSetsBinding
import com.divyanshu_in.kakcho_iconfinder.ui.adapters.IconsetsAdapter
import com.divyanshu_in.kakcho_iconfinder.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch



@AndroidEntryPoint
class IconSetsFragment : Fragment() {

    var binding: FragmentIconSetsBinding? = null
    var adapter: IconsetsAdapter? = null
    val args : IconSetsFragmentArgs by navArgs()
    val viewModel: MainViewModel by viewModels()

    var currentOffset = 0
    var isEndReached = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentIconSetsBinding.inflate(inflater, container, false)

        adapter = IconsetsAdapter(requireContext()){
            IconsDialogFragment(it).show(childFragmentManager, "TAG")
        }
        binding!!.rvIconsets.adapter = adapter

        binding!!.tvCategoryName.text = args.categoryName

        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.rvIconsets.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && !isEndReached) {
                    viewModel.getIconSets(args.categoryIdentifier, currentOffset)
                }
            }
        })


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){

            viewModel.getIconSets(args.categoryIdentifier, currentOffset)

            viewModel.iconsetsList.collect{
                adapter?.updateAdapter(it)

                if(it.isNotEmpty()){
                    currentOffset += 1
                    isEndReached = false
                }else{
                    isEndReached = true
                    adapter?.endReached()
                }
            }
        }

    }




}
}