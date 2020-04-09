package it.unibo.yahm.clientapplication

interface SensorData
data class AccelerationData(val xAcceleration: Float,
                            val yAcceleration: Float,
                            val zAcceleration: Float) : SensorData

data class GyroscopeData(val xAngularVelocity: Float,
                         val yAngularVelocity: Float,
                         val zAngularVelocity: Float) : SensorData

data class GpsData(val latitude: Double,
                   val longitude: Double,
                   val speed: Float) : SensorData