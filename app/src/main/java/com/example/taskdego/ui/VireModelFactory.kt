package com.example.taskdego.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskdego.data.dao.tTaskDao
import com.example.taskdego.data.dao.tTrainerProfileDao
import com.example.taskdego.data.dao.mItemDao
import com.example.taskdego.data.dao.tItemDao
import com.example.taskdego.logic.TaskViewModel

//class TaskVireModelFactory(private val tTaskDao: tTaskDao,
//                           private  val tTrainerProfileDao: tTrainerProfileDao,
//    private val mItemDao: mItemDao,
//    private val tItemDao: tItemDao) : ViewModelProvider.Factory{
//    override  fun <T: ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(TaskViewModel::class.java)){
//            @Suppress("UNCHECKED_CAST")
//            return TaskViewModel(tTaskDao, tTrainerProfileDao, mItemDao, tItemDao) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}