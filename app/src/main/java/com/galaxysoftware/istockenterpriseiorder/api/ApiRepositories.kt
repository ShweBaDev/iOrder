package com.galaxysoftware.istockenterpriseiorder.api

import okhttp3.ResponseBody
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepositories {
   private fun getApi(callback: (String?) -> Unit) {

        ApiClient.apiService.getData().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(
                    call: Call<ResponseBody>?,
                    t: Throwable?
            ) {

                callback(null)
            }

            override fun onResponse(
                    call: Call<ResponseBody>?,
                    response: Response<ResponseBody>?
            ) {

                val body = response?.body()?.string()


                callback(body)

            }
        })
    }

   private fun postApi(data: ApiHelper, callback: (String?) -> Unit) {
        ApiClient.apiService.setData(data).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(
                    call: Call<ResponseBody>?,
                    t: Throwable?
            ) {

                callback(null)
            }

            override fun onResponse(
                    call: Call<ResponseBody>?,
                    response: Response<ResponseBody>?
            ) {

                val body = response?.body()?.string()


                callback(body)

            }
        })
    }

    companion object {

        fun Post(sqlquerystring : String?, isStoreProcedure : Boolean?, sqlparameters : MutableList<ParameterHelper> = mutableListOf(), callback: (String?) -> Unit)
        {

               val data = ApiHelper(IsStoredProcedure = isStoreProcedure,
                        SqlQuery = (if (isStoreProcedure == true) null else sqlquerystring),
                        StoredProcedureName = (if (isStoreProcedure == false) null else sqlquerystring),
                        Parameters = mutableListOf(sqlparameters),
                        SqlExecutionType = SqlExecutionTypes.ExecuteOutput.value)

            val apiRepositories : ApiRepositories = ApiRepositories()
            apiRepositories.postApi(data){
                callback(it)
            }
        }

        fun Get(callback: (String?) -> Unit){
            val apiRepositories : ApiRepositories = ApiRepositories()
            apiRepositories.getApi(){
                callback(it)
            }
        }

    }
}