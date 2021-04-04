package com.galaxysoftware.istockenterpriseiorder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.galaxysoftware.istockenterpriseiorder.api.*
import com.galaxysoftware.istockenterpriseiorder.dao.SqlUtilities
import com.galaxysoftware.istockenterpriseiorder.database.OrderDatabase
import com.galaxysoftware.istockenterpriseiorder.databinding.ActivityMainBinding
import com.galaxysoftware.istockenterpriseiorder.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    val db by lazy {OrderDatabase.getInstance(this).userDao}
    lateinit var sqlUtilities: SqlUtilities



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sqlUtilities = SqlUtilities(this)
        DemoGetByQuery()


        //binding.btnclickme.setOnClickListener { DemoSave()}

        binding.btnclickme.setOnClickListener{
            //DemoUpload()
           // DemoSetData()
            DemoGetByQuery()
           // DemoEditInsertEntry()
        }

    }
    private fun DemoSave(users :List<User>){

        GlobalScope.async {
            val user = User(userid = 1, name = "ThuraTun",password = "015427",shortName = "TRT")

            db.InsertUsers(users)
        }

    }

    private fun DemoUpload(){
        //showProgressBar()
//        val apirepo = ApiRepositories()
//        apirepo.getApi {
//            var result = Gson().fromJson<PodcastResponse>(it,PodcastResponse::class.java)
//
//            Log.i("PodcastActivyity", "Results = $result")
//        }
        //hideProgressBar()

        ApiRepositories.Get {
            Log.i("PodcastActivyity", "Results = $it")

        }

    }

    private fun DemoSetData(){

        //val data = ApiHelper(IsStoredProcedure = true,SqlQuery = null,StoredProcedureName = "usp_GetAllSetupData",SqlExecutionType = 2)

        val parameter : MutableList<ParameterHelper> = mutableListOf<ParameterHelper>()
        parameter.add(ParameterHelper(PsqlParameterName = "_macaddress" , PsqlDbTypes = NpgsqlDbType.Text.value,PsqlParameterDirection = ParameterDirection.Input.value,PsqlParameterValue = "default"))

        ApiRepositories.Post(sqlquerystring = "usp_GetAllSetupData",isStoreProcedure = true,sqlparameters = parameter){
            Log.i("PodcastActivyity", "Results = $it")

        }

//        data.Parameters.add(parameter)
//
//        val apirepo = ApiRepositories()
//        apirepo.(data) {
//            Log.i("PodcastActivyity", "Results = $it")
//        }

    }

    private fun DemoEditInsertEntry(){


        val parameter : MutableList<ParameterHelper> = mutableListOf<ParameterHelper>()
        parameter.add(ParameterHelper(PsqlParameterName = "_edittranid" , PsqlDbTypes = NpgsqlDbType.Integer.value,PsqlParameterDirection = ParameterDirection.Input.value,PsqlParameterValue = -9))
        parameter.add(ParameterHelper(PsqlParameterName = "_newvoucher" , PsqlDbTypes = NpgsqlDbType.Boolean.value,PsqlParameterDirection = ParameterDirection.Input.value,PsqlParameterValue = false))
        parameter.add(ParameterHelper(PsqlParameterName = "_iseditvoucher" , PsqlDbTypes = NpgsqlDbType.Boolean.value,PsqlParameterDirection = ParameterDirection.Input.value,PsqlParameterValue = false))
        parameter.add(ParameterHelper(PsqlParameterName = "_userid" , PsqlDbTypes = NpgsqlDbType.Integer.value,PsqlParameterDirection = ParameterDirection.Input.value,PsqlParameterValue = 4))
        parameter.add(ParameterHelper(PsqlParameterName = "_entryformname" , PsqlDbTypes = NpgsqlDbType.Varchar.value,PsqlParameterDirection = ParameterDirection.Input.value,PsqlParameterValue = "WinSaleEntry"))

        //val data = ApiHelper(IsStoredProcedure = true,Parameters = mutableListOf(parameter),SqlQuery = null,StoredProcedureName = "usp_EditInsertEntry",SqlExecutionType = 2)


        ApiRepositories.Post(sqlquerystring = "usp_EditInsertEntry",isStoreProcedure = true,sqlparameters = parameter){
            Log.i("PodcastActivyity", "Results = $it")

        }

//        val apirepo = ApiRepositories()
//        apirepo.postApi(data) {
//            Log.i("PodcastActivyity", "Results = $it")
//        }
    }

    private fun DemoGetByQuery(){



//        val data = ApiHelper(IsStoredProcedure = false,SqlQuery = "select json_agg(posuser) from posuser",SqlExecutionType = SqlExecutionTypes.ExecuteOutput.value)
//        val apirepo = ApiRepositories()
//        apirepo.postApi(data) {
//
//
//            val listType = object : TypeToken<List<User>>() { }.type
//            var userlist = Gson().fromJson<List<User>>(it,listType)
//            Log.i("PodcastActivyity", "Results = $it")
//        }

        ApiRepositories.Post("select json_agg(posuser) from posuser",false){
                        val listType = object : TypeToken<List<User>>() { }.type
            var userlist = Gson().fromJson<List<User>>(it,listType)
            Log.i("PodcastActivyity", "Results = $it")
            DemoSave(userlist)
        }


        var userresult : List<User>? = listOf()
        sqlUtilities.getUsersDemo {
            userresult = it
        }


        var userreturnlist =sqlUtilities.getUsers()
    }

}