package com.radea.githubuser.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.radea.githubuser.ui.home.MainViewModel
import com.radea.githubuser.utils.SettingPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var pref: SettingPreferences
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setup() {
        pref = mock(SettingPreferences::class.java)
        mainViewModel = MainViewModel(pref)
    }

    @Test
    fun assertGetThemeSettings() {
        runTest {
            val themeFlow = MutableStateFlow(DARK_MODE)
            `when`(pref.getThemeSetting()).thenReturn(themeFlow)
            val result = mainViewModel.getThemeSettings()
            assertEquals(DARK_MODE, result.value)
        }
    }

    @Test
    fun assertSaveThemeSetting()  = runTest{
        Dispatchers.setMain(Unconfined)
        val themeFlow = MutableStateFlow(DARK_MODE)
        `when`(pref.getThemeSetting()).thenReturn(themeFlow)
        mainViewModel.saveThemeSetting(DARK_MODE)
        verify(pref).saveThemeSetting(DARK_MODE)
        Dispatchers.resetMain()
    }

    companion object {
        private const val DARK_MODE = true
    }
}