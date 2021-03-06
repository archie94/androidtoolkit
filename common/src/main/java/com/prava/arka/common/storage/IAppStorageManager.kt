package com.prava.arka.common.storage

/**
 * Created by Arka Prava Basu <arkaprava94@gmail.com> on 6/9/19.
 */
interface IAppStorageManager {
    fun <T> getAccessToken(): T

    fun <T> setAccessToken(accessToken: T)

    fun <T> getUserId(): T

    fun <T> setUserId(id: T)

    fun resetUserId()

    fun removeAccessToken()

    fun getMediaTempDirectoryPath(): String
}
