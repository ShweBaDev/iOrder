package com.galaxysoftware.istockenterpriseiorder.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.galaxysoftware.istockenterpriseiorder.BaseActivity
import com.galaxysoftware.istockenterpriseiorder.api.ApiRepositories
import com.galaxysoftware.istockenterpriseiorder.api.NpgsqlDbType
import com.galaxysoftware.istockenterpriseiorder.api.ParameterDirection
import com.galaxysoftware.istockenterpriseiorder.api.ParameterHelper
import com.galaxysoftware.istockenterpriseiorder.databinding.ActivitySignInBinding
import com.galaxysoftware.istockenterpriseiorder.models.User
import com.galaxysoftware.istockenterpriseiorder.utils.Utility.hideProgressBar
import com.galaxysoftware.istockenterpriseiorder.utils.Utility.showProgressBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SignInActivity : BaseActivity() {

    private lateinit var binding: ActivitySignInBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener{
           signIn()
        }
    }

    private fun signIn(){

        showProgressBar()

        val username = binding.inputEmail.text
        val password = binding.inputPassword.text

        val parameter : MutableList<ParameterHelper> = mutableListOf<ParameterHelper>()
        parameter.add(ParameterHelper(PsqlParameterName = "_username" , PsqlDbTypes = NpgsqlDbType.Varchar.value,PsqlParameterDirection = ParameterDirection.Input.value,PsqlParameterValue = username.toString()))

        ApiRepositories.Post(sqlquerystring = "getmobileuser",isStoreProcedure = true,sqlparameters = parameter){
            val listType = object : TypeToken<List<User>>() { }.type
            var users = Gson().fromJson<List<User>>(it,listType)

            users.let {

                if(password.toString()== users.first().password.toString())
                {

                    sqlUtilities.saveUsers(users)

                    val intent = Intent(this, MainConsoleActivity::class.java)
                    startActivity(intent)

                    hideProgressBar()
                }
                else
                {
                    hideProgressBar()
                }
            }
        }
    }
}