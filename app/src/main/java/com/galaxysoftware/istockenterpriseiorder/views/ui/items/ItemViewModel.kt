package com.galaxysoftware.istockenterpriseiorder.views.ui.items

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galaxysoftware.istockenterpriseiorder.R
import com.galaxysoftware.istockenterpriseiorder.api.ApiRepositories
import com.galaxysoftware.istockenterpriseiorder.api.NpgsqlDbType
import com.galaxysoftware.istockenterpriseiorder.api.ParameterDirection
import com.galaxysoftware.istockenterpriseiorder.api.ParameterHelper
import com.galaxysoftware.istockenterpriseiorder.models.Category
import com.galaxysoftware.istockenterpriseiorder.models.UsrCode
import com.galaxysoftware.istockenterpriseiorder.utils.Utility
import com.galaxysoftware.istockenterpriseiorder.views.BaseRecyclerViewAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ItemViewModel : ViewModel() {

    private var result: MutableLiveData<List<UsrCode>>? = null
    private var categoryResult: MutableLiveData<List<Category>>? = null
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun loadData(): LiveData<List<UsrCode>> {

        if (result == null) {
            result = MutableLiveData()
            val parameter: MutableList<ParameterHelper> = mutableListOf()
            parameter.add(
                ParameterHelper(
                    PsqlParameterName = "_usercode",
                    PsqlDbTypes = NpgsqlDbType.Varchar.value,
                    PsqlParameterDirection = ParameterDirection.Input.value,
                    PsqlParameterValue = null
                )
            )
            ApiRepositories.Post(
                sqlquerystring = "getmobileusrcode",
                isStoreProcedure = true,
                sqlparameters = parameter
            ) {
                val listType = object : TypeToken<List<UsrCode>>() {}.type
                var codelist = Gson().fromJson<List<UsrCode>>(it, listType)

                codelist.let {
                    result?.apply {
                        value = it
                    }
                }

            }
        }


        return result as MutableLiveData<List<UsrCode>>
    }

    fun loadCategory(): LiveData<List<Category>> {

        if (categoryResult == null) {
            categoryResult = MutableLiveData()
            val parameter: MutableList<ParameterHelper> = mutableListOf()
            parameter.add(
                ParameterHelper(
                    PsqlParameterName = "_categoryid",
                    PsqlDbTypes = NpgsqlDbType.Integer.value,
                    PsqlParameterDirection = ParameterDirection.Input.value,
                    PsqlParameterValue = null
                )
            )

            ApiRepositories.Post(
                sqlquerystring = "getmobilecategory",
                isStoreProcedure = true,
                sqlparameters = parameter
            ) {
                val listType = object : TypeToken<List<Category>>() {}.type
                var categorylist = Gson().fromJson<List<Category>>(it, listType)

                categorylist.let {
                    categoryResult?.apply {
                        value = it
                    }
                }

            }
        }


        return categoryResult as MutableLiveData<List<Category>>
    }

}