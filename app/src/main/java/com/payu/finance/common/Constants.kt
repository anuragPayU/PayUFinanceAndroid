package com.payu.finance.common

object Constants {
    const val BASE_URL = "https://sbox-intrasetu.payufin.io/"
    
    // API Endpoints
    object Endpoints {
        const val LOANS = "loans"
        const val REPAYMENTS = "repayments"
    }
    
    // Network
    const val NETWORK_TIMEOUT = 30L // seconds
    const val CONNECT_TIMEOUT = 10L // seconds
    const val READ_TIMEOUT = 30L // seconds
    
    // Legal URLs
    const val PRIVACY_POLICY_URL = "https://www.payu.in/privacy-policy" // TODO: Replace with actual Privacy Policy URL
    const val TERMS_AND_CONDITIONS_URL = "https://www.payu.in/terms-and-conditions" // TODO: Replace with actual T&C URL
}

