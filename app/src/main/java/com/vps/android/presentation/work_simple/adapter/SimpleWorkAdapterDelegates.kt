package com.vps.android.presentation.work_simple.adapter

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.vps.android.core.recycler.RecyclerViewItem
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.ItemWorkSimpleBinding
import com.vps.android.domain.mechanism.MechanismItem

typealias ItemClick = (MechanismItem) -> Unit

object SimpleWorkAdapterDelegates {

    var selectedIds: ArrayList<Int> = arrayListOf()

    fun simpleWorkDelegate(
        itemClick: ItemClick,
    ) = adapterDelegateViewBinding<MechanismItem, RecyclerViewItem, ItemWorkSimpleBinding>(
        { layoutInflater, root -> ItemWorkSimpleBinding.inflate(layoutInflater, root, false) }
    ) {
        bind { payloads ->
            binding.apply {
                itemName.text = item.id.toString()
                itemName.isChecked = selectedIds.contains(item.id)
                itemName.setSafeOnClickListener {
                    itemClick.invoke(item)
                }
            }
        }
    }

    fun changeSelection(itemId: Int) {
        val contains = selectedIds.contains(itemId)
        if (contains) {
            selectedIds.removeAll { it == itemId }
        } else {
            selectedIds.add(itemId)
        }
    }

}
