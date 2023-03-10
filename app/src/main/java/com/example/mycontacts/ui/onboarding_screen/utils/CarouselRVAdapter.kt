package com.example.mycontacts.ui.onboarding_screen.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycontacts.databinding.ItemCarouselBinding

class CarouselRVAdapter(
    private val carouselDataList: ArrayList<String>,
) : RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

    class CarouselItemViewHolder(
        val binding: ItemCarouselBinding
    ) : RecyclerView.ViewHolder(binding.root)

    // single viewType
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCarouselBinding.inflate(inflater, parent, false)

        return CarouselItemViewHolder(binding)
    }

    override fun getItemCount(): Int = carouselDataList.size

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        val data = carouselDataList[position]
        holder.binding.textView.text = data
    }


}