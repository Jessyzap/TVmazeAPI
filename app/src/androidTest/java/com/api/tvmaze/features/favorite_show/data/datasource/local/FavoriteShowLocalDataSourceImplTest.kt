package com.api.tvmaze.features.favorite_show.data.datasource.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.api.tvmaze.features.favorite_show.data.model.ShowObject
import com.api.tvmaze.getOrAwaitValue
import io.realm.Realm
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class FavoriteShowLocalDataSourceImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dataSource: FavoriteShowLocalDataSourceImpl
    private lateinit var testShow: ShowObject

    @Before
    fun setUp() {
        Realm.init(InstrumentationRegistry.getInstrumentation().targetContext)
        dataSource = FavoriteShowLocalDataSourceImpl()

        testShow = ShowObject().apply {
            id = 123
            name = "Test"
        }

        dataSource.deleteFavoriteShow(testShow).getOrAwaitValue() // clear any previous data
    }

    @After
    fun tearDown() {
        dataSource.closeRealm()
    }

    @Test
    fun saveFavoriteShow_savesSuccessfully() {
        dataSource.saveFavoriteShow(testShow)

        val favorites = dataSource.getFavoriteShows().getOrAwaitValue()
        assertTrue(favorites.any { it.id == testShow.id && it.name == testShow.name })
    }

    @Test
    fun deleteFavoriteShow_removesSuccessfully() {
        dataSource.saveFavoriteShow(testShow)

        val deleted = dataSource.deleteFavoriteShow(testShow).getOrAwaitValue()
        assertTrue(deleted)

        val favorites = dataSource.getFavoriteShows().getOrAwaitValue()
        assertFalse(favorites.any { it.id == testShow.id })
    }

    @Test
    fun checkIfIsFavorite_returnsTrueWhenExists() {
        dataSource.saveFavoriteShow(testShow)
        val isFavorite = dataSource.checkIfIsFavorite(testShow.id).getOrAwaitValue()
        assertTrue(isFavorite)
    }

    @Test
    fun checkIfIsFavorite_returnsFalseWhenNotExists() {
        val isFavorite = dataSource.checkIfIsFavorite(2).getOrAwaitValue()
        assertFalse(isFavorite)
    }

    @Test
    fun getFavoriteShows_returnsEmptyInitially() {
        val favorites = dataSource.getFavoriteShows().getOrAwaitValue(time = 3, timeUnit = TimeUnit.SECONDS)
        assertTrue(favorites.isEmpty())
    }

}