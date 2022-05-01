package cf.vandit.imagesapp

import android.widget.ImageButton
import cf.vandit.imagesapp.network.ImageData

interface ItemOnClickListener {
    fun onClick(item: ImageData, v:ImageButton)
}