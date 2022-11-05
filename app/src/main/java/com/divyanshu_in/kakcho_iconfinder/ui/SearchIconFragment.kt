package com.divyanshu_in.kakcho_iconfinder.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.divyanshu_in.kakcho_iconfinder.R
import com.divyanshu_in.kakcho_iconfinder.databinding.FragmentListIconsBinding
import com.divyanshu_in.kakcho_iconfinder.ui.adapters.IconsAdapter
import com.divyanshu_in.kakcho_iconfinder.utils.gone
import com.divyanshu_in.kakcho_iconfinder.utils.hide
import com.divyanshu_in.kakcho_iconfinder.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchIconFragment : Fragment() {

    var binding: FragmentListIconsBinding? = null
    private var adapter: IconsAdapter? = null
    val viewModel: MainViewModel by viewModels()

    var currentOffset = 0
    var isEndReached = false
    var currentQuery = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentListIconsBinding.inflate(inflater, container, false)

        binding!!.rvIconsList.gone()

        binding!!.rvIconsList.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = IconsAdapter(requireContext()){ icon_id ->
            findNavController().navigate(HomeFragmentDirections.actionFirstFragmentToDownloadIconFragment(icon_id))
        }
        binding!!.rvIconsList.adapter = adapter

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var debounceJob: Job? = null

        binding!!.rvIconsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && !isEndReached) {
                    viewModel.getIconsForSearch(currentQuery, currentOffset)
                }
            }
        })


        binding?.tilSearch?.editText?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                if(p0.isNullOrEmpty()){
                    binding!!.rvIconsList.gone()
                    debounceJob?.cancel()

                }else{
                    adapter?.clear()
                    debounceJob?.cancel()
                    debounceJob = lifecycleScope.launch {
                        delay(450)
                        currentQuery = p0.toString()
                        viewModel.getIconsForSearch(currentQuery, currentOffset)
                    }
                }


            }

        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.searchIconList.collect{
                    adapter?.updateAdapter(it.filterNotNull())

                    if(it.isNotEmpty()){
                        currentOffset += 1
                        binding!!.rvIconsList.visible()
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