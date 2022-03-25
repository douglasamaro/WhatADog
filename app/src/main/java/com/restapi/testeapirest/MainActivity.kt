package com.restapi.testeapirest

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.restapi.testeapirest.data.Api
import com.restapi.testeapirest.domain.model.DogModelItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        try {
            solicitar.setOnClickListener {
                val result = Api().InterfaceApi().getDogs()
                result.enqueue(object : Callback<List<DogModelItem>> {

                    override fun onResponse(
                        call: Call<List<DogModelItem>>,
                        response: Response<List<DogModelItem>>
                    ) {
                        if (response.isSuccessful) {
                            val resultado: List<DogModelItem> = response.body()!!
                            val Dog = resultado[0].breeds //pego o objeto e seleciono breeds

                            nome.text = Dog[0].name
                            temperamento.text = Dog[0].temperament

                            val executor = Executors.newSingleThreadExecutor()
                            val handler = Handler(Looper.getMainLooper())
                            var image: Bitmap? = null
                            executor.execute {
                                val imageURL = resultado[0].url
                                try {
                                    val `in` = java.net.URL(imageURL).openStream()
                                    image = BitmapFactory.decodeStream(`in`)
                                    handler.post {
                                        Onlineimagem.setImageBitmap(image)
                                    }
                                }
                                //for error
                                catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }


                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "tivemos algum problema na conexão",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }

                    override fun onFailure(
                        call: Call<List<DogModelItem>?>?,
                        t: Throwable?
                    ) {
                    }
                })
            }
        } catch (e: Exception) {
            Toast.makeText(this, "verifique a sua conexão", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "erro: ", e)
        }
    }
}