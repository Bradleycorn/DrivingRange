package net.bradball.android.drivingrange

import android.app.Activity
import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import net.bradball.android.drivingrange.di.DaggerApplicationComponent
import javax.inject.Inject


class DrivingRangeApplication: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        setupDependencyInjection()
    }

    private fun setupDependencyInjection() {
        // Am I red? If so, you probably need to generate the DI code.
        DaggerApplicationComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }
}