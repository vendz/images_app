package cf.vandit.imagesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import cf.vandit.imagesapp.adapters.ItemAdapter
import cf.vandit.imagesapp.database.Favourite
import cf.vandit.imagesapp.databinding.FragmentFavouritesBinding
import cf.vandit.imagesapp.network.ImageData

class FavouritesFragment : Fragment(), ItemOnClickListener {

    lateinit var binding: FragmentFavouritesBinding

    private val imageList = mutableListOf<Favourite>()
    private val adapter = ItemAdapter().also { it.setCallback(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onClick(item: ImageData, v: ImageButton) {}
}