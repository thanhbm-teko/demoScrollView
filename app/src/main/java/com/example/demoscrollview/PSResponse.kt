package com.example.demoscrollview

data class Product(val name: String, val pv_sku: String) {}

data class PSQueryData(val products: Array<Product>) {}

data class PSResponseData(val data: PSQueryData) {}

data class PSResponse (val code: Int, val message: String, val data: PSResponseData) {}