package com.vps.android.presentation.task

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.vps.android.MainNavigationDirections
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.task.GoodItem
import com.vps.android.domain.task.PlaceItem
import com.vps.android.domain.task.TaskTypeItem
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.task.TaskInteractor
import com.vps.android.presentation.base.BaseViewModel
import com.vps.android.presentation.base.NavigationCommand
import com.vps.android.presentation.select_task.SelectTaskFragment
import com.vps.android.presentation.select_task.SelectTaskSpec
import com.vps.android.presentation.task.feature.AddTaskEffectHandler
import com.vps.android.presentation.task.feature.AddTaskFeature
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class AddTaskViewModel(
    private val spec: AddTaskSpec,
    private val authInteractor: AuthInteractor,
    private val taskInteractor: TaskInteractor,
) : BaseViewModel() {

    private val feature = AddTaskFeature()
    val state = feature.state

    protected val _messages: Channel<String> = Channel()
    protected val _events: Channel<AddTaskFeature.Event> = Channel()
    val events: Flow<AddTaskFeature.Event> = _events.receiveAsFlow()

    init {
        feature.init(viewModelScope, AddTaskEffectHandler(authInteractor, taskInteractor, _events, _messages))
        initData()
    }

    private fun initData() {
        feature.act(AddTaskFeature.Action.InitData(spec))
    }

    override fun logout() {
        feature.act(AddTaskFeature.Action.Logout)
    }

    fun checkAndProcessTask() {
        feature.act(AddTaskFeature.Action.CheckAndProcessTask)
    }

    fun onSelectTaskBundle(bundle: Bundle) {
        val action = bundle[SelectTaskFragment.SELECT_PARAMETER_SCREEN]
        when (action) {
            SelectTaskFragment.SELECT_SCREEN_TYPE -> {
                val data = bundle[SelectTaskFragment.SELECT_ITEM] as TaskTypeItem
                feature.act(AddTaskFeature.Action.SaveTaskType(data))
            }
            SelectTaskFragment.SELECT_SCREEN_LOADING_PLACE -> {
                val data = bundle[SelectTaskFragment.SELECT_ITEM] as PlaceItem
                feature.act(AddTaskFeature.Action.SaveLoadingPlace(data))
            }
            SelectTaskFragment.SELECT_SCREEN_UNLOADING_PLACE -> {
                val data = bundle[SelectTaskFragment.SELECT_ITEM] as PlaceItem
                feature.act(AddTaskFeature.Action.SaveUnloadingPlace(data))
            }
            SelectTaskFragment.SELECT_SCREEN_GOOD -> {
                val data = bundle[SelectTaskFragment.SELECT_ITEM] as GoodItem
                feature.act(AddTaskFeature.Action.SaveGoodItem(data))
            }
            SelectTaskFragment.SELECT_SCREEN_TECH -> {
                val data = bundle[SelectTaskFragment.SELECT_ITEM] as? List<MechanismItem> ?: listOf()
                feature.act(AddTaskFeature.Action.SaveMechanismList(data))
            }
        }
    }

    fun toAuthScreen() {
        val dir = MainNavigationDirections.actionLogout()
        navigate(NavigationCommand.Dir(dir))
    }

    fun openSelectTaskTypeScreen() {
        val spec = SelectTaskSpec(
            screenFilter = SelectTaskFragment.SELECT_SCREEN_TYPE,
            taskTypeItem = state.value.taskType
        )
        val dir = MainNavigationDirections.actionToSelectTask(spec)
        navigate(NavigationCommand.Dir(dir))
    }

    fun openSelectTaskLoadingScreen() {
        val spec = SelectTaskSpec(
            screenFilter = SelectTaskFragment.SELECT_SCREEN_LOADING_PLACE,
            loadingPlace = state.value.loadingPlace
        )
        val dir = MainNavigationDirections.actionToSelectTask(spec)
        navigate(NavigationCommand.Dir(dir))
    }

    fun openSelectTaskUnLoadingScreen() {
        val spec = SelectTaskSpec(
            screenFilter = SelectTaskFragment.SELECT_SCREEN_UNLOADING_PLACE,
            unloadingPlace = state.value.unloadingPlace
        )
        val dir = MainNavigationDirections.actionToSelectTask(spec)
        navigate(NavigationCommand.Dir(dir))
    }

    fun openSelectTaskGoodScreen() {
        val spec = SelectTaskSpec(
            screenFilter = SelectTaskFragment.SELECT_SCREEN_GOOD,
            goodItem = state.value.goodItem
        )
        val dir = MainNavigationDirections.actionToSelectTask(spec)
        navigate(NavigationCommand.Dir(dir))
    }

    fun openSelectTechGoodScreen() {
        val spec = SelectTaskSpec(
            screenFilter = SelectTaskFragment.SELECT_SCREEN_TECH,
            taskTechList = state.value.mechanismItemList
        )
        val dir = MainNavigationDirections.actionToSelectTask(spec)
        navigate(NavigationCommand.Dir(dir))
    }

    fun openMainScreen() {
        val dir = MainNavigationDirections.actionToMain()
        navigate(NavigationCommand.Dir(dir))
    }

    fun onSave(outState: Bundle) {
        feature.save(outState)
    }

    fun onRestore(savedInstanceState: Bundle?) {
        feature.restore(savedInstanceState)
    }

}
