package cf.vandit.imagesapp.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import cf.vandit.imagesapp.R
import cf.vandit.imagesapp.adapters.ItemAdapter
import cf.vandit.imagesapp.data.models.ImageData
import cf.vandit.imagesapp.databinding.FragmentFavouritesBinding
import cf.vandit.imagesapp.utils.ItemOnClickListener
import com.google.android.material.snackbar.Snackbar

class FavouritesFragment : Fragment(), ItemOnClickListener {

    lateinit var binding: FragmentFavouritesBinding
    private lateinit var viewmodel: FavouritesViewModel
    private var images = mutableListOf<ImageData>()
    private lateinit var adapter: ItemAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title = "Favourites"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewmodel = ViewModelProvider(requireActivity())[FavouritesViewModel::class.java]

        adapter = ItemAdapter().also { it.setCallback(this) }
        binding.favRecView.adapter = adapter

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.favRecView)

        layoutManager = LinearLayoutManager(requireContext())
        binding.favRecView.layoutManager = layoutManager

        binding.favRecView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val indexRv =
                    (binding.favRecView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                if (indexRv != 0) {
                    binding.scrollToTopBtn.visibility = View.VISIBLE
                } else {
                    binding.scrollToTopBtn.visibility = View.GONE
                }
            }
        })

        binding.scrollToTopBtn.setOnClickListener {
            binding.favRecView.smoothScrollToPosition(0)
            binding.favRecView.smoothScrollBy(5, 0)
        }

        viewmodel.result.observe(viewLifecycleOwner) {
            images = it.reversed().toMutableList()
            adapter.submitList(images)

            // TODO: implement scroll to top here as user have to manually scoll to top to see the last updated item

            if (images.isEmpty()) {
                binding.favRecView.visibility = View.GONE
                binding.emptyFavTv.visibility = View.VISIBLE
                binding.animationView.visibility = View.VISIBLE
            } else {
                binding.favRecView.visibility = View.VISIBLE
                binding.emptyFavTv.visibility = View.GONE
                binding.animationView.visibility = View.GONE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onClick(item: ImageData, v: ImageButton) {
        viewmodel.deleteImage(item)
        v.setImageDrawable(context?.getDrawable(R.drawable.ic_star_border))

        Snackbar.make(requireView(), "${item.user.name} removed from favourites", Snackbar.LENGTH_LONG)
            .setAction("Undo"){
                viewmodel.insertImage(item)
                v.setImageDrawable(context?.getDrawable(R.drawable.ic_star_filled))
            }.show()
    }
}