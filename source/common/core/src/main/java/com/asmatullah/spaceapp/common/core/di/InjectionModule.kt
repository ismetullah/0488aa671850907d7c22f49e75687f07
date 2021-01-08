package com.asmatullah.spaceapp.common.core.di

import org.koin.core.module.Module

interface InjectionModule {
    fun create(): Module
}