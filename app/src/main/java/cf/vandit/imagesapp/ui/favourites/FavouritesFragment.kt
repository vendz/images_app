package cf.vandit.imagesapp.ui.favourites

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import cf.vandit.imagesapp.utils.ItemOnClickListener
import cf.vandit.imagesapp.R
import cf.vandit.imagesapp.adapters.ItemAdapter
import cf.vandit.imagesapp.data.database.FavouriteDatabase
import cf.vandit.imagesapp.databinding.FragmentFavouritesBinding
import cf.vandit.imagesapp.data.models.ImageData
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouritesFragment : Fragment(), ItemOnClickListener {

    lateinit var binding: FragmentFavouritesBinding

    lateinit var images: List<ImageData>
    lateinit var lastItem: ImageData

//    private val adapter = ItemAdapter(requireContext()).also { it.setCallback(this) }
    private lateinit var adapter: ItemAdapter
    private val handler = Handler()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title ="Favourites"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = ItemAdapter(requireContext()).also { it.setCallback(this) }
        binding.favRecView.adapter = adapter

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.favRecView)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.favRecView.layoutManager = layoutManager

        binding.favRecView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val indexRv = (binding.favRecView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                if(indexRv!=0){
                    binding.scrollToTopBtn.visibility = View.VISIBLE
                } else {
                    binding.scrollToTopBtn.visibility = View.GONE
                }
            }
        })

        binding.scrollToTopBtn.setOnClickListener{
            binding.favRecView.smoothScrollToPosition(0)
            binding.favRecView.smoothScrollBy(5,0)
        }

        lifecycleScope.launch(Dispatchers.IO){
            getImages()

            val monitor = Runnable {
                if(images.isEmpty()){
                    binding.favRecView.visibility = View.GONE
                    binding.emptyFavTv.visibility = View.VISIBLE
                    binding.animationView.visibility = View.VISIBLE
                }
            }
            handler.postDelayed(monitor, 0)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController())
                || super.onOptionsItemSelected(item)
    }

    private fun getImages(){
        val database = Room.databaseBuilder(requireContext(), FavouriteDatabase::class.java, "favouriteDB").build()
        images = database.favouriteDao().getAllImages()
        val monitor = Runnable {
            adapter.submitList(images)
        }
        handler.postDelayed(monitor, 0)
    }

    override fun onClick(item: ImageData, v: ImageButton) {
        val database = Room.databaseBuilder(requireContext(), FavouriteDatabase::class.java, "favouriteDB").build()
        lifecycleScope.launch {
            database.favouriteDao().deleteImage(item)
        }

        v.setImageDrawable(context?.getDrawable(R.drawable.ic_star_border))
        item.liked_by_user = false

        lastItem = item
        Snackbar.make(requireView(), "${item.user.name} removed from favourites", Snackbar.LENGTH_LONG)
            .setAction("Undo"){
                lifecycleScope.launch(Dispatchers.Main){
                    item.liked_by_user = true
                    database.favouriteDao().insertImage(item)
                    v.setImageDrawable(context?.getDrawable(R.drawable.ic_star_filled))
                }
            }.show()
    }
}