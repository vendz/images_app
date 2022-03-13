package cf.vandit.imagesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar

        // here "!!" before title is a null safety check
        actionBar!!.title = "Home"

        var ItemList = mutableListOf(
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", false, "https://snov.io/glossary/wp-content/uploads/2020/04/Screenshot-6.png"),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", false, "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg"),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", false, "https://cdn.pixabay.com/photo/2021/08/25/20/42/field-6574455__340.jpg"),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", false, "https://media.istockphoto.com/photos/hot-air-balloons-flying-over-the-botan-canyon-in-turkey-picture-id1297349747?b=1&k=20&m=1297349747&s=170667a&w=0&h=oH31fJty_4xWl_JQ4OIQWZKP8C6ji9Mz7L4XmEnbqRU="),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", false, "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/beach-quotes-1559667853.jpg"),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", false, "https://images.freeimages.com/images/large-previews/84f/weathered-blue-door-1538466.jpg")
            )

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recView)
        val adapter = ItemAdapter(ItemList)
        recView.adapter = adapter
        recView.layoutManager = LinearLayoutManager(this)
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