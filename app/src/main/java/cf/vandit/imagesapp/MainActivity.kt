package cf.vandit.imagesapp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import cf.vandit.imagesapp.databinding.ActivityMainBinding
import cf.vandit.imagesapp.network.ImageData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val imageList = mutableListOf<ImageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar

        // here "!!" before title is a null safety check
        actionBar!!.title = "Home"
        getImages()

        var itemList = mutableListOf(
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", false, "https://snov.io/glossary/wp-content/uploads/2020/04/Screenshot-6.png"),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", false, "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg"),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", false, "https://cdn.pixabay.com/photo/2021/08/25/20/42/field-6574455__340.jpg"),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", false, "https://media.istockphoto.com/photos/hot-air-balloons-flying-over-the-botan-canyon-in-turkey-picture-id1297349747?b=1&k=20&m=1297349747&s=170667a&w=0&h=oH31fJty_4xWl_JQ4OIQWZKP8C6ji9Mz7L4XmEnbqRU="),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", false, "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/beach-quotes-1559667853.jpg"),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", false, "https://images.freeimages.com/images/large-previews/84f/weathered-blue-door-1538466.jpg")
            )

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.recView)
        val adapter = ItemAdapter(imageList)
        binding.recView.adapter = adapter
        binding.recView.layoutManager = LinearLayoutManager(this)
        Log.d("TAG", "onCreate: ")

        binding.recView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val indexRv = (binding.recView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                if(indexRv!=0){
                    binding.scrollToTopBtn.visibility = View.VISIBLE
                } else {
                    binding.scrollToTopBtn.visibility = View.GONE
                }
            }
        })

        binding.scrollToTopBtn.setOnClickListener{
            binding.recView.smoothScrollToPosition(0)
            binding.recView.smoothScrollBy(5,0)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            getImages()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getImages() {
        imageList.clear()
        val images = RetrofitService.api.getImages()
        images.enqueue(object: Callback<List<ImageData>>{
            override fun onResponse(
                call: Call<List<ImageData>>,
                response: Response<List<ImageData>>
            ) {
                val responseBody = response.body()
                if (responseBody != null) {
                    imageList.addAll(responseBody)
                }
                binding.recView.adapter!!.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<ImageData>>, t: Throwable) {
                Log.d("TAG", t.stackTraceToString())
            }
        })
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