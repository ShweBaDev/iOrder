package com.galaxysoftware.istockenterpriseiorder.views.ui.outstand

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galaxysoftware.istockenterpriseiorder.api.ApiRepositories
import com.galaxysoftware.istockenterpriseiorder.api.NpgsqlDbType
import com.galaxysoftware.istockenterpriseiorder.api.ParameterDirection
import com.galaxysoftware.istockenterpriseiorder.api.ParameterHelper
import com.galaxysoftware.istockenterpriseiorder.models.CustOutstand
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat

class OutstandViewModel: ViewModel() {

    private var result: MutableLiveData<List<CustOutstand>>? = null
    public var totalresult:String="0.0"
    private val _text = MutableLiveData<String>().apply {
        val parameter : MutableList<ParameterHelper> = mutableListOf()
        parameter.add(ParameterHelper(PsqlParameterName = "_customerid", PsqlDbTypes = NpgsqlDbType.Integer.value, PsqlParameterDirection = ParameterDirection.Input.value, PsqlParameterValue = 2))

        ApiRepositories.Post(sqlquerystring = "getmobileoustandamt", isStoreProcedure = true, sqlparameters = parameter){
          value=it

        }
    }
    val text: LiveData<String> = _text
    fun loadData() : LiveData<List<CustOutstand>>{

        if(result == null)
        {
            result = MutableLiveData()
            val parameter : MutableList<ParameterHelper> = mutableListOf()
            parameter.add(ParameterHelper(PsqlParameterName = "_customerid", PsqlDbTypes = NpgsqlDbType.Integer.value, PsqlParameterDirection = ParameterDirection.Input.value, PsqlParameterValue = 2))

            ApiRepositories.Post(sqlquerystring = "getmobileoutstanding", isStoreProcedure = true, sqlparameters = parameter){
                val listType = object : TypeToken<List<CustOutstand>>() { }.type
                var codelist = Gson().fromJson<List<CustOutstand>>(it, listType)

                codelist.let {
                    result?.apply {
                        value = it
                    }
                }

            }
        }
       return result as MutableLiveData<List<CustOutstand>>
    }

    fun CurrencyFormat(obj: Any?): String {
        var currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance()
        var decimalFormatSymbols: DecimalFormatSymbols = (currencyFormat as DecimalFormat).decimalFormatSymbols
        decimalFormatSymbols.currencySymbol = ""
        (currencyFormat as DecimalFormat).decimalFormatSymbols = decimalFormatSymbols
        return currencyFormat.format(obj)
    }

}