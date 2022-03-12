package cf.vandit.imagesapp

data class ItemData (
    val title: String,
    val address: String,
    val desc: String,
    var isFav: Boolean,
    val image: String
)