package com.vps.android.presentation.main.adapter

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.vps.android.core.ext.visible
import com.vps.android.core.recycler.RecyclerViewItem
import com.vps.android.core.utils.CommonUtils
import com.vps.android.core.utils.setSafeOnClickListener
import com.vps.android.databinding.ItemTaskBinding
import com.vps.android.domain.mechanism.MechanismTypeClass
import com.vps.android.domain.task.TaskInfo
import java.util.*
import kotlin.concurrent.schedule

typealias EditClick = (TaskInfo, Boolean) -> Unit
typealias TaskActionClick = (TaskInfo, Boolean) -> Unit

object TaskAdapterDelegates {

    var mechanismTypeClass = MechanismTypeClass.SIMPLE
    val taskTimerHelper: HashMap<Int, Long?> = hashMapOf()

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
                tvTaskUnloading.text = item.unloadingPlaceName
                tvTaskGood.text = item.goodsName
                tvTaskTech.text = item.selectedMechanismsInfo.joinToString(", ") { it.name }

                btnStart.visible(!item.isActive())
                btnStop.visible(item.isActive())

                btnEditNotActive.visible(!item.isActive())
                btnEditActive.visible(item.isActive())

                when (mechanismTypeClass) {
                    MechanismTypeClass.SIMPLE -> {
                        taskTimerHelper.clear()
                        vCombinedFooter.visible(false)
                        tvTimer.visible(false)
                        groupUnloading.visible(false)
                        groupTech.visible(true)
                    }
                    MechanismTypeClass.COMBINED -> {
                        vCombinedFooter.visible(true)
                        tvTimer.visible(true)
                        groupUnloading.visible(true)
                        groupTech.visible(item.isDelegated())

                        if (item.isActive()) {
                            if (taskTimerHelper[item.id] == null) {
                                taskTimerHelper[item.id] = Date().time
                            }
                            val timerTask = Timer().schedule(1000, 1000) {
                                val currentTime = Date().time
                                val dif = currentTime - (taskTimerHelper[item.id] ?: Date().time)

                                binding.tvTimer.post {
                                    binding.tvTimer.text = CommonUtils.convertMsToTime(dif)
                                }
                            }
                            timerTask.run()
                        }
                    }
                }

                btnEditNotActive.setSafeOnClickListener {
                    editClick.invoke(item, item.isActive())
                }

                btnEditActive.setSafeOnClickListener {
                    editClick.invoke(item, item.isActive())
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
