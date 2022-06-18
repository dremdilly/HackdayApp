package com.daurenbek.hackdayapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val interceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

interface ApiService {
    @POST("registration")
    suspend fun sendRegisterRequest(
        @Header("Content-Type") content_type: String = "application/json",
        @Header("Accept") accept: String = "application/json",
        @Body body: RegisterData
    ): Response<ResponseBody>

    @POST("signin")
    suspend fun sendAuthorizeRequest(
        @Header("Content-Type") content_type: String = "application/json",
        @Header("Accept") accept: String = "application/json",
        @Body body: AuthorizeData
    ): Response<AuthResponse>

    @GET("users/")
    suspend fun sendProfileRequest(
        @Header("Content-Type") content_type: String = "application/json",
        @Header("Accept") accept: String = "application/json",
    ): Response<ProfileModel>

    @GET("specializations/")
    suspend fun getAllSpecializations(
        @Header("Content-Type") content_type: String = "application/json",
        @Header("Accept") accept: String = "application/json",
    ): Response<List<SpecializationModel>>

    @GET("lessons/")
    suspend fun getAllSubjects(
        @Header("Content-Type") content_type: String = "application/json",
        @Header("Accept") accept: String = "application/json",
    ): Response<List<SubjectModel>>
//    @GET("api/course/")
//    suspend fun getAllCourses(
//        @Header("Content-Type") content_type: String = "application/json",
//        @Header("Accept") accept: String = "application/json",
//    ): Response<List<CourseModel>>
//
//    @POST("api/course/{id}")
//    suspend fun createCourse(
//        @Path("id") id: String,
//        @Header("Content-Type") content_type: String = "application/json",
//        @Header("Accept") accept: String = "application/json",
//        @Body body: CourseModel
//    ): Response<ResponseBody>
//
//    @PUT("api/course/categories")
//    suspend fun getAllCoursesByCategory(
//        @Header("Content-Type") content_type: String = "application/json",
//        @Header("Accept") accept: String = "application/json",
//        @Body body: CategoriesData
//    ): Response<List<CourseModel>>
//
//    @GET("api/lesson/{id}")
//    suspend fun getLessonById(
//        @Path("id") id: Long,
//        @Header("Content-Type") content_type: String = "application/json",
//        @Header("Accept") accept: String = "application/json",
//    ): Response<LessonModel>
}

object NetworkApi {
    val token: String? = null

    val authService: ApiService by lazy {
        authRetrofit.create(ApiService::class.java)
    }

    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    private lateinit var authRetrofit: Retrofit
    private lateinit var retrofit: Retrofit

    fun authRetrofit(baseUrl: String) {
        authRetrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8181/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }
    fun createRetrofit(baseUrl: String, authToken: String) {
        val client = OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor { chain ->
            chain.proceed(chain.request().newBuilder().also {
                it.addHeader("Authorization", "Bearer $authToken")
            }.build())
        }.build()

        retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8181/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }
}