package com.divyanshu_in.kakcho_iconfinder.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.divyanshu_in.kakcho_iconfinder.databinding.FragmentIconSetsBinding
import com.divyanshu_in.kakcho_iconfinder.ui.adapters.IconsetsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch



@AndroidEntryPoint
class IconSetsFragment : Fragment() {

    var binding: FragmentIconSetsBinding? = null
    var adapter: IconsetsAdapter? = null
    val args : IconSetsFragmentArgs by navArgs()
    val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentIconSetsBinding.inflate(inflater, container, false)

        adapter = IconsetsAdapter(requireContext()){
            val dialog = IconsDialogFragment(it).show(childFragmentManager, "TAG")
        }
        binding!!.rvIconsets.adapter = adapter

        binding!!.tvCategoryName.text = args.categoryName

        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.getIconSets(args.categoryIdentifier, 1)

            viewModel.iconsetsList.collect{
                adapter?.updateAdapter(it)
            }
        }
    }




}