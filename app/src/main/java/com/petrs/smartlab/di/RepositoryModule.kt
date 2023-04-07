package com.petrs.smartlab.di

import com.petrs.smartlab.data.repository.SharedPreferencesRepository
import com.petrs.smartlab.data.repository.SmartLabRepository
import com.petrs.smartlab.data.repository.impl.SharedPreferencesRepositoryImpl
import com.petrs.smartlab.data.repository.impl.SmartLabRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<SharedPreferencesRepository> {
        SharedPreferencesRepositoryImpl(
            handler = get()
        )
    }

    single<SmartLabRepository> {
        SmartLabRepositoryImpl()
    }
}