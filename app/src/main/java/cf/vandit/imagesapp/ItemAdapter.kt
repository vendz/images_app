package cf.vandit.imagesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cf.vandit.imagesapp.databinding.ItemViewBinding
import com.bumptech.glide.Glide

class ItemAdapter(var items: List<ItemData>): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
        holder.binding.apply {
            itemTitle.text = items[position].title
            itemAddress.text = items[position].address
            itemDesc.text = items[position].desc

            Glide.with(itemImageView.context)
                .load(items[position].image)
                .into(itemImageView)

            if(items[position].isFav){
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