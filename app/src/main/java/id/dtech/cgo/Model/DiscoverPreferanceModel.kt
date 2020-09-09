package id.dtech.cgo.Model

class DiscoverPreferanceModel {
    var province_id : Int = 0
    var province_name : String? = null
    var city_id : Int = 0
    var city : String? = null
    var city_desc : String? = null
    var city_photos : ArrayList<PhotoModel>? = null
    var harbors_id : String? = null
    var harbors_name : String? = null
    var item : ArrayList<ExperienceModel>? = null
}