package net.bradball.android.drivingrange.di


import android.app.Application
import android.content.Context

import dagger.Module
import dagger.Provides
import net.bradball.android.drivingrange.DrivingRangeApplication

@Module(includes = [ViewModelBuildersModule::class])
open class AppModule {

    @Provides
    open fun provideApplication(application: DrivingRangeApplication): Application = application

    @Provides
    open fun provideContext(application: DrivingRangeApplication): Context = application.applicationContext

//    @Provides
//    open fun providesAppExecutors(): AppExecutors = AppExecutors.instance

//    @Singleton
//    @Provides
//    open fun providesAppDatabase(app: DrivingRangeApplication): IAppDatabase = Room.databaseBuilder(app, AppDatabase::class.java, "AppDB").build()

}
