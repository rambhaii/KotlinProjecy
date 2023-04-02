package com.example.gcsalesapp.data


//enum class ErrorType {
//   ERROR_LOGOUT,
//    ERROR_SERVER,
//    ERROR_
//}

class Constants {
    companion object{

        const val SUB_URL = "MarketingAppServicesV3"//Live
        const val BASE_URL = "https://gc9.scci.co.in/"+ SUB_URL +"/" //Used for live

//        const val SUB_URL = "MV2"//development
//        const val BASE_URL = "http://udev.scci.co.in:88/"+ SUB_URL+"/" //Used for developemnt


//        const val SUB_URL = "MarketingAppServicesV2"//Live copy
//        const val BASE_URL = "http://udev.scci.co.in:88/"+ SUB_URL+"/" //Live copy


        const val ANDROID_OS = 1
        const val DEFAULT_LANGUAGE = 1
        const val APP_UPDATE = 1
        const val APP_MINOR_UPDATE = 1
        const val REQUEST_CODE_LOCATION_PERMISSION = 101

        const val SALES_DATA = "sales_data"
        const val AUTH_DATA = "auth_data"
        const val LOGIN_FLAG = "login_flag"
        const val LOCATION_INTERVAL = "location_interval"
       // const val Marketing_RepID = "MarketingRepID"

        const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
        const val NOTIFICATION_CHANNEL_NAME = "Tracking"
        const val NOTIFICATION_ID = 1

        const val LOCATION_UPDATE_INTERVAL = 120*1000L
        const val FASTEST_LOCATION_INTERVAL = 120*1000L
        const val SMALLEST_DISPLACEMENT = 0F
        const val DEALER_INTERACTION_REQUEST = 100

        const val EXCEPTION_ERROR = 107
        const val LOCAL_ERROR = 109
    }
}