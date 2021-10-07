package com.vps.android.presentation.select_task.adapter

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.vps.android.core.recycler.RecyclerViewItem
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.ItemSelectTaskGoodBinding
import com.vps.android.domain.task.GoodItem

object TaskGoodAdapterDelegates {

    var selectedTypeId = -1

    fun goodDelegate(
        itemClick: TaskTypeClick,
    ) = adapterDelegateViewBinding<GoodItem, RecyclerViewItem, ItemSelectTaskGoodBinding>(
        { layoutInflater, root -> ItemSelectTaskGoodBinding.inflate(layoutInflater, root, false) }
    ) {
        bind { payloads ->
            binding.apply {
                itemName.text = item.name
                itemName.isChecked = item.id == selectedTypeId
                itemName.setSafeOnClickListener {
                    selectedTypeId = item.id
                    itemClick.invoke(item)
                }
            }
        }
    }

}
