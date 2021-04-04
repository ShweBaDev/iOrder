package com.galaxysoftware.istockenterpriseiorder.views.ui.checkout

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galaxysoftware.istockenterpriseiorder.api.ApiRepositories
import com.galaxysoftware.istockenterpriseiorder.api.NpgsqlDbType
import com.galaxysoftware.istockenterpriseiorder.api.ParameterDirection
import com.galaxysoftware.istockenterpriseiorder.api.ParameterHelper
import com.galaxysoftware.istockenterpriseiorder.dao.SqlUtilities
import com.galaxysoftware.istockenterpriseiorder.models.ShoppingCartItem
import com.galaxysoftware.istockenterpriseiorder.models.VoucherId
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CheckoutViewModel(application: Application): ViewModel(){
    var cardItems: LiveData<List<ShoppingCartItem>>? = null
    private var sqlUtilities = SqlUtilities(application)

    private var docId: MutableLiveData<VoucherId>? = null

    public fun loadData() : LiveData<List<ShoppingCartItem>>? {
        cardItems = sqlUtilities.getCartItems()
        return cardItems
    }

    fun getDocId(_usrshort: String?) : LiveData<VoucherId>{
        docId  = MutableLiveData()
        val parameter : MutableList<ParameterHelper> = mutableListOf()
        parameter.add(ParameterHelper(PsqlParameterName = "_usershort" , PsqlDbTypes = NpgsqlDbType.Varchar.value,PsqlParameterDirection = ParameterDirection.Input.value,PsqlParameterValue = _usrshort))

        ApiRepositories.Post(sqlquerystring = "getmobiledocid",isStoreProcedure = true,sqlparameters = parameter){
            val listType = object : TypeToken<List<VoucherId>>() { }.type
            var docidlist = Gson().fromJson<List<VoucherId>>(it,listType)

            docidlist.let {
                docId?.apply {
                    value = it[0]
                }
            }
        }
        return docId as MutableLiveData<VoucherId>
    }
}