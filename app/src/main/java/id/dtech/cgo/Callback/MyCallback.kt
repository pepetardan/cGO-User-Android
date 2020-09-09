package id.dtech.cgo.Callback

import id.dtech.cgo.Model.*

class MyCallback {
    companion object {
        interface TripInspirationCallback {
            fun onTripInspirationPrepare()
            fun onTripInspirationLoaded(inspirationList: ArrayList<TripInspirationModel>)
            fun onTripInspirationError()
        }

        interface PromoCallback {
            fun onPromoPrepare()
            fun onPromoLoaded(promoList: ArrayList<PromoModel>)
            fun onPromoError()
        }

        interface SpecialPromoCallback {
            fun onSpecialPromoPrepare()
            fun onSpecialPromoLoaded(promoModel: PromoModel)
            fun onSpecialPromoError(message: String)
        }

        interface DiscoverPreferanceCallback {
            fun onDiscoverPreferancePrepare()
            fun onDiscoverPreferanceLoaded(discoverList: ArrayList<DiscoverPreferanceModel>)
            fun onDiscoverPreferanceError()
        }

        interface ExperienceSearchCallback {
            fun onExperienceSearchPrepare()
            fun onExperienceSearchLoaded(experienceList: ArrayList<ExperienceModel>, totalRecords : Int)
            fun onExperienceSearchError()
        }

        interface ExperienceDestinationCallback {
            fun onExperienceDestinationPrepare()
            fun onExperienceDestinationLoaded(expDestination: ArrayList<ExpDestinationModel>)
            fun onExperienceDestinationError()
        }

        interface ExperienceDetailCallback {
            fun onExperienceDetailPrepare()
            fun onExperienceDetailLoaded(expDetailModel: ExperienceDetailModel)
            fun onExperienceDetailError()
        }

        interface ExperienceReviewCallback {
            fun onExperienceReviewPrepare()
            fun onExperienceReviewLoaded(reviewList: ArrayList<ReviewModel>)
            fun onExperienceReviewError()
        }

        interface OTPCallback {
            fun onOTPPrepare()
            fun onOTPSuccess(code: String, expiredDate: String, expiredMilis: Long)
            fun onOTPError()
        }

        interface LoginCallback {
            fun onLoginPrepare()
            fun onLoginSuccess(data: HashMap<String, Any>)
            fun onLoginError(message: String, code: Int)
        }

        interface RegisterCallback {
            fun onRegisterPrepare()
            fun onRegisterSuccess(data: HashMap<String, Any>)
            fun onRegisterError(message: String, code: Int)
        }

        interface AddOnCallback {
            fun onAddOnPrepare()
            fun onAddOnSuccess(data: java.util.ArrayList<AddOnModel>)
            fun onAddOnError()
        }

        interface UserInfoCallback {
            fun onUserInfoPrepare()
            fun onUserInfoSuccess(data: HashMap<String, Any>)
            fun onUserInfoError(error: String)
        }

        interface PaymentMethodCallback {
            fun onPaymentMethodPrepare()
            fun onPaymentMethodSuccess(
                bankMethodList: ArrayList<PaymentMethodModel>,
                creditCardMethodList: ArrayList<PaymentMethodModel>,
                paypalMethodList: ArrayList<PaymentMethodModel>
            )

            fun onPaymentMethodError(error: String)
        }

        interface CreatePaymentCallback {
            fun onCreatePaymentPrepare()
            fun onCreatePaymentSuccess(data: HashMap<String, Any>)
            fun onCreatePaymentError(message: String)
        }

        interface ConfirmationPaymentCallback {
            fun onConfirmationPaymentPrepare()
            fun onConfirmationPaymentSuccess(data: HashMap<String, Any>)
            fun onConfirmationPaymentError(message: String)
        }

        interface CreateBookingCallback {
            fun onCreateBookingPrepare()
            fun onCreateBookingSuccess(data: HashMap<String, Any>)
            fun onCreateBookingError(message: String)
        }

        interface DetailBookingCallback {
            fun onDetailBookingPrepare()
            fun onDetailBookingSuccess(data: HashMap<String, Any>)
            fun onDetailBookingError(message: String)
        }

        interface MyBookingCallback {
            fun onMyBookingPrepare()
            fun onMyBookingSuccess(data: ArrayList<HashMap<String, Any>>)
            fun onMyBookingError(message: String)
        }

        interface MyBookingHistoryCallback {
            fun onMyBookingHistoryPrepare()
            fun onMyBookingHistorySuccess(data: ArrayList<HashMap<String, Any>>)
            fun onMyBookingHistoryError(message: String)
        }

        interface TimeOptionCallback {
            fun onTimeOptionPrepare()
            fun onTimeOptionSuccess(timeOptionList : ArrayList<TimeOptionModel>)
            fun onTimeOptionError(message: String)
        }

        interface CreateWishlistCallback {
            fun onCreateWishlistPrepare()
            fun onCreateWishlistSuccess()
            fun onCreateWishlistError(errorCode : Int)
        }

        interface CheckWishlistCallback {
            fun onCheckWishlistPrepare()
            fun onCheckWishlistSuccess(isExist : Boolean)
            fun onCheckWishlistError()
        }

        interface PdfFileCallback {
            fun onPdfFilePrepare()
            fun onPdfFileSuccess(fileURL : String)
            fun onPdfFileError(error : String)
        }

        interface TransportationSearchCallback {
            fun onTransportationSearchPrepare()
            fun onTransportationSearchSuccess(
                transportationList: ArrayList<TransportationModel>,
                metaData: HashMap<String, Any>
            )

            fun onTransportationSearchError(message: String)
        }

        interface CreateReviewCallback {
            fun onCreateReviewPrepare()
            fun onCreateReviewSuccess()
            fun onCreateReviewError(message: String, code: Int)
        }

        interface EditProfileCallback {
            fun onEditProfilePrepare()
            fun onEditProfileSuccess(profile_pict_url : String)
            fun onEditProfileError(message: String, code: Int)
        }

        interface RefreshTokenCallback {
            fun onRefreshTokenPrepare()
            fun onRefreshTokenSuccess(data : HashMap<String,Any>)
            fun onRefreshTokenError(message: String, code: Int)
        }

        interface ExchangeRatesCallback {
            fun onExchangeRatesPrepare()
            fun onExchangeRatesSuccess(rates : Double)
            fun onExchangeRatesError(message: String, code: Int)
        }

        interface VersionCallback {
            fun onVersionPrepare()
            fun onVersionSuccess(versionCode : Int)
            fun onVersionError()
        }
    }
}