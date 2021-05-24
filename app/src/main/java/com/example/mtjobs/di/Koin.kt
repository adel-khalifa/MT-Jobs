package com.example.mtjobs.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.mtjobs.data.local.FavoriteDao
import com.example.mtjobs.data.local.JobsDao
import com.example.mtjobs.data.local.JobsDataBase
import com.example.mtjobs.data.remote.JobsApi
import com.example.mtjobs.repo.JobsRepo
import com.example.mtjobs.repo.JobsRepoImpl
import com.example.mtjobs.repo.JobsViewModel
import com.example.mtjobs.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val apiModule = module {

    fun provideJobsApi(retrofit: Retrofit): JobsApi {
        return retrofit.create(JobsApi::class.java)
    }
    single { provideJobsApi(get()) }

}

val databaseModule = module {

    fun provideDatabase(application: Application): JobsDataBase {
        return Room.databaseBuilder(
            application,
            JobsDataBase::class.java,
            Constants.JOBS_DATA_BASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideJobsDao(database: JobsDataBase): JobsDao {
        return database.jobsDao
    }

    fun provideFavoriteDao(database: JobsDataBase): FavoriteDao {
        return database.favoriteDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideJobsDao(get()) }
    single { provideFavoriteDao(get()) }
}

val networkModule = module {
    val connectTimeout: Long = 40// 20s
    val readTimeout: Long = 40 // 20s

    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        okHttpClientBuilder.build()
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(client: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)

            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    single { provideHttpClient() }
    single { provideRetrofit(get(), Constants.BASE_URL) }
}


val repositoryModule = module {

    fun provideJobsRepo(api: JobsApi, context: Context, dao: JobsDao,favoriteDao: FavoriteDao): JobsRepoImpl {
        return JobsRepoImpl(api, context, dao,favoriteDao)
    }
    single { provideJobsRepo(get(), androidContext(), get(),get()) }

}


val viewModelModule = module {

    // Specific viewModel pattern to tell Koin how to build CountriesViewModel
    viewModel {
        JobsViewModel(app = androidApplication(), jobsRepo = get())
    }

}