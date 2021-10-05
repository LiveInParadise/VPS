package com.vps.android.presentation.mechanism.adapter

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.vps.android.core.recycler.RecyclerViewItem
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.ItemMechanismBinding
import com.vps.android.domain.mechanism.MechanismItem

typealias ItemClick = (MechanismItem) -> Unit

object MechanismAdapterDelegates {

    fun mechanismDelegate(
        itemClick: ItemClick,
    ) = adapterDelegateViewBinding<MechanismItem, RecyclerViewItem, ItemMechanismBinding>(
        { layoutInflater, root -> ItemMechanismBinding.inflate(layoutInflater, root, false) }
    ) {
        bind { payloads ->
            binding.apply {
                itemName.text = item.name
                itemName.setSafeOnClickListener {
                    itemClick.invoke(item)
                }
            }
        }
    }

}
