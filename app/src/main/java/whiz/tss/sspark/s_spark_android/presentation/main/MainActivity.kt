package whiz.tss.sspark.s_spark_android.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.BottomNavigationBarItem
import whiz.sspark.library.data.entity.Profile
import whiz.sspark.library.data.enum.BottomNavigationType
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.data.enum.BottomNavigationId
import whiz.tss.sspark.s_spark_android.databinding.ActivityMainBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.today.TodayFragment

class MainActivity : BaseActivity() {

    private val binding by lazy {
         ActivityMainBinding.inflate(layoutInflater)
    }

    private val isNavigatedToSetting by lazy {
        intent.getBooleanExtra("isNavigatedToSetting", false)
    }

    private val deeplink by lazy {
        intent.getStringExtra("deepLink") ?: ""
    }

    private var currentFragment: Int = -1
    private var isNavigated = false
    private var lastShowedFragment: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }
    }

    override fun initView() {
        binding.vBottomNavigation.init(this, listOf(
            BottomNavigationBarItem(id = BottomNavigationId.TODAY.id, title = resources.getString(R.string.tab_today), type = BottomNavigationType.ICON.id, imageResource = R.drawable.ic_female_circular),
            BottomNavigationBarItem(id = BottomNavigationId.CLASS.id, title = resources.getString(R.string.tab_class), type = BottomNavigationType.ICON.id, imageResource = R.drawable.ic_female_circular),
            BottomNavigationBarItem(id = BottomNavigationId.ID_CARD.id, title = resources.getString(R.string.tab_id_card), type = BottomNavigationType.ICON.id, imageResource = R.drawable.ic_female_circular),
            BottomNavigationBarItem(id = BottomNavigationId.MENU.id, title = profile?.firstName ?: "", type = BottomNavigationType.PROFILE.id, imageUrl = profile?.imageUrl ?: ""))
        ) {
            when (it) {
                BottomNavigationId.TODAY.id -> {
                    if (!isFragmentVisible(BottomNavigationId.TODAY.id)) {
                        renderFragment(TodayFragment.newInstance(), BottomNavigationId.TODAY.id)
                    }
                }
                BottomNavigationId.CLASS.id -> {
//                    if (!isFragmentVisible(BottomNavigationId.CLASS.id)) { // TODO wait implementation
//                        renderFragment(ClassListFragment.newInstance(), BottomNavigationId.CLASS.id)
//                    }
                }
                BottomNavigationId.ID_CARD.id -> {
//                    binding.vBottomNavigation.setSelection(currentFragment) // TODO wait confirm UI
//                    if (!isFragmentVisible(BottomNavigationId.MENU.id)) {
//                        renderFragment(MenuFragment.newInstance(), BottomNavigationId.MENU.id)
//                    }
                }
                BottomNavigationId.MENU.id -> {
//                    if (!isFragmentVisible(BottomNavigationId.MENU.id)) { // TODO wait implementation
//                        renderFragment(MenuFragment.newInstance(), BottomNavigationId.MENU.id)
//                    }
                }
            }
        }
    }

    override fun observeView() {

    }

    override fun observeData() {

    }

    override fun observeError() {

    }

    private fun isFragmentVisible(id: Int) = currentFragment == id

    private fun renderFragment(fragment: Fragment, fragmentId: Int) {
        val fragmentList = supportFragmentManager.fragments
        val targetFragment = supportFragmentManager.findFragmentByTag(fragmentId.toString())

        fragmentList.forEach {
            supportFragmentManager.beginTransaction()
                .hide(it)
                .commit()
        }

        targetFragment?.let {
            supportFragmentManager.beginTransaction()
                .show(it)
                .commit()
        } ?: run {
            supportFragmentManager.beginTransaction()
                .add(R.id.flContainer, fragment, fragmentId.toString())
                .commit()
        }

        currentFragment = fragmentId
        binding.vBottomNavigation.setSelection(fragmentId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("isNavigatedToSettingFinished",isNavigatedToSetting)
        outState.putString("deepLink", deeplink)
        outState.putInt("currentFragment", currentFragment)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        isNavigated = savedInstanceState.getBoolean("isNavigatedToSettingFinished") || !savedInstanceState.getString("deepLink", "").isNullOrBlank()
        lastShowedFragment = savedInstanceState.getInt("currentFragment")
        super.onRestoreInstanceState(savedInstanceState)
    }
}