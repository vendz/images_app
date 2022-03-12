package cf.vandit.imagesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var ItemList = mutableListOf(
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", true, "https://snov.io/glossary/wp-content/uploads/2020/04/Screenshot-6.png"),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", true, "https://snov.io/glossary/wp-content/uploads/2020/04/Screenshot-6.png"),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", true, "https://snov.io/glossary/wp-content/uploads/2020/04/Screenshot-6.png"),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", true, "https://snov.io/glossary/wp-content/uploads/2020/04/Screenshot-6.png"),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", true, "https://snov.io/glossary/wp-content/uploads/2020/04/Screenshot-6.png"),
            ItemData("Hey This is Vandit", "hello world \nhello \nhi", "A demo is what you give to show how something works. You might give a demo of your fancy new espresso machine to your weekend guests, so they'll know how to use it. Demo", true, "https://snov.io/glossary/wp-content/uploads/2020/04/Screenshot-6.png")
            )

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recView)
        val adapter = ItemAdapter(ItemList)
        recView.adapter = adapter
        recView.layoutManager = LinearLayoutManager(this)
    }
}