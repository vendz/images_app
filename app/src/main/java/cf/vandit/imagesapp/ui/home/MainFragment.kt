package cf.vandit.imagesapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import cf.vandit.imagesapp.R
import cf.vandit.imagesapp.adapters.ItemAdapter
import cf.vandit.imagesapp.data.models.ImageData
import cf.vandit.imagesapp.databinding.FragmentMainBinding
import cf.vandit.imagesapp.utils.ItemOnClickListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment: Fragment(), ItemOnClickListener {
    lateinit var binding: FragmentMainBinding
    private lateinit var viewmodel: MainViewModel
    private lateinit var adapter: ItemAdapter
    private lateinit var layoutManager: LinearLayoutManager

    lateinit var navController: NavController

    private var imageList = mutableListOf<ImageData>()
    private var currentPage: Int = 1
    private var loading: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title ="Home"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        viewmodel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        adapter = ItemAdapter().also { it.setCallback(this) }

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.recView)
        binding.recView.adapter = adapter
        layoutManager = LinearLayoutManager(requireContext())
        binding.recView.layoutManager = layoutManager

        binding.scrollToTopBtn.setOnClickListener{
            binding.recView.smoothScrollToPosition(0)
            binding.recView.smoothScrollBy(5,0)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            imageList.clear()
            currentPage = 1
            viewmodel.fetchImages(currentPage)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.errorBtn.setOnClickListener {
            binding.swipeRefreshLayout.isRefreshing = true
            viewmodel.fetchImages(currentPage)
        }

        if(viewmodel.images.value == null){
            viewmodel.fetchImages(currentPage)
        } else {
            imageList.addAll(viewmodel.images.value!!)
            adapter.submitList(imageList)
        }
        observeViewModel()
    }

    private fun observeViewModel(){
        binding.recView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val indexRv =
                    (binding.recView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                if (indexRv != 0) {
                    binding.scrollToTopBtn.visibility = View.VISIBLE
                } else {
                    binding.scrollToTopBtn.visibility = View.GONE
                }

                if (!loading) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == imageList.size - 1) {
                        currentPage++
                        loading = true
                        viewmodel.fetchImages(currentPage)
                    }
                }
            }
        })
        viewmodel.images.observe(viewLifecycleOwner) { images ->
            images?.let {
                imageList += it.toMutableList()
                adapter.submitList(imageList.toMutableList())
                loading = false
                binding.swipeRefreshLayout.isRefreshing = false

            }
        }
    }

    override fun onClick(item: ImageData, v: ImageButton) {
        lifecycleScope.launch(Dispatchers.Main) {
            if(!viewmodel.isImageLiked(item)){
                viewmodel.insertImage(item)
                v.setImageDrawable(context?.getDrawable(R.drawable.ic_star_filled))
                Snackbar.make(requireView(), "${item.user.name} added to favourites", Snackbar.LENGTH_LONG)
                    .setAction("View Saved"){
                       if(navController.currentDestination?.id != R.id.favouritesFragment){
                           navController.navigate(R.id.action_mainFragment_to_favouritesFragment)
                       }
                    }.show()
            } else {
                viewmodel.deleteImage(item)
                v.setImageDrawable(context?.getDrawable(R.drawable.ic_star_border))
                Snackbar.make(requireView(), "${item.user.name} removed from favourites", Snackbar.LENGTH_LONG)
                    .setAction("Undo"){
                        viewmodel.insertImage(item)
                        v.setImageDrawable(context?.getDrawable(R.drawable.ic_star_filled))
                    }.show()
            }
        }
    }
}