package com.rrat.mvvm2demo.model.notification

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotifyWorker (context: Context, params: WorkerParameters) : Worker(context, params){
    override fun doWork(): Result {
        Log.i("Notify Worker", "doWork function is called...")
        return Result.success()
    }

}