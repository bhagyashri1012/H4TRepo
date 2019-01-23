package com.usit.hub4tickets.flight.ui

import com.usit.hub4tickets.flight.model.FilterModel

interface INotifier {
    fun notify(data: FilterModel.Filter)
}

