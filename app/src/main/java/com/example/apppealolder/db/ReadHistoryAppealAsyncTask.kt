package com.example.apppealolder.db

import android.content.Context
import android.os.AsyncTask
import com.example.apppealolder.ResponseData
import com.example.apppealolder.model.AppealInfo
import java.sql.SQLException

class ReadHistoryAppealAsyncTask(private val context: Context,
private val asyncTaskCallback: AsyncTaskCallback<ArrayList<AppealInfo>>
) : AsyncTask<Unit, Unit, ResponseData<ArrayList<AppealInfo>>>() {


    override fun onPreExecute() {

    }

    override fun doInBackground(vararg p0: Unit?): ResponseData<ArrayList<AppealInfo>> {
        val dbHelper = DBHelper.getInstance(context)
        try {
            val db = dbHelper.readableDatabase
            val appealList = ArrayList<AppealInfo>()
            val cursor = db.query(
                "Appeals",
                arrayOf(
                    "id",
                    "phone_number",
                    "district",
                    "request_data",
                    "description",
                    "isAllow"
                ),
                "isAllow= ?",
                arrayOf("1"),
                null,
                null,
                null
            )

            val indexId = cursor.getColumnIndex("id")
            val indexPhone = cursor.getColumnIndex("phone_number")
            val indexDistrict = cursor.getColumnIndex("district")
            val indexRequest = cursor.getColumnIndex("request_data")
            val indexDescription = cursor.getColumnIndex("description")
            val indexAllow = cursor.getColumnIndex("isAllow")

            while (cursor.moveToNext()) {
                val data = AppealInfo(
                    cursor.getInt(indexId),
                    cursor.getString(indexPhone),
                    cursor.getString(indexDistrict),
                    cursor.getString(indexRequest),
                    cursor.getString(indexDescription),
                    cursor.getInt(indexAllow)

                )

                appealList.add(data)
            }
            cursor.close()
            dbHelper.close()
            return ResponseData(isSuccess = true, data = appealList)

        } catch (e: SQLException) {
            return ResponseData(isSuccess = false, exception = e)
        }


    }

    override fun onPostExecute(result: ResponseData<ArrayList<AppealInfo>>) {
        if (result.isSuccess) {
            asyncTaskCallback.onSuccess(result.data)
        } else {
            asyncTaskCallback.onError(result.exception)
        }
    }

}