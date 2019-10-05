package com.christianrodier.pokemonandroid

import android.location.Location


class Pokemon {
    var name: String? = null
    var description: String? = null
    var image: Int? = null
    var power: Double? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var wasCaught: Boolean? = false
    var location:Location? = null

    constructor(image: Int, name: String, description: String, power: Double, latitude: Double, longitude: Double){

        this.name = name
        this.description = description
        this.image = image
        this.power = power
        this.latitude = latitude
        this.longitude = longitude
        this.wasCaught = false
        this.location = Location(name)
        this.location!!.latitude = latitude
        this.location!!.longitude = longitude
    }


}