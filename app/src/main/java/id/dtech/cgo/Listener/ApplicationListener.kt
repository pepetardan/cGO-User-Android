package id.dtech.cgo.Listener

import id.dtech.cgo.Model.*
import java.util.HashMap

class ApplicationListener {
    companion object {

        interface GuestListener {
            fun onGuestClicked(passengerMap : HashMap<String,Any>, position : Int)
        }

        interface ServiceExperienceListener {
            fun onServiceErrorClicked()
        }

        interface ActivityTypeListener {
            fun onActivityTypeClicked(model : ActivityTypeModel, isSelected : Boolean)
        }

        interface ExperienceDestinationListener {
            fun onExperienceDestinationClicked(model : ExpDestinationModel)
        }

        interface AddOnListener {
            fun onAddOnClicked(model : AddOnModel, selected : Boolean, position: Int)
        }

        interface AddOnCheckoutListener {
            fun onAddOnCheckoutClicked()
        }

        interface BankMethodListener {
            fun onBankMethodClicked(paymentMethodModel : PaymentMethodModel, position: Int,
                                    isBankCLicked : Boolean)
        }

        interface TransportationClassListener {
            fun onTransportationClassClicked(strClass : String)
        }

        interface TransportationResultListener {
            fun onTransportationClassClicked(transportationModel: TransportationModel, from : Int)
        }

        interface TimeOptionListener {
            fun onTimeOptionClicked(timeOptionModel: TimeOptionModel, from : Int)
        }

        interface SortByListener {
            fun onSortByClicked(sortMap : HashMap<String,Any>)
        }

        interface PackageListener {
            fun onPackageClicked(packageMap : HashMap<String,Any>)
        }

        interface GuideListener {
            fun onGuideClicked(guideMap : HashMap<String,Any>)
        }

        interface PaymentAvaibilityListener {
            fun onPaymentAvaibilityClicked(PaymentAvaibilityMap : HashMap<String,Any>)
        }

        interface BookingConfirmationListener {
            fun onBookingConfirmationClicked(BookingConfirmationMap : HashMap<String,Any>)
        }
    }
}