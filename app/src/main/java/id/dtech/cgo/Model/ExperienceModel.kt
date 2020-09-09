package id.dtech.cgo.Model

class ExperienceModel {
    var id : String? = null
    var exp_title : String? = null
    var exp_type : ArrayList<String>? = null
    var rating : Double = 0.0
    var count_rating : Int = 0
    var currency : String? = null
    var price : Long = 0
    var payment_type : String? = null
    var cover_photo : PhotoModel? = null
    var list_photo : ArrayList<HashMap<String,Any>>? = null
    var viewType : Int = 0
}