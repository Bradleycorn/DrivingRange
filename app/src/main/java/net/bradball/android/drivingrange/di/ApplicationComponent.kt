package net.bradball.android.drivingrange.di


import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import net.bradball.android.drivingrange.DrivingRangeApplication
import javax.inject.Singleton

/**
 * Dagger2 Component for allowing dynamic injection into our Activities, Fragments, ViewModels, etc.
 */
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityBuildersModule::class])
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: DrivingRangeApplication): Builder

        fun build(): ApplicationComponent
    }

    fun inject(application: DrivingRangeApplication)
}
