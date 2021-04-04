package com.galaxysoftware.istockenterpriseiorder.api

data class ApiHelper (

        var IsStoredProcedure : Boolean? = false,
        var SqlQuery : String? = null,
        var StoredProcedureName : String? = null,
        var Parameters : MutableList<MutableList<ParameterHelper>> = mutableListOf< MutableList<ParameterHelper>>(),
        var SqlExecutionType : Int? = null

)

data class ParameterHelper(

    var PsqlParameterName : String? = null,
    var PsqlDbTypes : Int? = null,
    var PsqlParameterDirection : Int? = null,
    var PsqlParameterValue : Any? = null
)


 enum class SqlExecutionTypes(var value:Int) {
    ExecuteOnly(0),
     ExecuteResult(1),
    ExecuteOutput(2)

}

enum class NpgsqlDbType(var value: Int)
{
    Boolean(2),
    Integer(9),
    Text(19),
    Varchar(22),
    Json(35)
}

enum class ParameterDirection(var value: Int){
    //
    // Summary:
    //     The parameter is an input parameter.
    Input(1),
    //
    // Summary:
    //     The parameter is an output parameter.
    Output(2),
    //
    // Summary:
    //     The parameter is capable of both input and output.
    InputOutput(3),
    //
    // Summary:
    //     The parameter represents a return value from an operation such as a stored procedure,
    //     built-in function, or user-defined function.
    ReturnValue(6)
}