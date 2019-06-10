package net.bradball.android.drivingrange.di


import android.app.Application
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient

import dagger.Module
import dagger.Provides
import net.bradball.android.drivingrange.DrivingRangeApplication
import javax.inject.Singleton

@Module(includes = [ViewModelBuildersModule::class])
open class AppModule {

    @Provides
    open fun provideApplication(application: DrivingRangeApplication): Application = application

    @Provides
    open fun provideContext(application: DrivingRangeApplication): Context = application.applicationContext

    @Provides
    open fun provideLocationSettingsClient(app: DrivingRangeApplication): SettingsClient = LocationServices.getSettingsClient(app)

    @Singleton
    @Provides
    open fun provideLocationClient(app: DrivingRangeApplication): FusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(app)

//    @Provides
//    open fun providesAppExecutors(): AppExecutors = AppExecutors.instance

//    @Singleton
//    @Provides
//    open fun providesAppDatabase(app: TwinspiresApp): IAppDatabase = Room.databaseBuilder(app, AppDatabase::class.java, "AppDB").build()



}