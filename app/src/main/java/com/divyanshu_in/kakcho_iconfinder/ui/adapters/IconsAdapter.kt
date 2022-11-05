package com.divyanshu_in.kakcho_iconfinder.ui.adapters

import android.content.Context
import android.graphics.drawable.Icon
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
import com.divyanshu_in.kakcho_iconfinder.models.ListAllIconsInIconSetData
import com.divyanshu_in.kakcho_iconfinder.models.ListItemSetsInCategoryData

class IconsAdapter(private val context: Context, private val onItemClick: (icon_id: Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder> (){

    private var iconsList: ArrayList<ListAllIconsInIconSetData.Icon> = arrayListOf()

    class IconsViewHolder(var binding: IconsItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == ICONSETS_VIEW){
            IconsViewHolder(IconsItemBinding.inflate(LayoutInflater.from(context), parent, false))
        }else{
            CategoriesAdapter.LoadingViewHolder(LoadingItemBinding.inflate(LayoutInflater.from(
                context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(position < iconsList.size){
            val icon = iconsList[position]
            (holder as IconsViewHolder).binding.apply {
                ivIconPreview.load(icon.rasterSizes.getOrElse(9){ icon.rasterSizes.last() }.formats.first().previewUrl)



                imageButton.apply {
                    isEnabled = if(icon.isPremium){
                        setImageResource(R.drawable.ic_premium)
                        false
                    }else{
                        setImageResource(R.drawable.ic_download)
                        true
                    }

                    setOnClickListener {
                        onItemClick.invoke(icon.iconId)
                    }
                }

            }
        }

    }

    override fun getItemCount(): Int = iconsList.size + 1

    override fun getItemViewType(position: Int): Int = if(position < iconsList.size) ICONSETS_VIEW else LOADING_VIEW

    fun updateAdapter(iconsets: List<ListAllIconsInIconSetData.Icon>){
        iconsList.addAll(iconsets)
        notifyDataSetChanged()
    }


}

