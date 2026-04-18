package br.edu.utfpr.usandohttp

import android.location.LocationManager
import android.os.Bundle
import android.os.UserManager
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView
    private lateinit var tvEndereco: TextView
    private lateinit var btBuscarEndereco: Button

    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvLatitude = findViewById(R.id.tvLatitude)
        tvLongitude = findViewById(R.id.tvLongitude)
        tvEndereco = findViewById(R.id.tvEndereco)
        btBuscarEndereco = findViewById(R.id.btBuscarEndereco)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

    }

    fun btBuscarEnderecoOnClick(view: View) {

    }
}