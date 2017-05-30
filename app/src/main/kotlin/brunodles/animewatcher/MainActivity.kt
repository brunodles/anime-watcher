package brunodles.animewatcher

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import brunodles.animewatcher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (intent.data != null)
            binding?.text?.text = intent.data.toString()
        else if (intent.hasExtra(Intent.EXTRA_TEXT))
            binding?.text?.text = intent.getStringExtra(Intent.EXTRA_TEXT)
    }
}
