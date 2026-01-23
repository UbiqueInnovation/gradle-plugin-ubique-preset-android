package ch.ubique.preset.example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		Log.i("plugin verification", "IS_FLAVOR_PROD=${BuildConfig.IS_FLAVOR_PROD}")
		Log.i("plugin verification", "IS_FLAVOR_DEV=${BuildConfig.IS_FLAVOR_DEV}")
	}

}