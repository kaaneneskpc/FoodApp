package com.example.foodyapp.core.repository

import com.example.foodyapp.core.service.localDataSource.LocalDataSource
import com.example.foodyapp.core.service.remoteDataSource.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FoodRecipesRepository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource,
) {

    val remote = remoteDataSource
    val local = localDataSource
}
