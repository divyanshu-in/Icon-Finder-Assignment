package com.divyanshu_in.kakcho_iconfinder.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divyanshu_in.kakcho_iconfinder.databinding.CategoriesItemBinding
import com.divyanshu_in.kakcho_iconfinder.databinding.LoadingItemBinding
import com.divyanshu_in.kakcho_iconfinder.models.ListCategoriesData

const val CATEGORY_VIEW = 0
const val LOADING_VIEW = 1

class CategoriesAdapter(
    private val context: Context,
    private val onCategoryClick: (categoryName: String, categoryIdentifier: String) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var categoryList: ArrayList<ListCategoriesData.Category> = arrayListOf()

    private var isEndReached = false

    class CategoriesViewHolder(var binding: CategoriesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class LoadingViewHolder(var binding: LoadingItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == CATEGORY_VIEW) {
            CategoriesViewHolder(CategoriesItemBinding.inflate(LayoutInflater.from(context),
                parent,
                false))
        } else {
            LoadingViewHolder(LoadingItemBinding.inflate(LayoutInflater.from(context),
                parent,
                false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (position < categoryList.size) {
            (holder as CategoriesViewHolder).binding.apply {
                val category = categoryList[position]
                textView.text = category.name
                root.setOnClickListener {
                    onCategoryClick.invoke(category.name, category.identifier)
                }

            }


        }

    }

    override fun getItemCount(): Int =
        if (isEndReached) categoryList.size else categoryList.size + 1

    override fun getItemViewType(position: Int): Int =
        if (position < categoryList.size) CATEGORY_VIEW else LOADING_VIEW

    fun updateAdapter(categories: List<ListCategoriesData.Category>) {
        categoryList.addAll(categories)
        notifyDataSetChanged()
    }

    fun endReached() {
        isEndReached = true
        notifyDataSetChanged()
    }


}

