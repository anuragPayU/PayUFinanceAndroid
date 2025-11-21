package com.payu.finance.domain.repository

import com.payu.finance.common.Result
import com.payu.finance.domain.model.HistoryScreenContent

/**
 * Repository interface for History Screen
 */
interface HistoryRepository : BaseRepository {
    /**
     * Fetch History Screen content
     */
    suspend fun getHistoryScreenContent(): Result<HistoryScreenContent>
}

