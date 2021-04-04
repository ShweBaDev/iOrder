package com.galaxysoftware.istockenterpriseiorder.views.ui.itemdetail

import android.renderscript.Script
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galaxysoftware.istockenterpriseiorder.api.ApiRepositories
import com.galaxysoftware.istockenterpriseiorder.api.NpgsqlDbType
import com.galaxysoftware.istockenterpriseiorder.api.ParameterDirection
import com.galaxysoftware.istockenterpriseiorder.api.ParameterHelper
import com.galaxysoftware.istockenterpriseiorder.models.ShoppingCartItem
import com.galaxysoftware.istockenterpriseiorder.models.UsrCode
import com.galaxysoftware.istockenterpriseiorder.models.VoucherId
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ItemDetailViewModel : ViewModel() {

    private var result: MutableLiveData<UsrCode>? = null

    private var docidResult: MutableLiveData<VoucherId>? = null

    lateinit var selectedcaritem : ShoppingCartItem

    fun loadData(_usrcode : String) : LiveData<UsrCode> {

        if(result == null)
        {
            result = MutableLiveData()
            val parameter : MutableList<ParameterHelper> = mutableListOf()
            parameter.add(ParameterHelper(PsqlParameterName = "_usercode" , PsqlDbTypes = NpgsqlDbType.Varchar.value,PsqlParameterDirection = ParameterDirection.Input.value,PsqlParameterValue = _usrcode))

            ApiRepositories.Post(sqlquerystring = "getmobileusrcode",isStoreProcedure = true,sqlparameters = parameter){
                val listType = object : TypeToken<List<UsrCode>>() { }.type
                var codelist = Gson().fromJson<List<UsrCode>>(it,listType)

                codelist.let {
                    result?.apply {
                        value = it[0]
                    }
                }

                selectedcaritem = ShoppingCartItem(
                        usrCode = codelist[0].usrcode,
                        description = codelist[0].description ,
                        unitqty = 1.0,
                        unittypeid = 1,
                        voucherNo = null,
                        saleprice = codelist[0].saleprice)
            }
        }
        return result as MutableLiveData<UsrCode>
    }
}