package com.sungbin.androidstudy

import android.content.Context
import com.sungbin.sungbintool.StorageUtils
import java.io.File

object DataUtils {

    fun getTodoList(): ArrayList<String>?{
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        return File("${StorageUtils.sdcard}/TODO-LIST").list()?.toCollection(ArrayList())
    }

    fun save(name: String, checked: Boolean){
        StorageUtils.createFolder("TODO-LIST")
        StorageUtils.save("TODO-LIST/$name.txt", checked.toString())
    }

    fun getIsChecked(name: String): Boolean {
        return StorageUtils.read("TODO-LIST/$name.txt", "false")!!.toBoolean()
    }

}