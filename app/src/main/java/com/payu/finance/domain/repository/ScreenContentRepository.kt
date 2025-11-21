package com.payu.finance.domain.repository

import com.payu.finance.common.Result
import com.payu.finance.domain.model.MobileInputScreenContent
import com.payu.finance.domain.model.OtpScreenContent
import com.payu.finance.domain.model.ProfileScreenContent

/**
 * Repository interface for Screen Content
 * ViewModel will use this to fetch screen content
 */
interface ScreenContentRepository : BaseRepository {
    /**
     * Fetch Mobile Input Screen content
     */
    suspend fun getMobileInputScreenContent(): Result<MobileInputScreenContent>
    
    /**
     * Fetch OTP Screen content
     */
    suspend fun getOtpScreenContent(): Result<OtpScreenContent>
    
    /**
     * Fetch Profile Screen content
     */
    suspend fun getProfileScreenContent(): Result<ProfileScreenContent>
}

