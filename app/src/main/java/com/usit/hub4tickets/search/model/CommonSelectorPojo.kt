package com.usit.hub4tickets.search.model

import java.io.Serializable

/**
 * Created by Bhagyashri Burade
 * Date: 24/10/2018
 * Email: bhagyashri.burade@usit.net.in
 */
class CommonSelectorPojo(id: String, itemsName: String, type: String) : Serializable {
    var id: String? = id
    var itemsName: String? = itemsName
    var type: String? = type
    var isSelected: Boolean = false
}
