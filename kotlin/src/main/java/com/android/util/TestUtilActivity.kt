package com.android.util

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.http.AATest.ApiService
import com.android.frame.http.AATest.UrlConstant
import com.android.frame.http.AATest.WangYiNewsWebviewActivity
import com.android.frame.http.AATest.bean.NewsListBean
import com.android.frame.http.ExceptionUtil
import com.android.frame.http.RetrofitFactory
import com.android.frame.http.SchedulerUtil
import com.android.frame.mvc.BaseActivity
import com.android.util.StatusBar.TestStatusBarUtilActivity
import com.android.util.regex.RegexUtil
import com.bumptech.glide.Glide
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_common_layout.*
import java.util.*
import kotlin.concurrent.thread
import kotlin.random.Random

/**
 * Created by xuzhb on 2019/9/22
 * Desc:测试工具类方法
 */
class TestUtilActivity : BaseActivity<ActivityCommonLayoutBinding>() {

    companion object {
        const val TEST_STATUS_BAR = "TEST_STATUS_BAR"
        const val TEST_DATE = "TEST_DATE"
        const val TEST_KEYBOARD = "TEST_KEYBOARD"
        const val TEST_DRAWABLE = "TEST_DRAWABLE"
        const val TEST_SPUTIL = "TEST_SPUTIL"
        const val TEST_STRING = "TEST_STRING"
        const val TEST_NOTIFICATION = "TEST_NOTIFICATION"
        const val TEST_CONTINUOUS_CLICK = "TEST_CONTINUOUS_CLICK"
        const val TEST_PINYIN = "TEST_PINYIN"
        const val TEST_LAYOUT_PARAMS = "TEST_LAYOUT_PARAMS"
        const val TEST_REGEX = "TEST_REGEX"
    }

    override fun handleView(savedInstanceState: Bundle?) {
        when (intent.getStringExtra(MODULE_NAME)) {
            TEST_STATUS_BAR -> testStatusBarUtil();
            TEST_DATE -> testDateUtil()
            TEST_KEYBOARD -> testKeyBoardUtil()
            TEST_DRAWABLE -> testDrawableUtil()
            TEST_SPUTIL -> testSPUtil()
            TEST_STRING -> testStringUtil()
            TEST_NOTIFICATION -> testNotification()
            TEST_CONTINUOUS_CLICK -> testContinuousClick()
            TEST_PINYIN -> testPinyin()
            TEST_LAYOUT_PARAMS -> testLayoutParams()
            TEST_REGEX -> testRegex()
        }
    }

    override fun initListener() {}

    override fun getViewBinding() = ActivityCommonLayoutBinding.inflate(layoutInflater)

    private fun testStatusBarUtil() {
        val text1 = "沉浸背景图片"
        val text2 = "状态栏白色，字体和图片黑色"
        val text3 = "状态栏黑色，字体和图片白色"
        val text4 = "状态栏黑色半透明，字体和图片白色"
        val text5 = "隐藏导航栏"
        val text6 = "导航栏和状态栏透明"
        val text7 = "透明度和十六进制对应表"
        initCommonLayout(this, "实现沉浸式状态栏", text1, text2, text3, text4, text5, text6, text7)
        btn1.setOnClickListener {
            jumpToTestStatusBarActivity(1, text1)
        }
        btn2.setOnClickListener {
            jumpToTestStatusBarActivity(2, text2)
        }
        btn3.setOnClickListener {
            jumpToTestStatusBarActivity(3, text3)
        }
        btn4.setOnClickListener {
            jumpToTestStatusBarActivity(4, text4)
        }
        btn5.setOnClickListener {
            jumpToTestStatusBarActivity(5, text5)
        }
        btn6.setOnClickListener {
            jumpToTestStatusBarActivity(6, text6)
        }
        btn7.setOnClickListener {
            val content =
                IOUtil.readInputStreameToString(resources.openRawResource(R.raw.hex_alpha_table))
            alert(this, content ?: "")
        }
    }

