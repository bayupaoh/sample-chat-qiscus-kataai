package me.bayupaoh.qiscussdk.base.presenter


interface BasePresenter<V> {

    fun attachView(view: V)

    fun detachView()

}