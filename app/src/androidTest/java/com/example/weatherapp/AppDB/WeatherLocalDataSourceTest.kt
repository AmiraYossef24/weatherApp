package com.example.weatherapp.AppDB

import AppDB.AppDataBase
import AppDB.WeatherLocalDataSource
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import kotlinx.coroutines.runBlocking
import model.Location
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@MediumTest
@RunWith(AndroidJUnit4::class)
class WeatherLocalDataSourceTest {

    lateinit var dataBase: AppDataBase
    lateinit var  localDataSource: WeatherLocalDataSource


    @get:Rule
    val rule = InstantTaskExecutorRule()


    @Before
    fun setUp(){
        dataBase=Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource = WeatherLocalDataSource(getApplicationContext())

    }

    @After
    fun dearDown(){
        dataBase.close()
    }


    @Test
    fun getAllSavedLocation_locationName_returnThisLocation()= runBlocking (){

        //Given
        val location=Location("egypt",30.258,31.04587)
        localDataSource.insertLocation(location)
        //When

        val result=localDataSource.getAllSavedLocation()
        //Then

        assertThat(result,not(nullValue()))
    }
    @Test
    fun insertLocation()= runBlocking(){

        //Given
        val location1=Location("egypt",30.258,31.04587)

        //When
        val result=localDataSource.insertLocation(location1)

        //Then

        assertThat(result,not(nullValue()))

    }

    @Test
    fun deleteLocationTest()= runBlocking {
        //Given
        val location1=Location("egypt",30.258,31.04587)
        localDataSource.insertLocation(location1)

        //When
        val result=localDataSource.deleteLocation(location1)

        //Then
        assertThat(result,equalTo(Unit))

    }
}