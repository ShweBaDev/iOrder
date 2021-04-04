package com.galaxysoftware.istockenterpriseiorder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import com.galaxysoftware.istockenterpriseiorder.api.ApiRepositories
import com.galaxysoftware.istockenterpriseiorder.api.NpgsqlDbType
import com.galaxysoftware.istockenterpriseiorder.api.ParameterDirection
import com.galaxysoftware.istockenterpriseiorder.api.ParameterHelper
import com.galaxysoftware.istockenterpriseiorder.dao.SqlUtilities
import com.galaxysoftware.istockenterpriseiorder.database.OrderDatabase
import com.galaxysoftware.istockenterpriseiorder.models.User
import com.galaxysoftware.istockenterpriseiorder.utils.Utility.hideProgressBar
import com.galaxysoftware.istockenterpriseiorder.utils.Utility.showProgressBar
import com.galaxysoftware.istockenterpriseiorder.views.MainConsoleActivity
import com.galaxysoftware.istockenterpriseiorder.views.SignInActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.internal.wait


class SplashScreenActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkUserStatus()
    }

    private fun checkUserStatus(){

        sqlUtilities.getUsersDemo{
            if(it?.count() == 0){

                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            else{

                val intent = Intent(this, MainConsoleActivity::class.java)
                startActivity(intent)
            }
        }
    }
}