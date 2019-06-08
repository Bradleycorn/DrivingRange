package net.bradball.android.drivingrange.di



import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.bradball.android.drivingrange.ui.rangeList.RangeListFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {


    @ContributesAndroidInjector
    abstract fun contributeRangeListFragment(): RangeListFragment
//
//    @ContributesAndroidInjector(modules = [ProgramModule::class])
//    abstract fun contributeProgramFragment(): ProgramFragment

}
