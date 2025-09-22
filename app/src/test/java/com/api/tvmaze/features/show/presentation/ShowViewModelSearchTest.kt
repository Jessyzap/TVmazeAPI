package com.api.tvmaze.features.show.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.api.tvmaze.features.show.presentation.viewModel.ShowViewModel
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShowViewModelSearchTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val viewModel = spyk(
        ShowViewModel(mockk(), mockk(), mockk()),
        recordPrivateCalls = true
    ) // for observe private functions

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onSearchQueryChanged_newQueryEmitted_previousSearchJobIsCancelled() = runTest {
        viewModel.onSearchQueryChanged("query1")
        advanceTimeBy(100) // job not null
        val firstJob = viewModel.searchCurrentJob

        viewModel.onSearchQueryChanged("query2")
        advanceTimeBy(100)
        val secondJob = viewModel.searchCurrentJob

        assert(firstJob?.isCancelled == true)
        assert(secondJob?.isActive == true)
    }

    @Test
    fun onSearchQueryChanged_afterDebounceTime_searchIsCalledWithCorrectTime() = runTest {
        viewModel.onSearchQueryChanged("query1")
        advanceTimeBy(700)
        assertEquals("", viewModel.searchCurrentSearchQuery)

        advanceTimeBy(800)
        assertEquals("query1", viewModel.searchCurrentSearchQuery)
    }

    @Test
    fun onSearchQueryChanged_sameQueryMultipleTimesQueryOnce_invokesSearchCorrectly() = runTest {
        viewModel.onSearchQueryChanged("query1")
        advanceTimeBy(850)

        viewModel.onSearchQueryChanged("query1")
        advanceTimeBy(850)

        viewModel.onSearchQueryChanged("query2")
        advanceTimeBy(850)

        coVerify(exactly = 1) { viewModel["getSearch"]("query1") }
        coVerify(exactly = 1) { viewModel["getSearch"]("query2") }
    }

}