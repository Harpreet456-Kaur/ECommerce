package com.example.e_commerce.models

data class ProductModel(
    var key: String?= null,
    var name: String?=null,
    var price: String?=null,
    var description: String?=null,
    var catId: String?=null,
    var subCatId: String?=null
): java.io.Serializable