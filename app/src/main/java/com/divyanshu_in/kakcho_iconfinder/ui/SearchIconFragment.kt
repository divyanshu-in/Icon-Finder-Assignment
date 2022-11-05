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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentListIconsBinding.inflate(inflater, container, false)

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

        binding?.tilSearch?.editText?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                if(p0.isNullOrEmpty()){
                    binding!!.tvIconsTitle.gone()
                    binding!!.rvIconsList.gone()
                }

                debounceJob?.cancel()
                debounceJob = lifecycleScope.launch {
                    delay(450)
                    viewModel.getIconsForSearch(p0.toString(), 0)
                }
            }

        })

        lifecycleScope.launch {
            viewModel.searchIconList.collect{
                adapter?.updateAdapter(it.filterNotNull())
                binding!!.rvIconsList.visible()
                binding!!.tvIconsTitle.visible()
            }
        }

    }

}