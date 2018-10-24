package me.bayupaoh.qiscussdk.di.component

import me.bayupaoh.qiscussdk.di.module.APIServiceModule
import me.bayupaoh.qiscussdk.di.scope.SuitCoreApplicationScope

import dagger.Component

@SuitCoreApplicationScope
@Component(modules = [(APIServiceModule::class)])
interface ApplicationComponent {

}