package cf.vandit.imagesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import cf.vandit.imagesapp.R
import cf.vandit.imagesapp.data.database.FavouriteDatabase
import cf.vandit.imagesapp.data.models.ImageData
import cf.vandit.imagesapp.databinding.ItemViewBinding
import cf.vandit.imagesapp.utils.ItemOnClickListener
import com.bumptech.glide.Glide

class ItemAdapter: ListAdapter<ImageData, ItemAdapter.ItemViewHolder>(DiffUtil()) {
    private lateinit var database: RoomDatabase
    private lateinit var callback: ItemOnClickListener

    fun setCallback(callback: ItemOnClickListener){
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        database = Room.databaseBuilder(parent.context, FavouriteDatabase::class.java, "favouriteDB").build()
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

    inner class ItemViewHolder(private val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: ImageData, callback: ItemOnClickListener){
            binding.listItem = item
            binding.callback = callback

            if(item.liked_by_user == true) {
                binding.itemFavBtn.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_star_filled))
            } else {
                binding.itemFavBtn.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_star_border))
            }
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