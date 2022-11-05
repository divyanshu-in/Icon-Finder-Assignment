package com.divyanshu_in.kakcho_iconfinder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.divyanshu_in.kakcho_iconfinder.databinding.FragmentDownloadIconBinding
import com.divyanshu_in.kakcho_iconfinder.models.Status
import com.divyanshu_in.kakcho_iconfinder.ui.adapters.DownloadIconAdapter
import com.divyanshu_in.kakcho_iconfinder.utils.gone
import com.divyanshu_in.kakcho_iconfinder.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DownloadIconFragment : Fragment() {

    val viewModel: MainViewModel by viewModels()

    var binding: FragmentDownloadIconBinding? = null

    val args: DownloadIconFragmentArgs by navArgs()

    var adapter: DownloadIconAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentDownloadIconBinding.inflate(inflater, container, false)

        adapter = DownloadIconAdapter(requireContext()) { downloadUrl ->
            viewModel.downloadIcon(downloadUrl, requireContext())
        }

        binding!!.rvDownloadIcons.adapter = adapter

        viewModel.getIconDetails(args.iconId)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.downloadStatus.collect {
                    when (it) {
                        Status.LOADING -> {
                            binding!!.cvProgress.visible()
                        }
                        Status.FINISHED -> {
                            binding!!.cvProgress.gone()
                            Toast.makeText(requireContext(), "icon saved!", Toast.LENGTH_SHORT)
                                .show()

                        }
                        Status.FAILED -> {
                            binding!!.cvProgress.gone()
                            Toast.makeText(requireContext(),
                                "oops! something terrible happened, please try again!",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.iconDetails.collect {
                    if (!it?.rasterSizes.isNullOrEmpty()) {
                        binding!!.cvProgress.gone()
                    }
                    adapter?.updateAdapter(it?.rasterSizes?.filterNotNull() ?: emptyList())
                }
            }
        }

    }


}