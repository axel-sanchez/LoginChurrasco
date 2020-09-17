package com.example.loginchurrasco.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loginchurrasco.data.models.Site
import com.example.loginchurrasco.databinding.ItemSiteBinding

/**
 * Clase que adapta el recyclerview de [SitiesFragment]
 * @author Axel Sanchez
 */
class SitiesAdapter(
    var mItems: List<Site>,
    private var itemClick: (Site) -> Unit) : RecyclerView.Adapter<SitiesAdapter.ViewHolder>() {

    private var mFilteredList = mItems

    class ViewHolder(private val binding: ItemSiteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Site, itemClick: (Site) -> Unit) {
            binding.image.setOnClickListener { itemClick(item) }
            binding.title.setOnClickListener { itemClick(item) }

            item.let{ site ->
                site.url_imagen?.let { urlImage ->
                    if( urlImage.isNotEmpty()){
                        Glide.with(itemView.context)
                            .load(urlImage)
                            .into(binding.image)
                    }
                }
            }

            binding.title.text = item.nombre
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val recyclerRowBinding: ItemSiteBinding = ItemSiteBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(recyclerRowBinding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(
        mFilteredList[position],
        itemClick
    )

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mFilteredList.size

    fun setItems(newItems: List<Site>) {
        mItems = newItems
    }

    fun getItems(): List<Site?> {
        return mFilteredList
    }
}