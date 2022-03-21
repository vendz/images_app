package cf.vandit.imagesapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.vandit.imagesapp.databinding.ItemViewBinding
import cf.vandit.imagesapp.network.ImageData
import com.bumptech.glide.Glide

class ItemAdapter(var items: List<ImageData>): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
        holder.binding.apply {
            itemTitle.text = items[position].user.name
            itemAddress.text = items[position].user.location
            itemDesc.text = items[position].user.bio

            Glide.with(itemImageView.context)
                .load(items[position].urls.regular)
                .into(itemImageView)

            if(items[position].liked_by_user){
                itemFavBtn.setImageResource(R.drawable.ic_star_filled)
            } else {
                itemFavBtn.setImageResource(R.drawable.ic_star_border)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}