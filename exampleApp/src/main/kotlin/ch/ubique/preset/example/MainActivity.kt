package ch.ubique.preset.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		BuildConfig.IS_FLAVOR_DEV
		BuildConfig.IS_FLAVOR_PROD
	}

}