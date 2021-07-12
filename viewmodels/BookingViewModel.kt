package com.telemed.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.telemed.api.ApiSingleTonCore
import com.telemed.api.RequestBuilder
import com.telemed.api.request.AppointmentDeleteRequest
import com.telemed.api.request.AppointmentRequest
import com.telemed.api.request.CreateEditSlotRequest
import com.telemed.api.request.EditProfileRequest
import com.telemed.api.response.*
import com.telemed.models.*
import com.telemed.utils.Utils
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BookingViewModel(application: Application) : BaseViewModel(application) {

    var specialityObserver =
        MutableLiveData<Resource<Response<JsonArrayResponse<SpecialityModel>>>>()
    var cityObserver =
        MutableLiveData<Resource<Response<JsonArrayResponse<CityResponse>>>>()
    var consultantObserver =
        MutableLiveData<Resource<Response<JsonArrayResponse<ConsultantResponse>>>>()
    var updatePersonalInfoObserver =
        MutableLiveData<Resource<Response<JsonObjectResponse<UserModel>>>>()
    var updateWorkingInfoObserver =
        MutableLiveData<Resource<Response<JsonObjectResponse<UserModel>>>>()
    var updatePaymentInfoObserver =
        MutableLiveData<Resource<Response<JsonObjectResponse<UserModel>>>>()
    var profilePicObserver =
        MutableLiveData<ResourceWithData<Response<JsonArrayResponse<ImageUploadResponse>>>>()
    var uploadImageOnAmazonObserver = MutableLiveData<ResourceWithData<Response<Void>>>()
    var personalInfoInfoObserver =
        MutableLiveData<Resource<Response<JsonObjectResponse<PersonalInfoResponse>>>>()
    var advertisementObserver =
        MutableLiveData<Resource<Response<JsonArrayResponse<AdvertisementResponse>>>>()
    var staticPageObserver =
        MutableLiveData<Resource<Response<JsonObjectResponse<StaticPagesResponse>>>>()
    var slotsObserver =
        MutableLiveData<Resource<Response<JsonArrayResponse<SlotsResponse>>>>()
    var createSlotObserver =
        MutableLiveData<Resource<Response<JsonObjectResponse<UserModel>>>>()
    var profileObserver =
        MutableLiveData<Resource<Response<JsonArrayResponse<ProfileResponse>>>>()
    var bookAppointmentObserver =
        MutableLiveData<Resource<Response<JsonArrayResponse<UserModel>>>>()
    var deleteDocumentObserver =
        MutableLiveData<Resource<Response<JsonObjectResponse<UserModel>>>>()
    var editProfileObserver =
        MutableLiveData<Resource<Response<JsonObjectResponse<UserModel>>>>()
    var notificationToggleObserver =
        MutableLiveData<Resource<Response<JsonObjectResponse<UserModel>>>>()
    var faqInfoObserver = MutableLiveData<Resource<Response<JsonArrayResponse<FaqModel>>>>()
    var consultantDetailObserver =
        MutableLiveData<Resource<Response<JsonArrayResponse<ProfileResponse>>>>()
    var appointmentListObserver =
        MutableLiveData<Resource<Response<JsonArrayResponse<AppointmentResponse>>>>()
    var appointmentDetailObserver =
        MutableLiveData<Resource<Response<JsonArrayResponse<AppointmentDetailsResponse>>>>()
    var appointmentDocUploadObserver =
        MutableLiveData<Resource<Response<JsonObjectResponse<UserModel>>>>()

    var deleteAppointmentDocumentObserver =
        MutableLiveData<Resource<Response<JsonObjectResponse<UserModel>>>>()

    fun getCityApi(context: Context) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.city(
                Utils.getToken(
                    context
                )
            )
                .enqueue(object : Callback<JsonArrayResponse<CityResponse>> {
                    override fun onResponse(
                        call: Call<JsonArrayResponse<CityResponse>>,
                        response: Response<JsonArrayResponse<CityResponse>>
                    ) {

                        loading.value = false
                        val message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            cityObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonArrayResponse<CityResponse>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun getSpecialityApi(context: Context, index: Int, limit: Int) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.speciality(
                Utils.getToken(
                    context
                ), index, limit
            )
                .enqueue(object : Callback<JsonArrayResponse<SpecialityModel>> {
                    override fun onResponse(
                        call: Call<JsonArrayResponse<SpecialityModel>>,
                        response: Response<JsonArrayResponse<SpecialityModel>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            specialityObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonArrayResponse<SpecialityModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun getConsultantApi(context: Context, searchText: String, index: Int, limit: Int) {
        if (Utils.isNetworkAvailable(getApplication())) {
            if (searchText.isNullOrEmpty()) {
                hideKeyBoard.value = ""
                loading.value = true
            }
            ApiSingleTonCore.getInstance(getApplication()).apiService.consultant(
                Utils.getToken(
                    context
                ), searchText, index, limit
            )
                .enqueue(object : Callback<JsonArrayResponse<ConsultantResponse>> {
                    override fun onResponse(
                        call: Call<JsonArrayResponse<ConsultantResponse>>,
                        response: Response<JsonArrayResponse<ConsultantResponse>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            consultantObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonArrayResponse<ConsultantResponse>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun getStaticPageApi(context: Context, path: String) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.getStaticPage(
                Utils.getToken(
                    context
                ), path
            ).enqueue(object : Callback<JsonObjectResponse<StaticPagesResponse>> {
                override fun onResponse(
                    call: Call<JsonObjectResponse<StaticPagesResponse>>,
                    response: Response<JsonObjectResponse<StaticPagesResponse>>
                ) {

                    loading.value = false
                    var message = getMessage(response.body()?.message, response.errorBody())
                    if (isForceUpdateNeeded(response.headers(), message) == false) {
                        staticPageObserver.value =
                            Resource(response.code(), response, "" + message)

                    }

                }

                override fun onFailure(
                    call: Call<JsonObjectResponse<StaticPagesResponse>>,
                    t: Throwable
                ) {
                    onFail(t.toString())
                }
            })
        }
    }

    fun updatePersonalInfo(request: ProfileInfoRequest) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.personalInfo(
                Utils.getToken(
                    getApplication()
                ), request
            )
                .enqueue(object : Callback<JsonObjectResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<UserModel>>,
                        response: Response<JsonObjectResponse<UserModel>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            updatePersonalInfoObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun updateWorkingInfo(request: ProfileInfoRequest) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.workingInfo(
                Utils.getToken(
                    getApplication()
                ), request
            )
                .enqueue(object : Callback<JsonObjectResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<UserModel>>,
                        response: Response<JsonObjectResponse<UserModel>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            updateWorkingInfoObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun updatePaymentInfo(request: ProfileInfoRequest) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.paymentInfo(
                Utils.getToken(
                    getApplication()
                ), request
            )
                .enqueue(object : Callback<JsonObjectResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<UserModel>>,
                        response: Response<JsonObjectResponse<UserModel>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            updatePaymentInfoObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun profilePicApi(
        location: String,
        token: String,
        type: String,
        builder: RequestBuilder,
        showProgress: Boolean,
        extraData: MyExtraData
    ) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = showProgress

            ApiSingleTonCore.getInstance(getApplication()).apiService.profilePic(
                token,
                location,
                type,
                1
            )
                .enqueue(object : Callback<JsonArrayResponse<ImageUploadResponse>> {
                    override fun onResponse(
                        call: Call<JsonArrayResponse<ImageUploadResponse>>,
                        response: Response<JsonArrayResponse<ImageUploadResponse>>
                    ) {
                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            profilePicObserver.value =
                                ResourceWithData(response.code(), response, "" + message, extraData)
                            extraData.fileUploadInterface?.onPresignedUrlSuccess(
                                ResourceWithData(
                                    response.code(),
                                    response,
                                    "" + message,
                                    extraData
                                )
                            )

                        }
                    }

                    override fun onFailure(
                        call: Call<JsonArrayResponse<ImageUploadResponse>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun uploadImageOnAmazonApi(
        url: String,
        file: RequestBody,
        showProgress: Boolean,
        extraData: MyExtraData
    ) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = showProgress

            ApiSingleTonCore.getInstance(getApplication()).apiService.uploadImageOnAmazon(url, file)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(
                        call: Call<Void>,
                        response: Response<Void>
                    ) {
                        loading.value = false
                        uploadImageOnAmazonObserver.value =
                            ResourceWithData(response.code(), response, "", extraData)
                        extraData.fileUploadInterface?.onFileUploadSuccess(
                            ResourceWithData(
                                response.code(),
                                response,
                                "",
                                extraData
                            )
                        )
                    }

                    override fun onFailure(
                        call: Call<Void>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun getPersonalInfoApi(context: Context) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.personalInfo(
                Utils.getToken(
                    context
                )
            )
                .enqueue(object : Callback<JsonObjectResponse<PersonalInfoResponse>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<PersonalInfoResponse>>,
                        response: Response<JsonObjectResponse<PersonalInfoResponse>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            personalInfoInfoObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<PersonalInfoResponse>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun getAdvertisementApi(context: Context) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.advertisement(
                Utils.getToken(
                    context
                )
            )
                .enqueue(object : Callback<JsonArrayResponse<AdvertisementResponse>> {
                    override fun onResponse(
                        call: Call<JsonArrayResponse<AdvertisementResponse>>,
                        response: Response<JsonArrayResponse<AdvertisementResponse>>
                    ) {

                        loading.value = false
                        val message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            advertisementObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonArrayResponse<AdvertisementResponse>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun getSlotsApi(context: Context, date: String) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true
            val requestBuilder = RequestBuilder()
            requestBuilder.date = date
            ApiSingleTonCore.getInstance(getApplication()).apiService.slots(
                Utils.getToken(context),
                requestBuilder
            )
                .enqueue(object : Callback<JsonArrayResponse<SlotsResponse>> {
                    override fun onResponse(
                        call: Call<JsonArrayResponse<SlotsResponse>>,
                        response: Response<JsonArrayResponse<SlotsResponse>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            slotsObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonArrayResponse<SlotsResponse>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun createSlot(request: CreateEditSlotRequest) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.createSlot(
                Utils.getToken(
                    getApplication()
                ), request
            )
                .enqueue(object : Callback<JsonObjectResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<UserModel>>,
                        response: Response<JsonObjectResponse<UserModel>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            createSlotObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun editSlot(request: CreateEditSlotRequest) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.editSlot(
                Utils.getToken(
                    getApplication()
                ), request
            )
                .enqueue(object : Callback<JsonObjectResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<UserModel>>,
                        response: Response<JsonObjectResponse<UserModel>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            createSlotObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun deleteSlot(id: String) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.deleteSlot(
                Utils.getToken(
                    getApplication()
                ), id
            )
                .enqueue(object : Callback<JsonObjectResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<UserModel>>,
                        response: Response<JsonObjectResponse<UserModel>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            createSlotObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun profileDetails(id: String) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.profile(
                Utils.getToken(
                    getApplication()
                ), id
            )
                .enqueue(object : Callback<JsonArrayResponse<ProfileResponse>> {
                    override fun onResponse(
                        call: Call<JsonArrayResponse<ProfileResponse>>,
                        response: Response<JsonArrayResponse<ProfileResponse>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            profileObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonArrayResponse<ProfileResponse>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun getConsultantSlotsApi(context: Context, consultantId: String, date: String) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true
            val requestBuilder = RequestBuilder()
            requestBuilder.date = date
            requestBuilder.consultantId = consultantId
            ApiSingleTonCore.getInstance(getApplication()).apiService.consultantSlots(
                Utils.getToken(context),
                requestBuilder
            )
                .enqueue(object : Callback<JsonArrayResponse<SlotsResponse>> {
                    override fun onResponse(
                        call: Call<JsonArrayResponse<SlotsResponse>>,
                        response: Response<JsonArrayResponse<SlotsResponse>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            slotsObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonArrayResponse<SlotsResponse>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }


    fun bookAppointmentApi(context: Context, request: AppointmentRequest) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true
            ApiSingleTonCore.getInstance(getApplication()).apiService.bookAppointment(
                Utils.getToken(context),
                request
            )
                .enqueue(object : Callback<JsonArrayResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonArrayResponse<UserModel>>,
                        response: Response<JsonArrayResponse<UserModel>>
                    ) {

                        loading.value = false
                        val message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            bookAppointmentObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonArrayResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun deleteDocumentApi(context: Context, url: String) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true
            val requestBuilder = RequestBuilder()
            requestBuilder.key = url
            ApiSingleTonCore.getInstance(getApplication()).apiService.deleteDocument(
                Utils.getToken(getApplication()),
                requestBuilder
            )
                .enqueue(object : Callback<JsonObjectResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<UserModel>>,
                        response: Response<JsonObjectResponse<UserModel>>
                    ) {

                        loading.value = false
                        val message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            deleteDocumentObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun editProfileApi(context: Context, request: EditProfileRequest) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true
            ApiSingleTonCore.getInstance(getApplication()).apiService.editProfile(
                Utils.getToken(context),
                request
            )
                .enqueue(object : Callback<JsonObjectResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<UserModel>>,
                        response: Response<JsonObjectResponse<UserModel>>
                    ) {

                        loading.value = false
                        val message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            editProfileObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun toggleNotification(context: Context) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true
            ApiSingleTonCore.getInstance(getApplication()).apiService.toggleNotification(
                Utils.getToken(context)
            )
                .enqueue(object : Callback<JsonObjectResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<UserModel>>,
                        response: Response<JsonObjectResponse<UserModel>>
                    ) {

                        loading.value = false
                        val message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            notificationToggleObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun toggleOnline(context: Context) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true
            ApiSingleTonCore.getInstance(getApplication()).apiService.toggleOnline(
                Utils.getToken(context)
            )
                .enqueue(object : Callback<JsonObjectResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<UserModel>>,
                        response: Response<JsonObjectResponse<UserModel>>
                    ) {

                        loading.value = false
                        val message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            notificationToggleObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun toggleAppointment(context: Context) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true
            ApiSingleTonCore.getInstance(getApplication()).apiService.toggleAppointment(
                Utils.getToken(context)
            )
                .enqueue(object : Callback<JsonObjectResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<UserModel>>,
                        response: Response<JsonObjectResponse<UserModel>>
                    ) {

                        loading.value = false
                        val message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            notificationToggleObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun faqList(context: Context) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.faq(
                Utils.getToken(
                    context
                )
            )
                .enqueue(object : Callback<JsonArrayResponse<FaqModel>> {
                    override fun onResponse(
                        call: Call<JsonArrayResponse<FaqModel>>,
                        response: Response<JsonArrayResponse<FaqModel>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            faqInfoObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonArrayResponse<FaqModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun consultantDetails(id: String) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.consultantDetails(
                Utils.getToken(
                    getApplication()
                ), id
            )
                .enqueue(object : Callback<JsonArrayResponse<ProfileResponse>> {
                    override fun onResponse(
                        call: Call<JsonArrayResponse<ProfileResponse>>,
                        response: Response<JsonArrayResponse<ProfileResponse>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            consultantDetailObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonArrayResponse<ProfileResponse>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun appointmentList(type: String) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.appointmentList(
                Utils.getToken(
                    getApplication()
                ), type
            )
                .enqueue(object : Callback<JsonArrayResponse<AppointmentResponse>> {
                    override fun onResponse(
                        call: Call<JsonArrayResponse<AppointmentResponse>>,
                        response: Response<JsonArrayResponse<AppointmentResponse>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            appointmentListObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonArrayResponse<AppointmentResponse>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun appointmentDetails(id: String) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.appointmentDetails(
                Utils.getToken(
                    getApplication()
                ), id
            )
                .enqueue(object : Callback<JsonArrayResponse<AppointmentDetailsResponse>> {
                    override fun onResponse(
                        call: Call<JsonArrayResponse<AppointmentDetailsResponse>>,
                        response: Response<JsonArrayResponse<AppointmentDetailsResponse>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            appointmentDetailObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonArrayResponse<AppointmentDetailsResponse>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun cancelAppointment(id: String) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.cancelAppointment(
                Utils.getToken(
                    getApplication()
                ), id
            )
                .enqueue(object : Callback<JsonObjectResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<UserModel>>,
                        response: Response<JsonObjectResponse<UserModel>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            createSlotObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun uploadAppointmentDoc(request: DocumentsItemDetails) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true

            ApiSingleTonCore.getInstance(getApplication()).apiService.uploadAppointmentDoc(
                Utils.getToken(
                    getApplication()
                ), request
            )
                .enqueue(object : Callback<JsonObjectResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<UserModel>>,
                        response: Response<JsonObjectResponse<UserModel>>
                    ) {

                        loading.value = false
                        var message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            appointmentDocUploadObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

    fun deleteAppointmentDocApi(context: Context, requestBuilder: AppointmentDeleteRequest) {
        if (Utils.isNetworkAvailable(getApplication())) {
            hideKeyBoard.value = ""
            loading.value = true
            ApiSingleTonCore.getInstance(getApplication()).apiService.deleteAppointmentDocument(
                Utils.getToken(getApplication()),
                requestBuilder
            )
                .enqueue(object : Callback<JsonObjectResponse<UserModel>> {
                    override fun onResponse(
                        call: Call<JsonObjectResponse<UserModel>>,
                        response: Response<JsonObjectResponse<UserModel>>
                    ) {

                        loading.value = false
                        val message = getMessage(response.body()?.message, response.errorBody())
                        if (isForceUpdateNeeded(response.headers(), message) == false) {
                            deleteAppointmentDocumentObserver.value =
                                Resource(response.code(), response, "" + message)

                        }

                    }

                    override fun onFailure(
                        call: Call<JsonObjectResponse<UserModel>>,
                        t: Throwable
                    ) {
                        onFail(t.toString())
                    }
                })
        }
    }

}
