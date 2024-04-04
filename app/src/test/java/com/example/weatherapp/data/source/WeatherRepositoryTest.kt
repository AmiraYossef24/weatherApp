package com.example.weatherapp.data.source

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import model.Calender
import model.IWeatherRepository
import model.Location
import model.WeatherRepository
import model.WeatherResponse
import network.weatherRemoteDataSource
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.math.abs

class WeatherRepositoryTest {


    lateinit var fakeLocalDataSource: FakeLocalDataSource
    lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    lateinit var repo : IWeatherRepository


    var location1 =Location("egypt",30.21548,31.215444)
    var location2 =Location("india",35.21548,31.215444)

    var cale1=Calender("4/4/2024","03:00am","egypt",50.2154,5874.21)
    var cale2=Calender("4/4/2024","03:00am","turky",50.2154,5874.21)

    val locationsList= mutableListOf<Location>(location1,location2)
    val calendersList= mutableListOf<Calender>(cale1,cale2)

    @Before
    fun setUp() {
        fakeLocalDataSource = FakeLocalDataSource(locationsList,calendersList)
        fakeRemoteDataSource = FakeRemoteDataSource()
        repo =WeatherRepository.getInstance(fakeRemoteDataSource,fakeLocalDataSource)
    }



    @Test
    fun testGetAllSavedLocationTest() = runBlocking {
        //Given
        repo.insertLocation(location1)
        repo.insertLocation(location2)
        //When
       val result=repo.getAllSavedLocation().first()
        //Then
        assertEquals("egypt",result[0].countryName)

    }

    @Test
    fun testGetAllSavedCalender() = runBlocking {
        //Given
        repo.insertCalender(cale1)
        repo.insertCalender(cale2)
        //When
        val result =repo.getAllSavedCalender().first()
        //Then
        assertEquals("turky",result[1].countryName)

    }

    @Test
    fun testInsertLocation() = runBlocking(){
        //When
         repo.insertLocation(location1)
         val result=repo.getAllSavedLocation().first()

        //Then
        assertEquals("egypt",result[0].countryName)

    }

    @Test
    fun testInsertCalender() = runBlocking(){
        //When
        repo.insertCalender(cale1)
        val result=repo.getAllSavedCalender().first()

        //Then
        assertEquals(cale1.countryName, result[0].countryName)
        //assertThat(result, nullValue())

    }
}
