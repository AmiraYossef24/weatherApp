package com.example.weatherapp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.model.FakeWeatherRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import model.Location
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import saved.viewModel.SavedViewModel


class SavedLocationsViewModelTest{

    lateinit var repo: FakeWeatherRepo
    lateinit var viewModel: SavedViewModel
    val loc1=Location("minia",50.214587,30.25414)
    val loc2=Location("giza",80.326598,81.3259914)
    val myLocationList =mutableListOf <Location>(loc1,loc2)


    @get:Rule
    val rule=InstantTaskExecutorRule()




    @Before
    fun setUp(){
        repo= FakeWeatherRepo(myLocationList)
        viewModel= SavedViewModel(repo)
    }

    @Test
    fun testGetAllSavedLocations()= runBlockingTest{

        //When
         viewModel.getAllSavedLocations()
         val result=viewModel.locationList.first()

        //Then
        assertEquals(result[0].countryName, myLocationList[0].countryName)
    }

    @Test
    fun testDeleteLocation()= runBlockingTest{
        //When
        viewModel.delete(loc1)
        viewModel.delete(loc2)
        val result = viewModel.locationList.first()

        assertThat(result[0].countryName, nullValue())
    }



}