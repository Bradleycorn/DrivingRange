package net.bradball.android.drivingrange.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.bradball.android.drivingrange.MainActivity

@Suppress("unused")
@Module
abstract class ActivityBuildersModule {

    /*
        Here, all of our activities include the same FragmentBuildersModule,
        which contains all of our fragments. This works, and doesn't seem
        to cause any issues, performance or otherwise. If we run into any
        issues, we may consider breaking FragmentBuildersModule into
        multiple modules that contain only the fragments for the activity
        in which they are used. We didn't do that now, because that would
        seem to violate the principle that a fragment is reusable and should
        be able to be used anywhere ...
     */


    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity


}
