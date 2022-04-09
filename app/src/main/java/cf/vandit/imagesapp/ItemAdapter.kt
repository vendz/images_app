package cf.vandit.imagesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cf.vandit.imagesapp.databinding.ItemViewBinding
import cf.vandit.imagesapp.network.ImageData
import com.bumptech.glide.Glide

class ItemAdapter: ListAdapter<ImageData, ItemAdapter.ItemViewHolder>(DiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<ImageData>(){
        override fun areItemsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
            return oldItem == newItem
        }
    }

    class ItemViewHolder(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: ImageData){
            binding.apply {
                itemTitle.text = item.user.name
                itemAddress.text = item.user.location
                itemDesc.text = item.user.bio

                Glide.with(itemImageView.context)
                    .load(item.urls.regular)
                    .into(itemImageView)

                if(item.liked_by_user){
                    itemFavBtn.setImageResource(R.drawable.ic_star_filled)
                } else {
                    itemFavBtn.setImageResource(R.drawable.ic_star_border)
                }
            }
        }
    }
}