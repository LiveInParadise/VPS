package com.vps.android.presentation.main.adapter

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.vps.android.core.ext.visible
import com.vps.android.core.recycler.RecyclerViewItem
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.ItemTaskBinding
import com.vps.android.domain.task.TaskInfo

typealias EditClick = (TaskInfo) -> Unit
typealias TaskActionClick = (TaskInfo, Boolean) -> Unit

object TaskAdapterDelegates {

    fun taskDelegate(
        editClick: EditClick,
        actionClick: TaskActionClick,
    ) = adapterDelegateViewBinding<TaskInfo, RecyclerViewItem, ItemTaskBinding>(
        { layoutInflater, root -> ItemTaskBinding.inflate(layoutInflater, root, false) }
    ) {
        bind { payloads ->
            binding.apply {
                tvTaskType.text = item.taskTypeName
                tvTaskLoading.text = item.loadingPlaceName
                tvTaskGood.text = item.goodsName
                tvTaskTech.text = item.selectedMechanismsInfo.joinToString(", ") { it.name }

                btnStart.visible(!item.isActive())
                btnStop.visible(item.isActive())

                btnEditSimple.setSafeOnClickListener {
                    editClick.invoke(item)
                }

                btnStart.setSafeOnClickListener {
                    actionClick.invoke(item, true)
                }

                btnStop.setSafeOnClickListener {
                    actionClick.invoke(item, false)
                }
            }
        }
    }

}
