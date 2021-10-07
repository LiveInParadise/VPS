package com.vps.android.presentation.select_task

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.vps.android.MainNavigationDirections
import com.vps.android.interactors.auth.AuthInteractor
import com.vps.android.interactors.mechanism.MechanismInteractor
import com.vps.android.interactors.task.TaskInteractor
import com.vps.android.presentation.base.BaseViewModel
import com.vps.android.presentation.base.NavigationCommand
import com.vps.android.presentation.select_task.feature.SelectTaskEffectHandler
import com.vps.android.presentation.select_task.feature.SelectTaskFeature
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class SelectTaskViewModel(
    private val selectTaskSpec: SelectTaskSpec,
    private val taskInteractor: TaskInteractor,
    private val authInteractor: AuthInteractor,
    private val mechanismInteractor: MechanismInteractor,
) : BaseViewModel() {

    private val feature = SelectTaskFeature()
    val state = feature.state

    protected val _messages: Channel<String> = Channel()
    protected val _events: Channel<SelectTaskFeature.Event> = Channel()
    val events: Flow<SelectTaskFeature.Event> = _events.receiveAsFlow()

    init {
        feature.init(viewModelScope, SelectTaskEffectHandler(taskInteractor, authInteractor, mechanismInteractor, _events, _messages))
        getData()
    }

    private fun getData() {
        when (selectTaskSpec.screenFilter) {
            SelectTaskFragment.SELECT_SCREEN_TYPE -> getTaskTypesList()
            SelectTaskFragment.SELECT_SCREEN_LOADING_PLACE -> getTaskLoadingList()
            SelectTaskFragment.SELECT_SCREEN_UNLOADING_PLACE -> getTaskLoadingList()
            SelectTaskFragment.SELECT_SCREEN_GOOD -> getTaskGoods()
            SelectTaskFragment.SELECT_SCREEN_TECH -> getTaskTech()
        }
    }

    private fun getTaskTypesList() {
        feature.act(SelectTaskFeature.Action.GetTaskTypes)
    }

    private fun getTaskLoadingList() {
        feature.act(SelectTaskFeature.Action.GetTaskLoadingPlaces)
    }

    private fun getTaskGoods() {
        feature.act(SelectTaskFeature.Action.GetTaskGoods)
    }

    private fun getTaskTech() {
        feature.act(SelectTaskFeature.Action.GetTaskTech)
    }

    override fun logout() {
        feature.act(SelectTaskFeature.Action.Logout)
    }

    fun toAuthScreen() {
        val dir = MainNavigationDirections.actionLogout()
        navigate(NavigationCommand.Dir(dir))
    }

    fun onSave(outState: Bundle) {
        feature.save(outState)
    }

    fun onRestore(savedInstanceState: Bundle?) {
        feature.restore(savedInstanceState)
    }

}
