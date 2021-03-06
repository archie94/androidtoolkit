package com.arka.prava.util.core.rv

import com.arka.prava.util.core.rv.IRvData

/**
 * Created by Sidharth Sethia on 26/06/20.
 * Copyright (c) 2020 UltraHuman HealthCare Pvt Ltd. All rights reserved.
 */
interface RvImpressionTracker {
    fun trackImpression(data: IRvData, position: Int)
}
