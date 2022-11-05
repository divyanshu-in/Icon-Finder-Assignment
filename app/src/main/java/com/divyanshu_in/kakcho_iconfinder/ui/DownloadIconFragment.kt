package com.divyanshu_in.kakcho_iconfinder.ui

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.divyanshu_in.kakcho_iconfinder.R
import com.divyanshu_in.kakcho_iconfinder.databinding.FragmentDownloadIconBinding
import com.divyanshu_in.kakcho_iconfinder.ui.adapters.DownloadIconAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DownloadIconFragment : Fragment() {

    val viewModel: MainViewModel by viewModels()

    var binding: FragmentDownloadIconBinding? = null

    val args : DownloadIconFragmentArgs by navArgs()

    var adapter: DownloadIconAdapter? = null

    var progressDialog: ProgressDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentDownloadIconBinding.inflate(inflater, container, false)

        adapter = DownloadIconAdapter(requireContext()){ downloadUrl->
            viewModel.downloadIcon(downloadUrl, requireContext())
        }

        binding!!.rvDownloadIcons.adapter = adapter

        viewModel.getIconDetails(args.iconId)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){

                viewModel.iconDetails.collect{
                    adapter?.updateAdapter(it?.rasterSizes?.filterNotNull() ?: emptyList())
                }

            }

        }

    }


}