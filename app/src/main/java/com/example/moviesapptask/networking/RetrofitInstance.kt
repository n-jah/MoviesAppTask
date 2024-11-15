package com.example.moviesapptask.networking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1ZTEwMjNjZDlkMzBkZDBlODA0OGVlY2FhNzNjMGJlNSIsIm5iZiI6MTczMTUyOTk4NC4xNzg4Nzk1LCJzdWIiOiI2NzM1MGMyZDJmYWZhZWM0MjU1M2ZkOTUiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.EHTpcrkO0U4B2g6JHDTe8eTYyZmTA0PzpyfQvdISHLo")
                .build()
            chain.proceed(newRequest)
        }
        .build()


    val api : MoviesApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MoviesApi::class.java)
    }
}


