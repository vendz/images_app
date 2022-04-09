package cf.vandit.imagesapp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import cf.vandit.imagesapp.databinding.ActivityMainBinding
import cf.vandit.imagesapp.network.ImageData
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val imageList = mutableListOf<ImageData>()
    private var currentPage: Int = 1;
    private var loading: Boolean = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar

        // here "!!" before title is a null safety check
        actionBar!!.title = "Home"
        getImages()

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.recView)
        val adapter = ItemAdapter(imageList)
        binding.recView.adapter = adapter
        var layoutManager = LinearLayoutManager(this)
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
            binding.swipeRefreshLayout.isRefreshing = true;
            getImages()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.swipeRefreshLayout.isRefreshing = true;
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
                            imageList.addAll(responseBody)
                        }
                        binding.recView.adapter!!.notifyDataSetChanged()
//                        binding.progressBar.visibility = View.GONE
                        binding.swipeRefreshLayout.isRefreshing = false;
                        loading = false
                    }
                } else {
                    Log.d("TAG", response.message())
                }
            } catch (e: UnknownHostException) {
                if (imageList.isEmpty()){
//                    binding.progressBar.visibility = View.GONE
                    binding.errorTextView.visibility = View.VISIBLE
                    binding.errorBtn.visibility = View.VISIBLE
                    binding.swipeRefreshLayout.isRefreshing = false;
                } else {
                    Log.e("TAG", e.stackTraceToString())
                }
            } catch (e: Exception){
                Log.e("TAG", e.stackTraceToString())
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> Toast.makeText(this, "Search Clicked!", Toast.LENGTH_SHORT).show()
            R.id.list -> Toast.makeText(this, "List Clicked!", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}