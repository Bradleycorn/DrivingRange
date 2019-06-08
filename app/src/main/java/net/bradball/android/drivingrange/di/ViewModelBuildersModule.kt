package net.bradball.android.drivingrange.di


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.bradball.android.drivingrange.ui.rangeList.RangeListViewModel

@Suppress("unused")
@Module
abstract class ViewModelBuildersModule {
    @Binds
    @IntoMap
    @ViewModelKey(RangeListViewModel::class)
    abstract fun bindRangeListViewModel(rangeListViewModel: RangeListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}
