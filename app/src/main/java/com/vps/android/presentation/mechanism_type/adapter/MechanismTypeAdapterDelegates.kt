package com.vps.android.presentation.mechanism_type.adapter

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.vps.android.core.recycler.RecyclerViewItem
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.ItemMechanismTypeBinding
import com.vps.android.domain.mechanism.MechanismType

typealias ItemClick = (MechanismType) -> Unit

object MechanismTypeAdapterDelegates {

    fun mechanismTypeDelegate(
        itemClick: ItemClick,
    ) = adapterDelegateViewBinding<MechanismType, RecyclerViewItem, ItemMechanismTypeBinding>(
        { layoutInflater, root -> ItemMechanismTypeBinding.inflate(layoutInflater, root, false) }
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
