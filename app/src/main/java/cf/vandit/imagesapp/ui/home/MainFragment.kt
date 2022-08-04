package cf.vandit.imagesapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import cf.vandit.imagesapp.utils.Constants
import cf.vandit.imagesapp.utils.ItemOnClickListener
import cf.vandit.imagesapp.R
import cf.vandit.imagesapp.data.network.service.RetrofitService
import cf.vandit.imagesapp.adapters.ItemAdapter
import cf.vandit.imagesapp.data.database.FavouriteDatabase
import cf.vandit.imagesapp.databinding.FragmentMainBinding
import cf.vandit.imagesapp.data.models.ImageData
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.UnknownHostException

class MainFragment: Fragment(), ItemOnClickListener {
    lateinit var binding: FragmentMainBinding
    private lateinit var viewmodel: MainViewModel
    private lateinit var adapter: ItemAdapter
    private lateinit var layoutManager: LinearLayoutManager

    lateinit var database: FavouriteDatabase
    lateinit var navController: NavController

    lateinit var lastItem: ImageData

    private var imageList = mutableListOf<ImageData>()
    private var currentPage: Int = 1
    private var loading: Boolean = true
//    private val adapter = ItemAdapter().also { it.setCallback(this) }

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

        database = Room.databaseBuilder(requireContext(), FavouriteDatabase::class.java, "favouriteDB").build()
        viewmodel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        adapter = ItemAdapter(requireContext()).also { it.setCallback(this) }
//        getImages()

//        Log.d("MainFragment", "onViewCreated Network Call")
//        viewmodel.fetchImages(currentPage)
//        observeViewModel()

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.recView)
//        adapter.submitList(imageList)
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
            Log.d("MainFragment", "swipeRefreshLayout Network Call")
            viewmodel.fetchImages(currentPage)
            observeViewModel()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.errorBtn.setOnClickListener {
//            binding.progressBar.visibility = View.VISIBLE
            binding.swipeRefreshLayout.isRefreshing = true
            viewmodel.fetchImages(currentPage)
            observeViewModel()
        }
    }

//    private fun getImages() {
//        binding.errorTextView.visibility = View.GONE
//        binding.errorBtn.visibility = View.GONE
//
////        val service = RetrofitService.getRetroInstance()
//
////        lifecycleScope.launch{
////            try {
////                val response = service.getImages(Constants.clientId, currentPage)
////                if (response.isSuccessful) {
////                    if(currentPage == 1){
////                        imageList.clear()
////                    }
////                    response.body()?.let {
////                        val responseBody = response.body()
////                        if (responseBody != null) {
////                            for(res in responseBody){
////                                Log.d("TAG", "getImages: ${res.id}")
////                                if(database.favouriteDao().getImage(res.id) != null){
////                                    res.liked_by_user = true
////                                }
////                            }
////                            imageList.addAll(responseBody)
////                        }
//////                        binding.recView.adapter!!.notifyDataSetChanged()
////                        adapter.submitList(imageList.toMutableList())
//////                        binding.progressBar.visibility = View.GONE
////                        binding.swipeRefreshLayout.isRefreshing = false
////                        loading = false
////                    }
////                } else {
////                    Log.d("TAG", response.message())
////                    Toast.makeText(requireContext(), "error loading data...", Toast.LENGTH_LONG).show()
////                }
////            } catch (e: UnknownHostException) {
////                if (imageList.isEmpty()){
//////                    binding.progressBar.visibility = View.GONE
////                    binding.errorTextView.visibility = View.VISIBLE
////                    binding.errorBtn.visibility = View.VISIBLE
////                    binding.swipeRefreshLayout.isRefreshing = false;
////                } else {
////                    Log.e("TAG", e.stackTraceToString())
////                    Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
////                }
////            } catch (e: Exception){
////                Log.e("TAG", e.stackTraceToString())
////                Toast.makeText(requireContext(), "An Exception Occurred", Toast.LENGTH_LONG).show()
////            }
////        }
//    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefreshLayout.isRefreshing = true
        Log.d("MainFragment", "onStart Network Call")
        viewmodel.fetchImages(currentPage)
        observeViewModel()
    }

    private fun observeViewModel(){
        viewmodel.images.observe(viewLifecycleOwner) { images ->
            images?.let {
                imageList += it.toMutableList()
                Log.d("MainFragment", "observeViewModel: ${it.size}")
                adapter.submitList(imageList.toMutableList())
                loading = false
                binding.swipeRefreshLayout.isRefreshing = false

                binding.recView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val indexRv = (binding.recView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                        if(indexRv!=0){
                            binding.scrollToTopBtn.visibility = View.VISIBLE
                        } else {
                            binding.scrollToTopBtn.visibility = View.GONE
                        }

                        if(!loading){
                            if(layoutManager.findLastCompletelyVisibleItemPosition() == imageList.size-1) {
                                currentPage++
                                loading = true
                                Log.d("MainFragment", "addOnScrollListener Network Call")
                                viewmodel.fetchImages(currentPage)
                                observeViewModel()
                            }
                        }
                    }
                })
            }
        }
    }

    override fun onClick(item: ImageData, v: ImageButton) {
        lifecycleScope.launch(Dispatchers.Main) {
            if(database.favouriteDao().getImage(item.id) == null){
                item.liked_by_user = true
                database.favouriteDao().insertImage(item)
                v.setImageDrawable(context?.getDrawable(R.drawable.ic_star_filled))
                Snackbar.make(requireView(), "${item.user.name} added to favourites", Snackbar.LENGTH_LONG)
                    .setAction("View Saved"){
                        lifecycleScope.launch(Dispatchers.Main) {
                            navController.navigate(R.id.action_mainFragment_to_favouritesFragment)
                        }
                    }.show()
            } else {
                item.liked_by_user = false
                database.favouriteDao().deleteImage(item)
                v.setImageDrawable(context?.getDrawable(R.drawable.ic_star_border))
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
    }
}