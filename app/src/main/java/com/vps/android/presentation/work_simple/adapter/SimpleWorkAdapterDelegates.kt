package com.vps.android.presentation.work_simple.adapter

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.vps.android.core.recycler.RecyclerViewItem
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.ItemWorkSimpleBinding
import com.vps.android.domain.mechanism.MechanismItem

typealias ItemClick = (MechanismItem) -> Unit

object SimpleWorkAdapterDelegates {

    var selectedId: Int = -1

    fun simpleWorkDelegate(
        itemClick: ItemClick,
    ) = adapterDelegateViewBinding<MechanismItem, RecyclerViewItem, ItemWorkSimpleBinding>(
        { layoutInflater, root -> ItemWorkSimpleBinding.inflate(layoutInflater, root, false) }
    ) {
        bind { payloads ->
            binding.apply {
                itemName.text = item.name.toString()
                itemName.isChecked = item.id == selectedId
                itemName.setSafeOnClickListener {
                    itemClick.invoke(item)
                }
            }
        }
    }

    fun clearSelection() {
        selectedId = -1
    }

    fun setSelection(mechanismId: Int) {
        selectedId = mechanismId
    }
}
