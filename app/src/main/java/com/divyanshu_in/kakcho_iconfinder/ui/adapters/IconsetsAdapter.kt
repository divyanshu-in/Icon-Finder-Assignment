package com.divyanshu_in.kakcho_iconfinder.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divyanshu_in.kakcho_iconfinder.databinding.CategoriesItemBinding
import com.divyanshu_in.kakcho_iconfinder.databinding.IconSetItemsBinding
import com.divyanshu_in.kakcho_iconfinder.databinding.LoadingItemBinding
import com.divyanshu_in.kakcho_iconfinder.models.ListItemSetsInCategoryData

const val ICONSETS_VIEW = 0

class IconsetsAdapter(private val context: Context, private val onItemClick: (iconset_id: Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder> (){

    private var iconsetsList: ArrayList<ListItemSetsInCategoryData.Iconset> = arrayListOf()
    private var isEndReached = false

    class IconsetsViewHolder(var binding: IconSetItemsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == ICONSETS_VIEW){
            IconsetsViewHolder(IconSetItemsBinding.inflate(LayoutInflater.from(context), parent, false))
        }else{
            CategoriesAdapter.LoadingViewHolder(LoadingItemBinding.inflate(LayoutInflater.from(
                context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(position < iconsetsList.size){
            val iconset = iconsetsList[position]
            (holder as IconsetsViewHolder).binding.apply {
                tvAuthor.text = iconset.author?.name ?: "UNKNOWN AUTHOR"
                tvTitle.text = iconset.name ?: "UNKNOWN TITLE"
                tvNumber.text = "${iconset.iconsCount.toString()} icons in this set"
                root.setOnClickListener {
                    iconset.iconsetId?.let {
                        onItemClick.invoke(it)
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int = if(!isEndReached) iconsetsList.size + 1 else iconsetsList.size

    override fun getItemViewType(position: Int): Int = if(position < iconsetsList.size) ICONSETS_VIEW else LOADING_VIEW

    fun updateAdapter(iconsets: List<ListItemSetsInCategoryData.Iconset?>){
        iconsetsList.addAll(iconsets.filterNotNull())
        notifyDataSetChanged()
    }

    fun endReached(){
        isEndReached = true
        notifyDataSetChanged()
    }


}

