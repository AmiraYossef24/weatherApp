package com.example.weatherapp.data.source

import model.DetailsResponse
import model.WeatherResponse
import network.IweatherRemoteDataSource
import retrofit2.Response

class FakeRemoteDataSource: IweatherRemoteDataSource {

    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lan: Double,
        apiKey: String,
        temp: String,
        lang: String
    ): Response<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getDetailsOverNetwork(
        lat: Double,
        lan: Double,
        apiKey: String,
        temp: String,
        lang: String
    ): Response<DetailsResponse> {
        TODO("Not yet implemented")
    }
}