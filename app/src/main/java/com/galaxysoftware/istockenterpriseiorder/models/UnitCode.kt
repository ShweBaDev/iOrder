package com.galaxysoftware.istockenterpriseiorder.models

data class UnitCode (
        val usrcode : String,
        val unittype : Int,
        val shortdesc : String,
        val saleprice : Double?
)