package com.galaxysoftware.istockenterpriseiorder

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.galaxysoftware.istockenterpriseiorder.dao.SqlUtilities

 abstract class BaseActivity : AppCompatActivity() {

    lateinit var sqlUtilities: SqlUtilities

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sqlUtilities = SqlUtilities(this)
    }
}