package com.wynneplaga.beyondfilm

import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.turingtechnologies.materialscrollbar.INameableAdapter

class NamedFastAdapter<T: GenericItem>(private val objectToNameBlock: (T) -> String): FastAdapter<T>(), INameableAdapter {

    override fun getCharacterForElement(element: Int): Char {
        return objectToNameBlock(getItem(element) ?: return '?').first()
    }

}