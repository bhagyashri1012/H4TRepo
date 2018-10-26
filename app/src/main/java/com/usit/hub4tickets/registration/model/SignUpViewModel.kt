package com.usit.hub4tickets.domain.presentation.screens.main

import android.content.Context
import com.usit.hub4tickets.domain.model.Hub4TicketsDomain

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class SignUpViewModel(var context: Context?) {
    var errorMessage: String? = null
    var hub4TicketsDomain: Hub4TicketsDomain = Hub4TicketsDomain()
}