    private fun jumpToTestStatusBarActivity(type: Int, text: String) {
        val intent = Intent(this, TestStatusBarUtilActivity::class.java)
        intent.putExtra(TestStatusBarUtilActivity.EXTRA_TYPE, type)
        intent.putExtra(TestStatusBarUtilActivity.EXTRA_TEXT, text)
        startActivity(intent)
    }

    private fun testDateUtil() {
        initCommonLayout(this, "测试时间工具", false, true)
        val sb = StringBuilder()
        //测试getCurrentDateTime
        sb.append("当前时间\n").append(DateUtil.getCurrentDateTime())
            .append("\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D_H_M_S_S))
        //测试date2String和string2Date
        sb.append("\n\nDate <--> String\n")
            .append(DateUtil.date2String(Date(), DateUtil.Y_M_D_H_M_S))
            .append("\n")
            .append(
                DateUtil.date2String(
                    DateUtil.string2Date("20200202", DateUtil.YMD)!!,
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
        //测试long2String和string2Long
        sb.append("\n\nLong <--> String\n")
            .append(DateUtil.long2String(System.currentTimeMillis(), DateUtil.Y_M_D_H_M_S_S))
            .append("\n")
            .append(
                DateUtil.long2String(
                    DateUtil.string2Long("20200202", DateUtil.YMD)!!,
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
        //测试date2Long和long2Date
        val dateLong = Date()
        sb.append("\n\nDate <--> Long\n").append(DateUtil.date2Long(dateLong))
            .append("\n").append(DateUtil.date2Long(DateUtil.long2Date(dateLong.time)!!))
        //测试isLeapYear
        sb.append("\n\n是否是闰年：\n").append("2000：").append(DateUtil.isLeapYear(2000))
            .append("\n").append("2012：").append(DateUtil.isLeapYear(2012))
            .append("\n").append("2019：").append(DateUtil.isLeapYear(2019))
        //测试convertOtherFormat
        val convertTime = DateUtil.getCurrentDateTime()
        sb.append("\n\n转换时间格式\n").append(convertTime).append(" -> ")
            .append(
                DateUtil.convertOtherFormat(
                    convertTime,
                    DateUtil.Y_M_D_H_M_S,
                    "yyyyMMddHHmmssSSS"
                )
            )
        //测试compareDate
        val compareTime1 = "2019-02-28"
        val compareTime2 = "2019-02-28"
        val compareTime3 = "2019-02-27"
        val compareTime4 = "2019-03-01"
        sb.append("\n\n比较日期大小\n")
            .append(compareTime1).append(" ").append(compareTime2).append("：")
            .append(DateUtil.compareDate(compareTime1, compareTime2, DateUtil.Y_M_D)).append("\n")
            .append(compareTime1).append(" ").append(compareTime3).append("：")
            .append(DateUtil.compareDate(compareTime1, compareTime3, DateUtil.Y_M_D)).append("\n")
            .append(compareTime1).append(" ").append(compareTime4).append("：")
            .append(DateUtil.compareDate(compareTime1, compareTime4, DateUtil.Y_M_D))
        //测试getDistanceDateByYear
        val distanceDate = "2020-02-02 12:11:03.123"
        sb.append("\n\nN年前/后的日期\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D))
            .append("\n1年前：").append(DateUtil.getDistanceDateByYear(-1, DateUtil.Y_M_D))
            .append("\n2年后：").append(DateUtil.getDistanceDateByYear(2, DateUtil.Y_M_D))
            .append("\n").append(distanceDate)
            .append("\n3年前：")
            .append(DateUtil.getDistanceDateByYear(-3, DateUtil.Y_M_D_H_M_S_S, distanceDate))
            .append("\n4年后：")
            .append(DateUtil.getDistanceDateByYear(4, DateUtil.Y_M_D_H_M_S_S, distanceDate))
        //测试getDistanceDateByMonth
        sb.append("\n\nN月前/后的日期\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D))
            .append("\n1月前：").append(DateUtil.getDistanceDateByMonth(-1, DateUtil.Y_M_D))
            .append("\n2月后：").append(DateUtil.getDistanceDateByMonth(2, DateUtil.Y_M_D))
            .append("\n").append(distanceDate)
            .append("\n3月前：")
            .append(DateUtil.getDistanceDateByMonth(-3, DateUtil.Y_M_D_H_M_S_S, distanceDate))
            .append("\n4月后：")
            .append(DateUtil.getDistanceDateByMonth(4, DateUtil.Y_M_D_H_M_S_S, distanceDate))
        //测试getDistanceDateByWeek
        sb.append("\n\nN周前/后的日期\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D))
            .append("\n1周前：").append(DateUtil.getDistanceDateByWeek(-1, DateUtil.Y_M_D))
            .append("\n2周后：").append(DateUtil.getDistanceDateByWeek(2, DateUtil.Y_M_D))
            .append("\n").append(distanceDate)
            .append("\n3周前：")
            .append(DateUtil.getDistanceDateByWeek(-3, DateUtil.Y_M_D_H_M_S_S, distanceDate))
            .append("\n4周后：")
            .append(DateUtil.getDistanceDateByWeek(4, DateUtil.Y_M_D_H_M_S_S, distanceDate))
        //测试getDistanceDateByDay
        sb.append("\n\nN天前/后的日期\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D))
            .append("\n1天前：").append(DateUtil.getDistanceDateByDay(-1, DateUtil.Y_M_D))
            .append("\n2天后：").append(DateUtil.getDistanceDateByDay(2, DateUtil.Y_M_D))
            .append("\n").append(distanceDate)
            .append("\n3天前：")
            .append(DateUtil.getDistanceDateByDay(-3, DateUtil.Y_M_D_H_M_S_S, distanceDate))
            .append("\n4天后：")
            .append(DateUtil.getDistanceDateByDay(4, DateUtil.Y_M_D_H_M_S_S, distanceDate))
        //测试getDistanceDay
        val distanceDay1 = "2020-02-02"
        val distanceDay2 = "2020-01-31"
        sb.append("\n\n间隔天数\n").append(distanceDay1).append(" ").append(distanceDay2).append("：")
            .append(DateUtil.getDistanceDay(distanceDay1, distanceDay2, DateUtil.Y_M_D))
        //测试getDistanceSecond
        val distanceSecond1 = "12:11:56.786"
        val distanceSecond2 = "12:12:03.123"
        sb.append("\n\n间隔秒数\n").append(distanceSecond1).append(" ").append(distanceSecond2)
            .append("：")
            .append(DateUtil.getDistanceSecond(distanceSecond1, distanceSecond2, DateUtil.H_M_S_S))
        //测试isInThePeriod
        val startDate = "2020-02-02 12:23:45.004"
        val endDate = "2020-12-02 12:23:45.004"
        val dateTimeInThePeriod1 = "2020-02-02 12:23:45.002"
        val dateTimeInThePeriod2 = "2020-12-02 12:23:46.002"
        sb.append("\n\n是否在某个时间段\n").append("[").append(startDate).append(" - ").append(endDate)
            .append("]\n")
            .append(DateUtil.getCurrentDateTime()).append("：")
            .append(DateUtil.isInThePeriod(startDate, endDate, DateUtil.Y_M_D_H_M_S))
            .append("\n").append(dateTimeInThePeriod1).append("：")
            .append(
                DateUtil.isInThePeriod(
                    startDate,
                    endDate,
                    DateUtil.Y_M_D_H_M_S,
                    dateTimeInThePeriod1
                )
            )
            .append("\n").append(dateTimeInThePeriod2).append("：")
            .append(
                DateUtil.isInThePeriod(
                    startDate,
                    endDate,
                    DateUtil.Y_M_D_H_M_S,
                    dateTimeInThePeriod2
                )
            )
        //测试getStartTimeOfToday和getStartTimeOfDay
        val startTime = "2020-02-02"
        sb.append("\n\n某天零点时间\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D)).append("：")
            .append(DateUtil.long2String(DateUtil.getStartTimeOfToday(), DateUtil.Y_M_D_H_M_S_S))
            .append("\n").append(startTime).append("：")
            .append(
                DateUtil.long2String(
                    DateUtil.getStartTimeOfDay(startTime, DateUtil.Y_M_D),
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
        //测试getStartTimeOfCurrentMonth和getStartTimeOfMonth
        sb.append("\n\n某月零点时间\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M)).append("：")
            .append(
                DateUtil.long2String(
                    DateUtil.getStartTimeOfCurrentMonth(),
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
            .append("\n")
            .append(DateUtil.convertOtherFormat(startTime, DateUtil.Y_M_D, DateUtil.Y_M))
            .append("：")
            .append(
                DateUtil.long2String(
                    DateUtil.getStartTimeOfMonth(startTime, DateUtil.Y_M_D),
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
        //测试getEndTimeOfToday和getEndTimeOfDay
        val endTime = "2020-02-02"
        sb.append("\n\n某天最后时间\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D)).append("：")
            .append(DateUtil.long2String(DateUtil.getEndTimeOfToday(), DateUtil.Y_M_D_H_M_S_S))
            .append("\n").append(endTime).append("：")
            .append(
                DateUtil.long2String(
                    DateUtil.getEndTimeOfDay(startTime, DateUtil.Y_M_D),
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
        //测试getEndTimeOfCurrentMonth和getEndTimeOfMonth
        sb.append("\n\n某月最后时间\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M)).append("：")
            .append(
                DateUtil.long2String(
                    DateUtil.getEndTimeOfCurrentMonth(),
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
            .append("\n")
            .append(DateUtil.convertOtherFormat(startTime, DateUtil.Y_M_D, DateUtil.Y_M))
            .append("：")
            .append(
                DateUtil.long2String(
                    DateUtil.getEndTimeOfMonth(startTime, DateUtil.Y_M_D),
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
        //测试getCurrentDayOfWeekCH和getDayOfWeekCH
        val weekTime = "2020-02-02"
        sb.append("\n\n判断周几\n").append(DateUtil.getCurrentDateTime()).append("：")
            .append(DateUtil.getCurrentDayOfWeekCH()).append("\n").append(weekTime).append("：")
            .append(DateUtil.getDayOfWeekCH(weekTime, DateUtil.Y_M_D))
        tv.text = sb.toString()
    }

    private fun testKeyBoardUtil() {
        initCommonLayout(this, "测试键盘工具", "弹出软键盘", "收起软键盘", "复制到剪切板")
        btn1.setOnClickListener {
            KeyboardUtil.showSoftInput(this, it)
        }
        btn2.setOnClickListener {
            KeyboardUtil.hideSoftInput(this, it)
        }
        btn3.setOnClickListener {
            KeyboardUtil.copyToClipboard(this, "https://www.baidu.com")
        }
    }

    private fun testDrawableUtil() {
        initCommonLayout(this, "代码创建Drawable", "按钮", "solid", "stroke", "虚线stroke", "solid和stroke")
        btn2.setOnClickListener {
            val drawable = DrawableUtil.createSolidShape(
                SizeUtil.dp2px(10f),
                resources.getColor(R.color.orange)
            )
            btn1.setBackground(drawable)
            btn1.setTextColor(resources.getColor(R.color.white))
        }
        btn3.setOnClickListener {
            val drawable = DrawableUtil.createStrokeShape(
                SizeUtil.dp2px(15f),
                SizeUtil.dp2px(1f).toInt(),
                resources.getColor(R.color.orange)
            )
            btn1.setBackground(drawable)
            btn1.setTextColor(resources.getColor(R.color.orange))
        }
        btn4.setOnClickListener {
            val drawable = DrawableUtil.createStrokeShape(
                SizeUtil.dp2px(15f),
                SizeUtil.dp2px(1f).toInt(),
                resources.getColor(R.color.orange),
                SizeUtil.dp2px(2f),
                SizeUtil.dp2px(2f)
            )
            btn1.setBackground(drawable)
            btn1.setTextColor(resources.getColor(R.color.orange))
        }
        btn5.setOnClickListener {
            val drawable = DrawableUtil.createSolidStrokeShape(
                SizeUtil.dp2px(25f),
                resources.getColor(R.color.white),
                SizeUtil.dp2px(5f).toInt(),
                resources.getColor(R.color.orange)
            )
            btn1.setBackground(drawable)
            btn1.setTextColor(resources.getColor(R.color.orange))
        }
    }

    private fun testSPUtil() {
        initCommonLayout(
            this,
            "SharePreferences工具类",
            "保存",
            "读取",
            showInputLayout = true,
            showTextView = true
        )
        il.inputTextHint = "请输入名字"
        var name by SPUtil(applicationContext, "default", "name", "")
        btn1.setOnClickListener {
            name = il.inputText.trim()
            il.inputText = ""
            KeyboardUtil.hideSoftInput(this, it)
        }
        btn2.setOnClickListener {
            tv.text = name
        }
    }

    private fun testStringUtil() {
        initCommonLayout(this, "字符串工具类", "显示不同颜色", "带下划线", "带点击事件", showTextView = true)
        val content = "欢迎拨打热线电话"
        tv.setTextColor(Color.BLACK)
        tv.setTextSize(15f)
        btn1.setOnClickListener {
            tv.text = StringUtil.createTextSpan(
                content, 4, 8,
                resources.getColor(R.color.orange),
                SizeUtil.sp2px(18f).toInt(),
                Typeface.ITALIC
            )
        }
        btn2.setOnClickListener {
            tv.text = StringUtil.createTextSpan(
                content, 4, 8,
                resources.getColor(R.color.orange),
                SizeUtil.sp2px(18f).toInt(),
                Typeface.NORMAL,
                true
            )
        }
        btn3.setOnClickListener {
            tv.movementMethod = LinkMovementMethod.getInstance()  //必须设置这个点击事件才能生效
            tv.text = StringUtil.createTextSpan(
                content, 4, 8,
                resources.getColor(R.color.orange),
                SizeUtil.sp2px(18f).toInt(),
                Typeface.BOLD_ITALIC,
                true
            ) { showToast("热线电话：10086") }
        }
    }

    private fun testNotification() {
        tv.text = intent.getStringExtra("content")
        val titleIl = createInputLayout(this, "请输入标题")
        ll.addView(titleIl, 0)
        il.inputTextHint = "请输入内容"
        initCommonLayout(
            this, "通知管理", "自定义通知", "自定义通知(带跳转)", "自定义通知(最全使用示例)",
            "新闻通知", "通知是否打开", "跳转通知设置界面", showInputLayout = true, showTextView = true
        )
        btn1.setOnClickListener {
            var title = titleIl.inputText.trim()
            if (TextUtils.isEmpty(title)) {
                title = "这是标题"
            }
            var content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                content = "这是内容"
            }
            NotificationUtil.showNotification(applicationContext, title, content)
        }
        btn2.setOnClickListener {
            var title = titleIl.inputText.trim()
            if (TextUtils.isEmpty(title)) {
                title = "这是标题"
            }
            var content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                content = "跳转到通知管理页面"
            }
            val intent = Intent()
            intent.setClass(this, TestUtilActivity::class.java)
            intent.putExtra(MODULE_NAME, TEST_NOTIFICATION)
            intent.putExtra("content", content)
            NotificationUtil.showNotification(
                applicationContext,
                title,
                content,
                intent = intent
            )
            finish()
        }
        btn3.setOnClickListener {
            var title = titleIl.inputText.trim()
            if (TextUtils.isEmpty(title)) {
                title = "这是标题"
            }
            var content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                content = "这是内容"
            }
            NotificationUtil.showNotificationFullUse(this, title, content)
        }
        btn4.setOnClickListener {
            requestNews()
        }
        btn5.setOnClickListener {
            tv.text =
                if (NotificationUtil.isNotificationEnabled(applicationContext)) "通知权限已打开" else "通知权限被关闭"
        }
        btn6.setOnClickListener {
            NotificationUtil.gotoNotificationSetting(this)
        }
    }

    private fun requestNews() {
        val count = 100
        RetrofitFactory.instance.createService(ApiService::class.java, UrlConstant.NEWS_URL)
            .getWangYiNewsByBody(1, count)
            .compose(SchedulerUtil.ioToMain())
            .subscribe(object : Observer<NewsListBean> {

                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(bean: NewsListBean) {
                    if (bean.isSuccess()) {
                        if (bean.result != null && bean.result.size > 0) {
                            val result = bean.result.get(Random.nextInt(count))
                            val title = "网易新闻"
                            val content = result.title
                            val target =
                                Glide.with(this@TestUtilActivity).asBitmap().load(result.image)
                                    .submit()
                            try {
                                thread(start = true) {
                                    val bitmap = target.get()
                                    val intent = Intent(
                                        this@TestUtilActivity,
                                        WangYiNewsWebviewActivity::class.java
                                    )
                                    with(intent) {
                                        putExtra("EXTRA_TITLE", title)
                                        putExtra("EXTRA_URL", result.path)
                                    }
                                    NotificationUtil.showNotification(
                                        this@TestUtilActivity,
                                        title,
                                        content,
                                        largeIconBitmap = bitmap,
                                        showBigText = true,
                                        intent = intent
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                showToast("获取图片失败，${e.message}")
                            }
                        } else {
                            showToast("获取新闻失败！")
                        }
                    } else {
                        showToast(bean.message)
                    }
                }

                override fun onError(e: Throwable) {
                    showToast(ExceptionUtil.convertExceptopn(e))
                    e.printStackTrace()
                }

            })
    }

    private var mLastTime = 0L

    //连续点击事件监听
    private fun testContinuousClick() {
        initCommonLayout(this, "连续点击", "连续点击", "连续点击(点击最大时间间隔2秒)", showTextView = true)
        tv.gravity = Gravity.CENTER
        btn1.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?, clickCount: Int) {
                val currentTime = System.currentTimeMillis()
                val sb = StringBuilder()
                sb.append("点击次数:").append(clickCount).append("\n距离上次点击的时间间隔:")
                    .append(currentTime - mLastTime).append("ms\n")
                    .append("当前默认最大时间间隔:").append(getClickInterval()).append("ms")
                tv.text = sb.toString()
                mLastTime = currentTime
            }
        })
        btn2.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?, clickCount: Int) {
                val currentTime = System.currentTimeMillis()
                val sb = StringBuilder()
                sb.append("点击次数:").append(clickCount).append("\n距离上次点击的时间间隔:")
                    .append(currentTime - mLastTime).append("ms\n")
                    .append("当前默认最大时间间隔:").append(getClickInterval()).append("ms")
                tv.text = sb.toString()
                mLastTime = currentTime
            }

            override fun getClickInterval(): Int = 2000
        })
    }

    //拼音工具
    private fun testPinyin() {
        initCommonLayout(
            this, "拼音工具", "获取汉字拼音", "获取姓氏拼音",
            showInputLayout = true, showTextView = true
        )
        il.inputText = "测试拼音工具"
        val s = il.inputText.trim()
        val text = "汉字拼音：${PinyinUtil.hanzi2Pinyin(s)}" +
                "\n首字母拼音：${PinyinUtil.getFirstLetter(s)}"
        il.getEditText().setSelection(s.length)
        tv.text = text
        btn1.setOnClickListener {
            val content = il.inputText.trim()
            val split = " "
            val result = "汉字拼音：${PinyinUtil.hanzi2Pinyin(content, split)}" +
                    "\n首字母拼音：${PinyinUtil.getFirstLetter(content, split)}"
            tv.text = result
        }
        btn2.setOnClickListener {
            val name = il.inputText.trim()
            val result = "姓氏的拼音：${PinyinUtil.getSurnamePinyin(name)}" +
                    "\n首字母拼音：${PinyinUtil.getSurnameFirstLetter(name)}"
            tv.text = result
        }
        il.setOnTextClearListener {
            tv.text = ""
        }
    }

    //布局参数工具
    private fun testLayoutParams() {
        initCommonLayout(
            this, "布局参数工具", "设置上下左右Margin为10dp", "设置上下左右Padding为10dp",
            "增加上下左右Margin为5dp", "减少上下左右Margin为5dp",
            "增加上下左右Padding为5dp", "减少上下左右Padding为5dp", "还原"
        )
        LayoutParamsUtil.setMarginTop(btn1, SizeUtil.dp2px(10f).toInt())
        val rootLl = LinearLayout(this)
        val param1 =
            LinearLayout.LayoutParams(SizeUtil.dp2px(300f).toInt(), SizeUtil.dp2px(200f).toInt())
        param1.gravity = Gravity.CENTER_HORIZONTAL
        rootLl.layoutParams = param1
        rootLl.setBackgroundColor(Color.BLACK)
        val targetLl = LinearLayout(this)
        val params2 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        targetLl.layoutParams = params2
        targetLl.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        val dp20 = SizeUtil.dp2px(20f).toInt()
        val dp40 = SizeUtil.dp2px(40f).toInt()
        LayoutParamsUtil.setMargin(targetLl, dp20, dp20, dp20, dp20)
        LayoutParamsUtil.setPadding(targetLl, dp40, dp40, dp40, dp40)
        val view = View(this)
        val param3 =
            LinearLayout.LayoutParams(SizeUtil.dp2px(300f).toInt(), SizeUtil.dp2px(200f).toInt())
        view.layoutParams = param3
        view.setBackgroundColor(Color.WHITE)
        targetLl.addView(view)
        rootLl.addView(targetLl)
        ll.addView(rootLl, 0)
        btn1.setOnClickListener {
            val margin = SizeUtil.dp2px(10f).toInt()
            LayoutParamsUtil.setMarginLeft(targetLl, margin)
            LayoutParamsUtil.setMarginRight(targetLl, margin)
            LayoutParamsUtil.setMarginTop(targetLl, margin)
            LayoutParamsUtil.setMarginBottom(targetLl, margin)
        }
        btn2.setOnClickListener {
            val padding = SizeUtil.dp2px(10f).toInt()
            LayoutParamsUtil.setPaddingLeft(targetLl, padding)
            LayoutParamsUtil.setPaddingRight(targetLl, padding)
            LayoutParamsUtil.setPaddingTop(targetLl, padding)
            LayoutParamsUtil.setPaddingBottom(targetLl, padding)
        }
        btn3.setOnClickListener {
            val margin = SizeUtil.dp2px(5f).toInt()
            LayoutParamsUtil.addMarginLeft(targetLl, margin)
            LayoutParamsUtil.addMarginRight(targetLl, margin)
            LayoutParamsUtil.addMarginTop(targetLl, margin)
            LayoutParamsUtil.addMarginBottom(targetLl, margin)
        }
        btn4.setOnClickListener {
            val margin = -SizeUtil.dp2px(5f).toInt()
            LayoutParamsUtil.addMarginLeft(targetLl, margin)
            LayoutParamsUtil.addMarginRight(targetLl, margin)
            LayoutParamsUtil.addMarginTop(targetLl, margin)
            LayoutParamsUtil.addMarginBottom(targetLl, margin)
        }
        btn5.setOnClickListener {
            val padding = SizeUtil.dp2px(5f).toInt()
            LayoutParamsUtil.addPaddingLeft(targetLl, padding)
            LayoutParamsUtil.addPaddingRight(targetLl, padding)
            LayoutParamsUtil.addPaddingTop(targetLl, padding)
            LayoutParamsUtil.addPaddingBottom(targetLl, padding)
        }
        btn6.setOnClickListener {
            val padding = -SizeUtil.dp2px(5f).toInt()
            LayoutParamsUtil.addPaddingLeft(targetLl, padding)
            LayoutParamsUtil.addPaddingRight(targetLl, padding)
            LayoutParamsUtil.addPaddingTop(targetLl, padding)
            LayoutParamsUtil.addPaddingBottom(targetLl, padding)
        }
        btn7.setOnClickListener {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            targetLl.layoutParams = params
            LayoutParamsUtil.setMargin(targetLl, dp20, dp20, dp20, dp20)
            LayoutParamsUtil.setPadding(targetLl, dp40, dp40, dp40, dp40)
        }
    }

    //正则表达式工具
    private fun testRegex() {
        initCommonLayout(
            this, "正则表达式工具", true, true,
            "匹配数字", "匹配整数", "匹配浮点数", "匹配汉字", "匹配英文字母或数字", "匹配英文字母和数字",
            "匹配手机号", "匹配身份证号", "匹配\"yyyy-MM-dd\"的日期格式", "匹配输入的正则表达式",
            "提取文本中数字", "根据给定的正则表达式提取内容"
        )
        val regexIl = createInputLayout(this, "请输入正则表达式")
        ll.addView(regexIl, 1)
        il.inputTextHint = "请输入要匹配的内容"
        btn1.setOnClickListener {
            setRegexResult(RegexUtil.isDigit(il.inputText))
        }
        btn2.setOnClickListener {
            setRegexResult(RegexUtil.isInteger(il.inputText))
        }
        btn3.setOnClickListener {
            setRegexResult(RegexUtil.isFloat(il.inputText))
        }
        btn4.setOnClickListener {
            setRegexResult(RegexUtil.isChinese(il.inputText))
        }
        btn5.setOnClickListener {
            setRegexResult(RegexUtil.isLetterOrDigit(il.inputText))
        }
        btn6.setOnClickListener {
            setRegexResult(RegexUtil.isLetterAndDigit(il.inputText))
        }
        btn7.setOnClickListener {
            setRegexResult(RegexUtil.isMobile(il.inputText))
        }
        btn8.setOnClickListener {
            setRegexResult(RegexUtil.isIdCard(il.inputText))
        }
        btn9.setOnClickListener {
            setRegexResult(RegexUtil.isyyyyMMdd(il.inputText))
        }
        btn10.setOnClickListener {
            val regex = regexIl.inputText.trim()
            if (TextUtils.isEmpty(regex)) {
                showToast("请先输入正则表达式")
                return@setOnClickListener
            }
            setRegexResult(RegexUtil.isMatch(regex, il.inputText))
        }
        btn11.setOnClickListener {
            val content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                showToast("请先输入要匹配的内容")
                return@setOnClickListener
            }
            tv.text = RegexUtil.extractDigit(content, "|")
        }
        btn12.setOnClickListener {
            val content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                showToast("请先输入要匹配的内容")
                return@setOnClickListener
            }
            val regex = regexIl.inputText.trim()
            if (TextUtils.isEmpty(regex)) {
                showToast("请先输入正则表达式")
                return@setOnClickListener
            }
            tv.text = RegexUtil.extract(regex, content, "|")
        }
    }

    private fun setRegexResult(isCorrect: Boolean) {
        val result = "\"${il.inputText}\"：$isCorrect"
        tv.text = result
    }

}