package cf.vandit.imagesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_view.view.*

class ItemAdapter(var items: List<ItemData>): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
        holder.itemView.apply {
            item_title.text = items[position].title
            item_address.text = items[position].address
            item_desc.text = items[position].desc
            Glide.with(this)
                .load(items[position].image)
                .into(item_imageView)

            if(items[position].isFav){
                item_favBtn.setImageResource(R.drawable.ic_star_filled)
            } else {
                item_favBtn.setImageResource(R.drawable.ic_star_border)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}