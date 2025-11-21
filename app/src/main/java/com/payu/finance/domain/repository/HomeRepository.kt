package com.payu.finance.domain.repository

import com.payu.finance.common.Result
import com.payu.finance.domain.model.HomeScreenContent

/**
 * Repository interface for Home Screen
 */
interface HomeRepository : BaseRepository {
    /**
     * Fetch Home Screen content
     */
    suspend fun getHomeScreenContent(): Result<HomeScreenContent>
}

