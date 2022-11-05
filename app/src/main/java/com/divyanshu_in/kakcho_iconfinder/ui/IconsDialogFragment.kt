package com.divyanshu_in.kakcho_iconfinder.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.divyanshu_in.kakcho_iconfinder.databinding.FragmentListIconsBinding
import com.divyanshu_in.kakcho_iconfinder.ui.adapters.IconsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IconsDialogFragment(private val iconset_id: Int) : DialogFragment() {

    private var adapter: IconsAdapter? = null
    var binding: FragmentListIconsBinding? = null
    val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentListIconsBinding.inflate(inflater, container, false)

        binding!!.rvIconsList.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = IconsAdapter(requireContext()) {
            findNavController().navigate(IconSetsFragmentDirections.actionIconSetsFragmentToDownloadIconFragment(
                it))
        }
        binding!!.rvIconsList.adapter = adapter

        binding!!.tilSearch.visibility = View.GONE


        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getIcons(iconset_id, 0)

                viewModel.iconsList.collect {
                    adapter?.updateAdapter(it.filterNotNull())
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }


}