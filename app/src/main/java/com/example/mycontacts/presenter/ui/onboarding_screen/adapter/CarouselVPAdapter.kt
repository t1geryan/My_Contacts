package com.example.mycontacts.presenter.ui.onboarding_screen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycontacts.databinding.ItemCarouselBinding

typealias OnCarouselItemClickListener = () -> Unit

class CarouselVPAdapter(
    private val carouselDataList: Array<String>,
    private val listener: OnCarouselItemClickListener = {}
) : RecyclerView.Adapter<CarouselVPAdapter.CarouselItemViewHolder>(), OnClickListener {

    class CarouselItemViewHolder(
        val binding: ItemCarouselBinding
    ) : RecyclerView.ViewHolder(binding.root)

    // single viewType
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCarouselBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return CarouselItemViewHolder(binding)
    }

    override fun getItemCount(): Int = carouselDataList.size

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        val data = carouselDataList[position]
        holder.binding.textView.text = data
    }

    override fun onClick(view: View) {
        listener()
    }
}