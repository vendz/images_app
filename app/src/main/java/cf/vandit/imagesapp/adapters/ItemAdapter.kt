package cf.vandit.imagesapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cf.vandit.imagesapp.ItemOnClickListener
import cf.vandit.imagesapp.databinding.ItemViewBinding
import cf.vandit.imagesapp.network.ImageData
import com.bumptech.glide.Glide

class ItemAdapter: ListAdapter<ImageData, ItemAdapter.ItemViewHolder>(DiffUtil()) {
    private lateinit var callback: ItemOnClickListener

    fun setCallback(callback: ItemOnClickListener){
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, callback)
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<ImageData>(){
        override fun areItemsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
            return oldItem == newItem
        }
    }

    class ItemViewHolder(private val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: ImageData, callback: ItemOnClickListener){
            binding.listItem = item
            binding.callback = callback
        }
    }
}

@BindingAdapter("loadImage")
fun loadImage(item_imageView: ImageView, url: String){
    Glide.with(item_imageView)
        .load(url)
        .centerCrop()
        .into(item_imageView)
}

@BindingAdapter("setCallback", "imageData")
fun setCallback(v: ImageButton, callback: ItemOnClickListener, imageData: ImageData){
    v.setOnClickListener { callback.onClick(imageData, v) }
}