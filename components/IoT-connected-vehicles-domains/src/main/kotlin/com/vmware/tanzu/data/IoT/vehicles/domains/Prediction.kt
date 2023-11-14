package com.vmware.tanzu.data.IoT.vehicles.domains

data class Prediction(val vin : String, var prediction: Prediction,  var probability : Float)
