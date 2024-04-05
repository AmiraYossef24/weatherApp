package com.example.weatherapp.AppDB

import AppDB.AppDataBase
import AppDB.LocationDAO
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.flow.first
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

@RunWith(AndroidJUnit4::class)
@SmallTest
class LocationDAOTest {

    lateinit var dataBase : AppDataBase
    lateinit var dao: LocationDAO

    @get:Rule
    val rule=InstantTaskExecutorRule()

    @Before
    fun SetUp(){
         dataBase = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDataBase::class.java
        ).build()
         dao=dataBase.locationDAO()
    }

    @After
    fun teerDown(){

        dataBase.close()
    }

    @Test
    fun getAllLocation()= runBlocking(){

        //Given
        val location1 =Location("Egypt",30.1245,31.784512)
        dao.insert(location1)

        //When
        val result=dao.getAllLocation().first(){

            assertThat(it,not(nullValue()))
            assertThat(it[0].countryName ,equalTo(location1.countryName) )
            true
         }

        //Then
        //assertThat(result,not(nullValue()))

    }

    @Test
    fun insert(){

        //Given
        val location2=Location("india",60.4587,61.25487)

        //When

        val result=dao.insert(location2)

        //Given
        assertThat(result,not(nullValue()))
    }

    @Test
    fun delete(){
        //Given
        val location3=Location("turky",80.26558,81.36210)
        //When
        dao.insert(location3)
        val result =dao.delete(location3)

        //Given
        assertThat(result, equalTo(Unit))    }
}