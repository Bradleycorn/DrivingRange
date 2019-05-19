package net.bradball.android.drivingrange.di


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelBuildersModule {
//    @Binds
//    @IntoMap
//    @ViewModelKey(TwinspiresActivityViewModel::class)
//    abstract fun bindTwinsSpiresActivityViewModel(twinspiresActivityViewModel: TwinspiresActivityViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}
