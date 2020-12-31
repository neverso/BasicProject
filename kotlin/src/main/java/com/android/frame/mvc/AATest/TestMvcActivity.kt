package com.android.frame.mvc.AATest

import android.os.Bundle
import com.android.basicproject.databinding.ActivityTestMvcBinding
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.AATest.entity.WeatherBean
import com.android.frame.mvc.AATest.server.ApiHelper
import com.android.frame.mvc.BaseActivity
import com.android.frame.mvc.extra.http.CustomObserver
import com.android.util.JsonUtil
import com.google.gson.Gson
import kotlin.random.Random

/**
 * Created by xuzhb on 2020/12/30
 * Desc:
 */
class TestMvcActivity : BaseActivity<ActivityTestMvcBinding>() {

    private val mCities = mutableListOf(
        "深圳", "福州", "北京", "广州", "上海", "厦门",
        "泉州", "重庆", "天津", "啦啦", "哈哈", "随便"
    )

    override fun handleView(savedInstanceState: Bundle?) {
    }

    override fun initListener() {
    }

    override fun getViewBinding() = ActivityTestMvcBinding.inflate(layoutInflater)

    //在这里处理从服务器加载和显示数据的逻辑，请务必重写refreshData，当加载失败点击重试时会调用这个方法
    override fun refreshData() {
        showWeatherInfo(mCities[Random.nextInt(mCities.size)])
    }

    private fun showWeatherInfo(city: String) {
        ApiHelper.getWeatherByQuery(city)
            .subscribe(object : CustomObserver<BaseResponse<WeatherBean>>(this) {
                override fun onSuccess(response: BaseResponse<WeatherBean>) {
                    binding.tv.text = JsonUtil.formatJson(Gson().toJson(response))
                }
            })
    }

}