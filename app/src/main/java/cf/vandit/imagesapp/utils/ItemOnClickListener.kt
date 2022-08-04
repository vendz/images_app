package cf.vandit.imagesapp.utils

import android.widget.ImageButton
import cf.vandit.imagesapp.data.models.ImageData

interface ItemOnClickListener {
    fun onClick(item: ImageData, v:ImageButton)
}