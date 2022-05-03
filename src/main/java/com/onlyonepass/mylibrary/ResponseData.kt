package com.onlyonepass.mylibrary

data class ResponseData(
    var status:Boolean,
    var result:List<ReturnData>,
    var message:String,
    var amount:String?,
    var memo:String?,
    var currency:String?,
    var email:String?,
    var mobile:String?
)
data class ReturnData(
   var id:String,
   var name:String,
   var publicKey:String
)
