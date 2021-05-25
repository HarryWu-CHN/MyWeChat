package com.example.mywechat.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywechat.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class SignupViewModel @Inject constructor(
        private val registerRepository : RegisterRepository,
) : ViewModel() {
    val liveData = MutableLiveData<Boolean?>(null)
    fun register(username: String , password: String) {
        liveData.value = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = registerRepository.register(username, password)
                liveData.postValue(response.success)
            } catch (e : IOException){
                liveData.postValue(false)
            }
        }
    }

}

//class TranslateViewModel : ViewModel() {
//    /**
//     * 最简单的无任何输入的请求
//     * 通过扩展属性viewModelScope的launch函数开启协程访问网络并且返回
//     */
//    fun requestDailyWord() {
//        viewModelScope.launch {
//            val result = try {
//                // 网络返回成功
//                Result.success(TranslateService.getApi().requestDailyWord())
//            } catch (e: Exception) {
//                // 网络返回失败
//                Result.failure(e)
//            }
//            // 发射数据，之后观察者就会收到数据
//            // 注意这里是主线程，直接用setValue()即可
//            dailyWordLiveData.value = result
//        }
//    }
//
//    /**
//     * 翻译输入LiveData
//     */
//    private val inputLiveData: MutableLiveData<String> = MutableLiveData()
//
//    /**
//     * 翻译结果输出LiveData
//     * 通过LiveData的扩展函数switchMap()实现变换，在下游能够返回支持协程的CoroutineLiveData
//     * CoroutineLiveData是通过Top-Level函数里面的liveData()方法来创建，在这里可以传入闭包，开启协程访问网络并且返回
//     *
//     * 注:
//     * 1. LiveDataScope, ViewModelScope和lifecycleScope会自动处理自身的生命周期，在生命周期结束时会自动取消没有执行完成的协程任务
//     * 2. 其中map和switchMap与RxJava中的map和flatMap有点类似
//     */
//    val translateResult: LiveData<Result<BaseResult<String>>> = inputLiveData.switchMap { input ->
//        liveData {
//            val result = try {
//                // 网络返回成功
//                Result.success(TranslateService.getApi().requestTranslateResult(input))
//            } catch (e: Exception) {
//                // 网络返回失败
//                Result.failure(e)
//            }
//            // 发射数据，之后观察者就会收到数据
//            emit(result)
//        }
//    }
//
//    /**
//     * 开始翻译
//     */
//    fun requestTranslate(input: String) {
//        inputLiveData.value = input
//    }
//
//}