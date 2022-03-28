package com.restapi.testeapirest

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.restapi.testeapirest.data.Api
import com.restapi.testeapirest.domain.model.DogModelItem
import com.restapi.testeapirest.domain.model.Screen
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            solicitar.setOnClickListener {
                CoroutineScope(IO).launch {
                    TextMain()
                }
            }
    }

    private fun SetarText(name: String, temperament: String, Url: String) {
        nome.text = name
        temperamento.text = temperament

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap? = null
        executor.execute {
            val imageURL = Url
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
                handler.post {
                    Onlineimagem.setImageBitmap(image)
                }
            }
            //for error
            catch (e: Exception) {
                Log.d(TAG, "error: ", e)
            }
        }
    }

    private suspend fun TextMain() {
        withContext(Main) {
            val ListMain: ArrayList<Screen> = CallApi()
            delay(3000)
            Log.d(TAG, "error list: $ListMain")
            val ListIndex = ListMain[0]
            SetarText(
                ListIndex.name.toString(),
                ListIndex.temperament.toString(),
                ListIndex.url.toString()
            )
        }
    }

   private suspend fun CallApi(): ArrayList<Screen> {
        val Listm = ArrayList<Screen>() // crio o retorno
        val result = Api().InterfaceApi().getDogs() // chamo a api
        delay(1000)
        result.enqueue(object : Callback<List<DogModelItem>> {
            override fun onResponse(
                call: Call<List<DogModelItem>>,
                response: Response<List<DogModelItem>>
            ) {
                if (response.isSuccessful) {
                    val resultado: List<DogModelItem> = response.body()!!
                    val Dog = resultado[0].breeds //pego o objeto e seleciono breeds

                    // preparo o retorno
                    val nomel = Dog[0].name
                    val temperamentol = Dog[0].temperament
                    val resultadol = resultado[0].url

                    Listm.add(Screen(nomel, temperamentol, resultadol))
                    Log.d(TAG, "error list: $Listm")
                }
            }
            override fun onFailure(
                call: Call<List<DogModelItem>?>?,
                t: Throwable?
            ) {
            }
        })
    return Listm
    }
}

