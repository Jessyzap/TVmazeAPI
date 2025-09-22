import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.api.tvmaze.features.favorite_show.data.datasource.local.FavoriteShowLocalDataSourceImpl
import com.api.tvmaze.features.favorite_show.data.model.ShowObject
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class FavoriteShowLocalDataSourceImplTest {

    private lateinit var dataSource: FavoriteShowLocalDataSourceImpl
    private lateinit var testShow: ShowObject

    @Before
    fun setUp() = runTest {
        Realm.init(InstrumentationRegistry.getInstrumentation().targetContext)
        val config = RealmConfiguration.Builder()
            .inMemory()
            .name("test-realm")
            .build()
        Realm.setDefaultConfiguration(config)

        dataSource = FavoriteShowLocalDataSourceImpl()

        testShow = ShowObject().apply {
            id = 123
            name = "Test"
        }

        dataSource.deleteFavoriteShow(testShow)
    }

    @After
    fun tearDown() = runTest {
        dataSource.closeRealm()
    }

    @Test
    fun saveFavoriteShow_savesSuccessfully() = runTest {
        dataSource.saveFavoriteShow(testShow)

        val favorites = dataSource.getFavoriteShows()
        assertTrue(favorites.any { it.id == testShow.id && it.name == testShow.name })
    }

    @Test
    fun deleteFavoriteShow_removesSuccessfully() = runTest {
        dataSource.saveFavoriteShow(testShow)

        val deleted = dataSource.deleteFavoriteShow(testShow)
        assertTrue(deleted)

        val favorites = dataSource.getFavoriteShows()
        assertFalse(favorites.any { it.id == testShow.id })
    }

    @Test
    fun checkIfIsFavorite_returnsTrueWhenExists() = runTest {
        dataSource.saveFavoriteShow(testShow)

        val isFavorite = dataSource.checkIfIsFavorite(testShow.id)
        assertTrue(isFavorite)
    }

    @Test
    fun checkIfIsFavorite_returnsFalseWhenNotExists() = runTest {
        val isFavorite = dataSource.checkIfIsFavorite(2)
        assertFalse(isFavorite)
    }

    @Test
    fun getFavoriteShows_returnsEmptyInitially() = runTest {
        val favorites = dataSource.getFavoriteShows()
        assertTrue(favorites.isEmpty())
    }
}
