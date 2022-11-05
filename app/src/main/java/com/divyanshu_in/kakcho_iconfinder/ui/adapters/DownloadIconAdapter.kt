package com.divyanshu_in.kakcho_iconfinder.ui.adapters

import android.content.Context
import android.graphics.drawable.Icon
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.divyanshu_in.kakcho_iconfinder.R
import com.divyanshu_in.kakcho_iconfinder.databinding.CategoriesItemBinding
import com.divyanshu_in.kakcho_iconfinder.databinding.IconSetItemsBinding
import com.divyanshu_in.kakcho_iconfinder.databinding.IconsItemBinding
import com.divyanshu_in.kakcho_iconfinder.databinding.LoadingItemBinding
import com.divyanshu_in.kakcho_iconfinder.models.IconDetails
import com.divyanshu_in.kakcho_iconfinder.models.ListAllIconsInIconSetData
import com.divyanshu_in.kakcho_iconfinder.models.ListItemSetsInCategoryData
import com.divyanshu_in.kakcho_iconfinder.utils.visible

class DownloadIconAdapter(private val context: Context, private val onItemClick: (downloadUrl: String) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder> (){

    private var rasterList: ArrayList<IconDetails.RasterSize> = arrayListOf()

    class IconsViewHolder(var binding: IconsItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return IconsViewHolder(IconsItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(position < rasterList.size){
            val icon = rasterList[position]
            (holder as IconsViewHolder).binding.apply {
                ivIconPreview.load(icon.formats?.first()?.previewUrl)

                imageButton.setImageResource(R.drawable.ic_download)

                tvSizeDetail.visible()

                tvSizeDetail.text = "size: ${icon.sizeHeight} X ${icon.sizeWidth}"

                root.setOnClickListener {
                    icon.formats?.first()?.downloadUrl?.let{
                        onItemClick.invoke(it)

                    }
                }

            }
        }

    }

    override fun getItemCount(): Int = rasterList.size


    fun updateAdapter(iconsets: List<IconDetails.RasterSize>){
        rasterList.addAll(iconsets)
        notifyDataSetChanged()
    }


}

