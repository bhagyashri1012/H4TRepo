package com.usit.hub4tickets.flight.ui

import com.usit.hub4tickets.flight.model.FilterModel

interface INotifierOneWay {
    fun notify(data: FilterModel.Filter)
}

