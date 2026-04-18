package br.edu.utfpr.usandohttp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.UserManager
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView
    private lateinit var tvEndereco: TextView
    private lateinit var btBuscarEndereco: Button
    private lateinit var progressBar: ProgressBar

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
        progressBar = findViewById(R.id.progressBar)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)

    }

    fun btBuscarEnderecoOnClick(view: View) {

        val latittude = tvLatitude.text.toString()
        val longitude = tvLongitude.text.toString()
        val GOOGLE_API_KEY = "AIzaSyDsy454kAkXofX828BEMieAQ7EbtpjohZY"

        if (latittude == "Não conectado" || longitude == "Não conectado") {
            tvEndereco.text = "Localização não encontrada"
            return
        }

        Thread {
            runOnUiThread {
                btBuscarEndereco.isEnabled = false
                progressBar.visibility = View.VISIBLE
            }

            try {
                val endereco = "https://maps.googleapis.com/maps/api/geocode/json?latlng=${latittude},${longitude}&key=${GOOGLE_API_KEY}"

                val url = URL(endereco)
                val urlConnection = url.openConnection()

                val inputStream = urlConnection.getInputStream() //linha bloqueante (espera)

                val entrada = BufferedReader(InputStreamReader(inputStream))

                val saida = StringBuilder()

                var linha = entrada.readLine()

                while (linha != null) {
                    saida.append(linha)
                    linha = entrada.readLine()
                }

                val dado = extrairEndereco(saida.toString())


                runOnUiThread {
                    tvEndereco.text = dado
                }


            } catch (e: Exception) {
                runOnUiThread {
                    tvEndereco.text = e.message
                }
            } finally {
                runOnUiThread {
                    btBuscarEndereco.isEnabled = true
                    progressBar.visibility = View.GONE
                }
            }



            //executo o código de IO



        }.start()
    }

    fun extrairEndereco(dados: String): String {

        val jsonElement = JsonParser.parseString(dados)
        val resultado = jsonElement.asJsonObject.getAsJsonArray("results")

        val formattedAddress = resultado[0].asJsonObject.get( "formatted_address").asString

        return formattedAddress
    }

    override fun onLocationChanged(location: Location) {
        tvLatitude.text = location.latitude.toString()
        tvLongitude.text = location.longitude.toString()
    }
}