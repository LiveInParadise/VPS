package com.vps.android.presentation.select_task.adapter

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.vps.android.R
import com.vps.android.core.ext.visible
import com.vps.android.core.recycler.BaseDelegationAdapter
import com.vps.android.core.recycler.RecyclerViewItem
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.ItemMultiSelectTaskBinding
import com.vps.android.databinding.ItemSelectTaskBinding
import com.vps.android.databinding.ItemSelectTaskGoodTitleBinding
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.task.CreateTaskItem
import com.vps.android.domain.task.FullGoodItem
import com.vps.android.domain.task.PlaceItem
import com.vps.android.domain.task.TaskTypeItem

typealias TaskTypeClick = (CreateTaskItem) -> Unit
typealias TaskTechClick = (MechanismItem) -> Unit

object SelectTaskParameterAdapterDelegates {

    private val viewPool = RecyclerView.RecycledViewPool()

    var selectedTypeId = -1
    var selectedTechIds: ArrayList<Int> = arrayListOf()

    fun taskDelegate(
        itemClick: TaskTypeClick,
    ) = adapterDelegateViewBinding<TaskTypeItem, RecyclerViewItem, ItemSelectTaskBinding>(
        { layoutInflater, root -> ItemSelectTaskBinding.inflate(layoutInflater, root, false) }
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

    fun loadingDelegate(
        itemClick: TaskTypeClick,
    ) = adapterDelegateViewBinding<PlaceItem, RecyclerViewItem, ItemSelectTaskBinding>(
        { layoutInflater, root -> ItemSelectTaskBinding.inflate(layoutInflater, root, false) }
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

    fun goodDelegate(
        itemClick: TaskTypeClick,
    ) = adapterDelegateViewBinding<FullGoodItem, RecyclerViewItem, ItemSelectTaskGoodTitleBinding>(
        { layoutInflater, root -> ItemSelectTaskGoodTitleBinding.inflate(layoutInflater, root, false) }
    ) {
        bind { payloads ->
            binding.apply {
                if (item.categoryName.isNullOrEmpty()) {
                    itemTitle.visible(false)
                    recyclerView.visible(true)
                } else {
                    itemTitle.visible(true)
                    itemTitle.text = item.categoryName
                }
                val goodItemsAdapter = BaseDelegationAdapter(TaskGoodAdapterDelegates.goodDelegate(itemClick))
                TaskGoodAdapterDelegates.selectedTypeId = selectedTypeId

                val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
                val drawable = ContextCompat.getDrawable(context, R.drawable.divider_list)
                drawable?.let { dividerItemDecoration.setDrawable(it) }
                recyclerView.apply {
                    addItemDecoration(dividerItemDecoration)
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    adapter = goodItemsAdapter
                    setRecycledViewPool(viewPool)
                }
                goodItemsAdapter.items = item.goodsList

                itemTitle.setOnClickListener {
                    recyclerView.isVisible = !recyclerView.isVisible
                }
            }
        }
    }

    fun techDelegate(
        itemClick: TaskTechClick,
    ) = adapterDelegateViewBinding<MechanismItem, RecyclerViewItem, ItemMultiSelectTaskBinding>(
        { layoutInflater, root -> ItemMultiSelectTaskBinding.inflate(layoutInflater, root, false) }
    ) {
        bind { payloads ->
            binding.apply {
                itemName.text = item.name
                itemName.isChecked = selectedTechIds.contains(item.id)
                itemName.setSafeOnClickListener {
                    val contains = selectedTechIds.contains(item.id)
                    if (contains) {
                        selectedTechIds.removeAll { it == item.id }
                    } else {
                        selectedTechIds.add(item.id)
                    }
                    itemClick.invoke(item)
                }
            }
        }
    }

}
