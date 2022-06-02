package com.example.foodkatta

import java.io.Serializable

data class MyData(val id:String,val name:String,val nameId:String,val contact:String,val balance:String,val paid:String,val group: String)

data class Memberdata(val id:String,var name: String, var nameId: String, var contact: String, var balance:String, var paid:String,var group: String) :
    Serializable

