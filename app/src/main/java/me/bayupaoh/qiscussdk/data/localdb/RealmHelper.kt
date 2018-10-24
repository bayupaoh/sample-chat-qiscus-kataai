package me.bayupaoh.qiscussdk.data.localdb

import io.realm.Realm
import io.realm.RealmObject

/**
 * Created by dodydmw19 on 7/25/18.
 */

class RealmHelper<T : RealmObject> {

    fun saveObject(data: T) {
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction { r -> r.insertOrUpdate(data) }
        } finally {
            realm?.close()
        }
    }

    fun saveList(data: List<T>) {
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction { r -> r.insertOrUpdate(data) }
        } finally {
            realm?.close()
        }
    }

    fun getData(id: Int, paramName: String, data: T) : T{
        val realm: Realm = Realm.getDefaultInstance()
        return realm.where(data::class.java).equalTo(paramName, id).findFirst()!!
    }

    fun getData(data: T) : List<T>{
        val realm: Realm = Realm.getDefaultInstance()
        return realm.where(data::class.java).findAll()!!
    }

    fun deleteData(data: T){
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            val realmResult = realm.where(data::class.java).findAll()!!
            realm?.executeTransaction { r -> realmResult.deleteAllFromRealm() }
        } finally {
            realm?.close()
        }

    }

}