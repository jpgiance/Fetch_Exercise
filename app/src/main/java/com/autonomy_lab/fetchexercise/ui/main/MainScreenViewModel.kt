package com.autonomy_lab.fetchexercise.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autonomy_lab.fetchexercise.data.network.FetchApiItem
import com.autonomy_lab.fetchexercise.network.FetchApi
import com.autonomy_lab.fetchexercise.network.InternetConnectionObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val fetchApi: FetchApi,
    internetConnectionObserver: InternetConnectionObserver
): ViewModel() {

    private val TAG = this::class.simpleName


    private var _fetchItemList = MutableStateFlow<List<List<FetchApiItem>>>(emptyList())
    val fetchItemList: StateFlow<List<List<FetchApiItem>>> get() = _fetchItemList

    private var _refreshing = MutableStateFlow<Boolean>(false)
    val refreshing: StateFlow<Boolean> get() = _refreshing

    val isInternetAvailable = internetConnectionObserver.isInternetAvailable.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )


    init {
        getItemListFromNetwork()
    }

    fun getItemListFromNetwork(){
        viewModelScope.launch {
            try {
                _refreshing.value = true
                val response = withContext(Dispatchers.IO){
                    fetchApi.fetchItemList()
                }

                if (response.isSuccessful){

//                    _fetchItemList.value = response.body() ?: emptyList()
                    val popo = response.body() ?: emptyList()

                    _fetchItemList.value = popo
                        .filter { it.listId != null }
                        .filter { !it.name.isNullOrBlank() }
                        .groupBy { it.listId }
                        .toSortedMap(compareBy { it })
                        .values
                        .toList()




                }else{

                    Log.e(TAG, "getItemListFromNetwork: Api response Unsuccessful" )
                }

                _refreshing.value = false

            }catch (exception: IOException){
                Log.e(TAG, "getItemListFromNetwork: Error occurred: ${exception.message}" )
                _refreshing.value = false

            }
        }
    }


}