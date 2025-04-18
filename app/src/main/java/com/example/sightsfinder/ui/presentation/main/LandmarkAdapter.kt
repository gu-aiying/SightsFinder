package com.example.sightsfinder.ui.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sightsfinder.databinding.RvLandmarksNearbyItemBinding
import com.example.sightsfinder.domain.model.Landmark

class LandmarkAdapter(
    private val onDetailClick: (Landmark) -> Unit
) : RecyclerView.Adapter<LandmarkAdapter.LandmarkViewHolder>() {

    private val items = mutableListOf<Landmark>()

    fun submitList(newList: List<Landmark>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandmarkViewHolder {
        val binding = RvLandmarksNearbyItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LandmarkViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LandmarkViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class LandmarkViewHolder(
        private val binding: RvLandmarksNearbyItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Landmark) {
            binding.tvLandmarkName.text = item.name
            binding.tvDistance.text = "Расстояние: ${item.distanceMeters} м"

            // Загрузить изображение, если оно есть
            Glide.with(binding.root)
                .load(item.imageUrl ?: "https://upload.wikimedia.org/wikipedia/commons/6/65/No-Image-Placeholder.svg")
                .into(binding.ivLandmark)

            binding.btDetail.setOnClickListener {
                onDetailClick(item)
            }
        }
    }
}
