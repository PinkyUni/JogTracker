package com.pinkyuni.jogtracker

import android.app.Application
import com.google.gson.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.pinkyuni.jogtracker.data.APIService
import okhttp3.*
import org.json.JSONObject
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.generic.with
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

val networkModule = Kodein.Module("networkModule") {
    constant(tag = "base_url") with "https://jogtracker.herokuapp.com/api/v1/"
    bind<Gson>() with singleton { provideGson() }
    bind<Interceptor>() with singleton { provideInterceptor() }
    bind<OkHttpClient>() with singleton { provideOkHttpClient(instance()) }
    bind<Retrofit>() with singleton {
        provideRetrofit(
            instance(),
            instance(),
            instance(tag = "base_url")
        )
    }
    bind<APIService>() with singleton { instance<Retrofit>().create(APIService::class.java) }
    bind<ConnectionStateMonitor>() with singleton { ConnectionStateMonitor(instance<Application>()) }
}

private fun provideInterceptor(): Interceptor = Interceptor { chain ->
    val originalRequest: Request = chain.request()
    val builder: Request.Builder =
        originalRequest.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
    val newRequest: Request = builder.build()
    val response = chain.proceed(newRequest)
    val rawJson = response.body()?.string()
    val jsonObject = JSONObject(rawJson!!)
    val t = jsonObject.get("response")
    val contentType: MediaType? = response.body()!!.contentType()
    val body = ResponseBody.create(contentType, t.toString())
    response.newBuilder().body(body).build()
}

private fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient =
    OkHttpClient().newBuilder().addInterceptor(interceptor).build()

private fun provideGson() = GsonBuilder()
    .registerTypeAdapter(Date::class.java, object : JsonDeserializer<Date> {
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): Date {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
            return sdf.parse(json.asJsonPrimitive.asString) ?: Date()
        }
    })
    .setLenient()
    .create()

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gson: Gson,
    baseUrl: String
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}