package cf.vandit.imagesapp.fragments

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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import cf.vandit.imagesapp.Constants
import cf.vandit.imagesapp.ItemOnClickListener
import cf.vandit.imagesapp.R
import cf.vandit.imagesapp.RetrofitService
import cf.vandit.imagesapp.adapters.ItemAdapter
import cf.vandit.imagesapp.database.FavouriteDatabase
import cf.vandit.imagesapp.databinding.FragmentMainBinding
import cf.vandit.imagesapp.network.ImageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class MainFragment: Fragment(), ItemOnClickListener {
    lateinit var binding: FragmentMainBinding
    lateinit var adapter: ItemAdapter
    lateinit var database: FavouriteDatabase

    private val imageList = mutableListOf<ImageData>()
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

        database = Room.databaseBuilder(requireContext(), FavouriteDatabase::class.java, "favouriteDB").build()

        adapter = ItemAdapter(requireContext()).also { it.setCallback(this) }
//        getImages()

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.recView)
//        adapter.submitList(imageList)
        binding.recView.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recView.layoutManager = layoutManager

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
                        currentPage += 1
                        getImages()
                        loading = true;
                    }
                }
            }
        })

        binding.scrollToTopBtn.setOnClickListener{
            binding.recView.smoothScrollToPosition(0)
            binding.recView.smoothScrollBy(5,0)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            getImages()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.errorBtn.setOnClickListener {
//            binding.progressBar.visibility = View.VISIBLE
            binding.swipeRefreshLayout.isRefreshing = true
            getImages()
        }
    }

    private fun getImages() {
        binding.errorTextView.visibility = View.GONE
        binding.errorBtn.visibility = View.GONE

        var service: RetrofitService = RetrofitService.create()

        lifecycleScope.launch{
            try {
                val response = service.getImages(Constants.clientId, currentPage)
                if (response.isSuccessful) {
                    if(currentPage == 1){
                        imageList.clear()
                    }
                    response.body()?.let {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            for(res in responseBody){
                                Log.d("TAG", "getImages: ${res.id}")
                                if(database.favouriteDao().getImage(res.id) != null){
                                    res.liked_by_user = true
                                }
                            }
                            imageList.addAll(responseBody)
                        }
//                        binding.recView.adapter!!.notifyDataSetChanged()
                        adapter.submitList(imageList.toMutableList())
//                        binding.progressBar.visibility = View.GONE
                        binding.swipeRefreshLayout.isRefreshing = false
                        loading = false
                    }
                } else {
                    Log.d("TAG", response.message())
                    Toast.makeText(requireContext(), "error loading data...", Toast.LENGTH_LONG).show()
                }
            } catch (e: UnknownHostException) {
                if (imageList.isEmpty()){
//                    binding.progressBar.visibility = View.GONE
                    binding.errorTextView.visibility = View.VISIBLE
                    binding.errorBtn.visibility = View.VISIBLE
                    binding.swipeRefreshLayout.isRefreshing = false;
                } else {
                    Log.e("TAG", e.stackTraceToString())
                    Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception){
                Log.e("TAG", e.stackTraceToString())
                Toast.makeText(requireContext(), "An Exception Occurred", Toast.LENGTH_LONG).show()
            }
        }

//        val response1 = service.getImages1()
//        response1.enqueue(object: Callback<List<ImageData>>{
//            override fun onResponse(
//                call: Call<List<ImageData>>,
//                response: Response<List<ImageData>>
//            ) {
//                val responseBody = response.body()
//                if (responseBody != null) {
//                    imageList.addAll(responseBody)
//                }
//                binding.recView.adapter!!.notifyDataSetChanged()
//                binding.progressBar.visibility = View.GONE
//            }
//
//            override fun onFailure(call: Call<List<ImageData>>, t: Throwable) {
//                Log.d("TAG", t.stackTraceToString())
//            }
//        })
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefreshLayout.isRefreshing = true
        getImages()
    }

    override fun onClick(item: ImageData, v: ImageButton) {
        lifecycleScope.launch(Dispatchers.Main) {
            if(database.favouriteDao().getImage(item.id) == null){
                item.liked_by_user = true
                database.favouriteDao().insertImage(item)
                v.setImageDrawable(context?.getDrawable(R.drawable.ic_star_filled))
            } else {
                item.liked_by_user = false
                database.favouriteDao().deleteImage(item)
                v.setImageDrawable(context?.getDrawable(R.drawable.ic_star_border))
            }
        }
    }
}