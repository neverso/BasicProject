package com.android.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.util.StatusBar.TestStatusBarUtilActivity;
import com.android.util.activity.ActivityUtil;
import com.android.util.activity.TestJumpActivity;
import com.android.util.app.AATest.TestAppListActivity;
import com.android.util.app.AppUtil;
import com.android.util.traffic.NetworkStatsHelper;
import com.android.util.traffic.TrafficInfo;
import com.android.util.traffic.TrafficStatsUtil;
import com.android.widget.InputLayout;

import java.util.List;

/**
 * Created by xuzhb on 2019/10/20
 * Desc:测试工具类方法
 */
public class TestUtilActivity extends BaseActivity {

    private static final String TAG = "TestUtilActivity";

    public static final String TEST_STATUS_BAR = "TEST_STATUS_BAR";
    public static final String TEST_DATE = "TEST_DATE";
    public static final String TEST_DRAWABLE = "TEST_DRAWABLE";
    public static final String TEST_KEYBOARD = "TEST_KEYBOARD";
    public static final String TEST_NOTIFICATION = "TEST_NOTIFICATION";
    public static final String TEST_TRAFFICSTATS = "TEST_TRAFFICSTATS";
    public static final String TEST_NETWORK_STATS = "TEST_NETWORK_STATS";
    public static final String TEST_CONTINUOUS_CLICK = "TEST_CONTINUOUS_CLICK";
    public static final String TEST_PINYIN = "TEST_PINYIN";
    public static final String TEST_ACTIVITY = "TEST_ACTIVITY";
    public static final String TEST_APP = "TEST_APP";
    public static final String TEST_DEVICE = "TEST_DEVICE";

    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.il)
    InputLayout il;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.btn4)
    Button btn4;
    @BindView(R.id.btn5)
    Button btn5;
    @BindView(R.id.btn6)
    Button btn6;
    @BindView(R.id.btn7)
    Button btn7;
    @BindView(R.id.btn8)
    Button btn8;
    @BindView(R.id.btn9)
    Button btn9;

    @Override
    public void handleView(Bundle savedInstanceState) {
        switch (getIntent().getStringExtra(CommonLayoutUtil.MODULE_NAME)) {
            case TEST_STATUS_BAR:
                testStatusBarUtil();
                break;
            case TEST_DATE:
                testDateUtil();
                break;
            case TEST_KEYBOARD:
                testKeyBoardUtil();
                break;
            case TEST_DRAWABLE:
                testDrawableUtil();
                break;
            case TEST_NOTIFICATION:
                testNotification();
                break;
            case TEST_TRAFFICSTATS:
                testTrafficStats();
                break;
            case TEST_NETWORK_STATS:
                testNetworkStats();
                break;
            case TEST_CONTINUOUS_CLICK:
                testContinuousClick();
                break;
            case TEST_PINYIN:
                testPinyin();
                break;
            case TEST_ACTIVITY:
                testActivity();
                break;
            case TEST_APP:
                testApp();
                break;
            case TEST_DEVICE:
                testDevice();
                break;
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_layout;
    }

    private void testStatusBarUtil() {
        String text1 = "沉浸背景图片";
        String text2 = "状态栏白色，字体和图片黑色";
        String text3 = "状态栏黑色，字体和图片白色";
        String text4 = "状态栏黑色半透明，字体和图片白色";
        CommonLayoutUtil.initCommonLayout(this, "实现沉浸式状态栏", text1, text2, text3, text4);
        btn1.setOnClickListener(v -> {
            jumpToTestStatusBarActivity(1, text1);
        });
        btn2.setOnClickListener(v -> {
            jumpToTestStatusBarActivity(2, text2);
        });
        btn3.setOnClickListener(v -> {
            jumpToTestStatusBarActivity(3, text3);
        });
        btn4.setOnClickListener(v -> {
            jumpToTestStatusBarActivity(4, text4);
        });
    }

    private void jumpToTestStatusBarActivity(int type, String text) {
        Intent intent = new Intent(this, TestStatusBarUtilActivity.class);
        intent.putExtra(TestStatusBarUtilActivity.EXTRA_TYPE, type);
        intent.putExtra(TestStatusBarUtilActivity.EXTRA_TEXT, text);
        startActivity(intent);
    }

    private void testDateUtil() {
        CommonLayoutUtil.initCommonLayout(this, "测试时间工具", "按钮");
        btn1.setOnClickListener(v -> {
            LogUtil.e(TAG, "当天开始时间:" + DateUtil.long2String(DateUtil.getStartTimeOfToday(), DateUtil.Y_M_D_H_M_S));
            LogUtil.e(TAG, "当天结束时间:" + DateUtil.long2String(DateUtil.getEndTimeOfToday(), DateUtil.Y_M_D_H_M_S));
            String someDay = "2019-12-01";
            LogUtil.e(TAG, someDay + "开始时间:" + DateUtil.long2String(DateUtil.getStartTimeOfDay(someDay, DateUtil.Y_M_D), DateUtil.Y_M_D_H_M_S));
            LogUtil.e(TAG, someDay + "结束时间:" + DateUtil.long2String(DateUtil.getEndTimeOfDay(someDay, DateUtil.Y_M_D), DateUtil.Y_M_D_H_M_S));
            LogUtil.e(TAG, "本月开始时间:" + DateUtil.long2String(DateUtil.getStartTimeOfCurrentMonth(), DateUtil.Y_M_D_H_M_S));
            LogUtil.e(TAG, "本月结束时间:" + DateUtil.long2String(DateUtil.getEndTimeOfCurrentMonth(), DateUtil.Y_M_D_H_M_S));
            String someMonth = "2019-02";
            LogUtil.e(TAG, someMonth + "开始时间:" + DateUtil.long2String(DateUtil.getStartTimeOfMonth(someMonth, DateUtil.Y_M), DateUtil.Y_M_D_H_M_S));
            LogUtil.e(TAG, someMonth + "结束时间:" + DateUtil.long2String(DateUtil.getEndTimeOfMonth(someMonth, DateUtil.Y_M), DateUtil.Y_M_D_H_M_S));
        });
    }

    private void testKeyBoardUtil() {
        CommonLayoutUtil.initCommonLayout(this, "测试键盘工具", "弹出软键盘", "收起软键盘", "复制到剪切板");
        btn1.setOnClickListener(v -> {
            KeyboardUtil.showSoftInput(getApplicationContext(), v);
        });
        btn2.setOnClickListener(v -> {
            KeyboardUtil.hideSoftInput(getApplicationContext(), v);
        });
        btn3.setOnClickListener(v -> {
            KeyboardUtil.copyToClipboard(getApplicationContext(), "https://www.baidu.com");
        });
    }

    private void testDrawableUtil() {
        CommonLayoutUtil.initCommonLayout(this, "代码创建Drawable", "按钮", "solid", "stroke", "虚线stroke", "solid和stroke");
        btn2.setOnClickListener(v -> {
            GradientDrawable drawable = DrawableUtil.createSolidShape(
                    SizeUtil.dp2px(10f),
                    getResources().getColor(R.color.orange)
            );
            btn1.setBackground(drawable);
            btn1.setTextColor(getResources().getColor(R.color.white));
        });
        btn3.setOnClickListener(v -> {
            GradientDrawable drawable = DrawableUtil.createStrokeShape(
                    SizeUtil.dp2px(15),
                    (int) SizeUtil.dp2px(1),
                    getResources().getColor(R.color.orange)
            );
            btn1.setBackground(drawable);
            btn1.setTextColor(getResources().getColor(R.color.orange));
        });
        btn4.setOnClickListener(v -> {
            GradientDrawable drawable = DrawableUtil.createStrokeShape(
                    SizeUtil.dp2px(15),
                    (int) SizeUtil.dp2px(1),
                    getResources().getColor(R.color.orange),
                    SizeUtil.dp2px(2f),
                    SizeUtil.dp2px(2f)
            );
            btn1.setBackground(drawable);
            btn1.setTextColor(getResources().getColor(R.color.orange));
        });
        btn5.setOnClickListener(v -> {
            GradientDrawable drawable = DrawableUtil.createSolidStrokeShape(
                    SizeUtil.dp2px(25),
                    getResources().getColor(R.color.white),
                    (int) SizeUtil.dp2px(2),
                    getResources().getColor(R.color.orange)
            );
            btn1.setBackground(drawable);
            btn1.setTextColor(getResources().getColor(R.color.orange));
        });
    }

    //通知管理
    private void testNotification() {
        String extraCpntent = getIntent().getStringExtra("content");
        tv.setText(extraCpntent);  //传递过来的参数
        InputLayout titleIl = new InputLayout(this);
        titleIl.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) SizeUtil.dp2px(40)));
        titleIl.setInputTextHint("请输入标题");
        titleIl.setInputTextType(InputType.TYPE_CLASS_TEXT);
        titleIl.setInputTextSize(SizeUtil.sp2px(15));
        titleIl.setInputTextColor(Color.BLACK);
        titleIl.setInputTextColorHint(Color.parseColor("#E6E6E6"));
        titleIl.setShowBottomLine(true);
        ll.addView(titleIl, 0);
        il.setInputTextHint("请输入内容");
        CommonLayoutUtil.initCommonLayout(this, "通知管理", true, true,
                "自定义通知", "自定义通知（带跳转）", "通知是否打开", "跳转通知设置界面");
        btn1.setOnClickListener(v -> {
            String title = titleIl.getInputText().trim();
            if (TextUtils.isEmpty(title)) {
                title = "这是标题";
            }
            String content = il.getInputText().trim();
            if (TextUtils.isEmpty(content)) {
                content = "这是内容";
            }
            NotificationUtil.showNotification(getApplicationContext(), title, content);
        });
        btn2.setOnClickListener(v -> {
            String title = titleIl.getInputText().trim();
            if (TextUtils.isEmpty(title)) {
                title = "这是标题";
            }
            String content = il.getInputText().trim();
            if (TextUtils.isEmpty(content)) {
                content = "跳转到通知管理页面";
            }
            Intent intent = new Intent(TestUtilActivity.this, TestUtilActivity.class);
            intent.putExtra(CommonLayoutUtil.MODULE_NAME, TEST_NOTIFICATION);
            intent.putExtra("content", content);
            NotificationUtil.showNotificationWithIntent(TestUtilActivity.this, intent, title, content);
            finish();
        });
        btn3.setOnClickListener(v -> {
            boolean isOpen = NotificationUtil.isNotificationEnabled(getApplicationContext());
            tv.setText(isOpen ? "通知权限已打开" : "通知权限被关闭");
        });
        btn4.setOnClickListener(v -> {
            NotificationUtil.gotoNotificationSetting(getApplicationContext());
        });
    }

    //流量工具
    private void testTrafficStats() {
        CommonLayoutUtil.initCommonLayout(this, "TrafficStats", false, true, "流量统计", "已安装应用流量统计", "已安装应用流量排行");
        String spacing = "=================================================================================\n";
        btn1.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append("开机以来(B)\t\t上传\t\t下载\t\t总和\n");
            sb.append("总流量\t\t" + format(TrafficStats.getTotalRxBytes()) + "\t\t" + format(TrafficStats.getTotalTxBytes()) +
                    "\t\t" + format(TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes()) + "\n");
            sb.append("总数据包\t\t" + TrafficStats.getTotalRxPackets() + "\t\t" + TrafficStats.getTotalTxPackets() +
                    "\t\t" + (TrafficStats.getTotalRxPackets() + TrafficStats.getTotalTxPackets()) + "\n");
            sb.append("Mobile流量\t\t" + format(TrafficStats.getMobileRxBytes()) + "\t\t" + format(TrafficStats.getMobileTxBytes()) +
                    "\t\t" + format(TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes()) + "\n");
            sb.append("Mobile数据包\t\t" + TrafficStats.getMobileRxPackets() + "\t\t" + TrafficStats.getMobileTxPackets() +
                    "\t\t" + (TrafficStats.getMobileRxPackets() + TrafficStats.getMobileTxPackets()) + "\n");
            sb.append("本应用流量\t\t" + TrafficStatsUtil.getLocalAppTotalBytes(this, ByteUnit.B) + "\t" +
                    TrafficStatsUtil.getLocalAppTotalBytes(this, ByteUnit.KB) + "\t" +
                    SystemUtil.getUid(this, getPackageName()) + "\n");
            LogUtil.i("流量统计", " \n" + spacing + sb + spacing);
        });
        btn2.setOnClickListener(v -> {
            new Thread(() -> {
                List<TrafficInfo> list = TrafficStatsUtil.getAppTotalBytesList(this, ByteUnit.MB);
                StringBuilder sb = new StringBuilder();
                for (TrafficInfo info : list) {
                    sb.append(info + "\n");
                }
                LogUtil.logLongTag("流量列表(MB)", " \n" + spacing + sb + spacing);
            }).start();
        });
        btn3.setOnClickListener(v -> {
            new Thread(() -> {
                List<TrafficInfo> list = TrafficStatsUtil.getAppTotalBytesSortList(this, ByteUnit.MB, 5);
                StringBuilder sb = new StringBuilder();
                for (TrafficInfo info : list) {
                    sb.append(info + "\n");
                }
                LogUtil.logLongTag("流量排行(MB)", " \n" + spacing + sb + spacing);
            }).start();
        });
    }

    @SuppressLint("DefaultLocale")
    private String format(long value) {
        return String.format("%.2f", (value / 1024f / 1024f));
    }

    //流量统计
    private void testNetworkStats() {
        CommonLayoutUtil.initCommonLayout(this, "NetworkStatsManager", false, true,
                "流量统计", "已安装应用流量统计", "已安装应用流量排行", "是否有权限", "跳转权限申请页面");
        String spacing = "=================================================================================\n";
        btn1.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append("Wifi流量\t" + NetworkStatsHelper.getInstance().getWifiTotalBytes(ByteUnit.MB) + "\n");
            sb.append("Mobile流量\t" + NetworkStatsHelper.getInstance().getMobileTotalBytes(ByteUnit.MB) + "\n");
            int uid = SystemUtil.getUid(this, getPackageName());
            double wifiBytes = NetworkStatsHelper.getInstance().getAppWifiTotalBytes(0, uid, ByteUnit.B);
            double mobileBytes = NetworkStatsHelper.getInstance().getAppMobileTotalBytes(0, uid, ByteUnit.B);
            sb.append("本应用Wifi流量\t" + wifiBytes + "\n");
            sb.append("本应用Moile流量\t" + mobileBytes + "\n");
            LogUtil.i("流量统计", "  \n" + spacing + sb + spacing);
        });
        btn2.setOnClickListener(v -> {
            new Thread(() -> {
                showToast("正在获取...");
                List<TrafficInfo> list = NetworkStatsHelper.getInstance().getAppTotalBytesList(ConnectivityManager.TYPE_MOBILE
                        , 0, System.currentTimeMillis(), ByteUnit.MB);
                StringBuilder sb = new StringBuilder();
                for (TrafficInfo info : list) {
                    sb.append(info + "\n");
                }
                LogUtil.logLongTag("流量列表(MB)", " \n" + spacing + sb + spacing);
                showToast("获取成功，请查看日志");
            }).start();
        });
        btn3.setOnClickListener(v -> {
            new Thread(() -> {
                showToast("正在获取...");
                StringBuilder sb = new StringBuilder();

                sb.append("总流量排行前10(MB)\n");
                List<TrafficInfo> totalList = NetworkStatsHelper.getInstance().getAppMobileTotalBytesSortList(0, ByteUnit.MB, 10);
                for (TrafficInfo info : totalList) {
                    sb.append(info + "\n");
                }

                sb.append("\n\n本月流量排行前10(MB)\n");
                List<TrafficInfo> monthList = NetworkStatsHelper.getInstance().getAppMobileTotalBytesSortList(DateUtil.getStartTimeOfCurrentMonth(), ByteUnit.MB, 10);
                for (TrafficInfo info : monthList) {
                    sb.append(info + "\n");
                }

                sb.append("\n\n今日流量排行前10(MB)\n");
                List<TrafficInfo> dayList = NetworkStatsHelper.getInstance().getAppMobileTotalBytesSortList(DateUtil.getStartTimeOfToday(), ByteUnit.MB, 10);
                for (TrafficInfo info : dayList) {
                    sb.append(info + "\n");
                }

                sb.append("\n\n昨日流量排行前10(MB)\n");
                String formatStr = DateUtil.Y_M_D_H_M_S;
                String yesterdayStr = DateUtil.getDistanceDateByDay(-1, formatStr);
                List<TrafficInfo> yesterdayList = NetworkStatsHelper.getInstance().getAppTotalBytesSortList(ConnectivityManager.TYPE_MOBILE,
                        DateUtil.getStartTimeOfDay(yesterdayStr, formatStr), DateUtil.getEndTimeOfDay(yesterdayStr, formatStr), ByteUnit.MB, 10);
                for (TrafficInfo info : yesterdayList) {
                    sb.append(info + "\n");
                }

                LogUtil.logLongTag("流量列表(MB)", " \n" + spacing + sb + spacing);
                showToast("获取成功，请查看日志");
            }).start();
        });
        btn4.setOnClickListener(v -> {
            if (NetworkStatsHelper.hasPermissionToReadNetworkStats(this)) {
                showToast("已申请权限");
            } else {
                showToast("无权限");
            }
        });
        btn5.setOnClickListener(v -> {
            NetworkStatsHelper.requestReadNetworkStats();
        });
    }


    private long mLastClickTime = 0;

    //连续点击事件监听
    private void testContinuousClick() {
        CommonLayoutUtil.initCommonLayout(this, "连续点击", false, true,
                "连续点击", "连续点击(点击最大时间间隔2秒)");
        tv.setGravity(Gravity.CENTER);
        btn1.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v, int clickCount) {
                long currentClickTime = System.currentTimeMillis();
                StringBuilder sb = new StringBuilder();
                sb.append("点击次数:").append(clickCount).append("\n距离上次点击的时间间隔:")
                        .append(currentClickTime - mLastClickTime).append("ms\n")
                        .append("当前默认最大时间间隔:").append(getClickInterval()).append("ms");
                tv.setText(sb.toString());
                mLastClickTime = currentClickTime;
            }
        });
        btn2.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v, int clickCount) {
                long currentClickTime = System.currentTimeMillis();
                StringBuilder sb = new StringBuilder();
                sb.append("点击次数:").append(clickCount).append("\n距离上次点击的时间间隔:")
                        .append(currentClickTime - mLastClickTime).append("ms\n")
                        .append("当前默认最大时间间隔:").append(getClickInterval()).append("ms");
                tv.setText(sb.toString());
                mLastClickTime = currentClickTime;
            }

            @Override
            protected int getClickInterval() {
                return 2000;
            }
        });
    }

    //拼音工具
    private void testPinyin() {
        CommonLayoutUtil.initCommonLayout(this, "拼音工具", true, true,
                "获取汉字拼音", "获取姓氏拼音");
        il.setInputText("测试拼音工具");
        String s = il.getInputText().trim();
        String text = "汉字拼音：" + PinyinUtil.hanzi2Pinyin(s) +
                "\n首字母拼音：" + PinyinUtil.getFirstLetter(s);
        il.getEditText().setSelection(s.length());
        tv.setText(text);
        btn1.setOnClickListener(v -> {
            String content = il.getInputText().trim();
            String split = " ";
            String result = "汉字拼音：" + PinyinUtil.hanzi2Pinyin(content, split) +
                    "\n首字母拼音：" + PinyinUtil.getFirstLetter(content, split);
            tv.setText(result);
        });
        btn2.setOnClickListener(v -> {
            String name = il.getInputText().trim();
            String result = "姓氏的拼音：" + PinyinUtil.getSurnamePinyin(name) +
                    "\n首字母拼音：" + PinyinUtil.getSurnameFirstLetter(name);
            tv.setText(result);
        });
        il.setOnTextClearListener(() -> {
            tv.setText("");
        });
    }

    //Activity工具
    private void testActivity() {
        CommonLayoutUtil.initCommonLayout(this, "Activity工具", false, true,
                "跳转到东方财富", "跳转Activity(带动画)", "跳转Activity(携带参数)");
        StringBuilder sb = new StringBuilder();
        sb.append("MainActivity是否存在：")
                .append(ActivityUtil.isActivityExists(this, "com.android.java",
                        "com.android.base.MainActivity"))
                .append("\n启动Activity名：")
                .append(ActivityUtil.getLauncherActivityName(this, getPackageName()));
        String[] topActivity = ActivityUtil.getTopActivityName(this).split(" ");
        sb.append("\n\n栈顶Activity信息：\n")
                .append(topActivity[0]).append("(包名)\n")
                .append(topActivity[1]).append("(类名)");
        tv.setText(sb.toString());
        btn1.setOnClickListener(v -> {
            /*
             * 哔哩哔哩：tv.danmaku.bili，tv.danmaku.bili.ui.splash.SplashActivity
             * 微博：com.sina.weibo，com.sina.weibo.SplashActivity
             * 东方财富：com.eastmoney.android.berlin，com.eastmoney.android.berlin.activity.MainActivity
             * 铁路12306：com.MobileTicket，com.alipay.mobile.quinox.LauncherActivity
             * 微信：com.tencent.mm，com.tencent.mm.ui.LauncherUI
             * 支付宝：com.eg.android.AlipayGphone，com.eg.android.AlipayGphone.AlipayLogin
             * UC浏览器：com.UCMobile，com.UCMobile.main.UCMobile
             * 淘宝：com.taobao.taobao，com.taobao.tao.welcome.Welcome
             * 京东：com.jingdong.app.mall，com.jingdong.app.mall.main.MainActivity
             * 爱奇艺：com.qiyi.video，com.qiyi.video.WelcomeActivity
             * 今日头条：com.ss.android.article.news，com.ss.android.article.news.activity.MainActivity
             */
            String packageName = "com.eastmoney.android.berlin";
            String className = "com.eastmoney.android.berlin.activity.MainActivity";
            if (ActivityUtil.isActivityExists(this, packageName, className)) {
                ActivityUtil.startActivity(this, packageName, className);
            } else {
                showToast("不存在该应用");
            }
        });
        btn2.setOnClickListener(v -> {
            ActivityUtil.startActivity(this, TestJumpActivity.class,
                    R.anim.slide_right_in, R.anim.slide_left_out);
        });
        btn3.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(TestJumpActivity.EXTRA_DATA, btn3.getText().toString());
            ActivityUtil.startActivity(this, TestJumpActivity.class, bundle);
        });
    }

    //App工具
    private void testApp() {
        CommonLayoutUtil.initCommonLayout(this, "App工具",
                "本应用相关信息", "已安装应用列表", "打开应用设置页面",
                "安装测试应用", "卸载测试应用", "打开测试应用", "跳转至拨号界面",
                "拨打电话", "发送短信");
        btn1.setOnClickListener(v -> {
            ExtraUtil.alert(this, getAppInfo());
        });
        btn2.setOnClickListener(v -> {
            startActivity(TestAppListActivity.class);
        });
        btn3.setOnClickListener(v -> {
            AppUtil.openLocalAppSettings(this);
        });
        String testApkPackageName = "com.android.testinstall";
        String testApkPath = Environment.getExternalStorageDirectory() + "/AATest/test_install.apk";
        btn4.setOnClickListener(v -> {
            if (!PermissionUtil.requestPermissions(this, 1,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showToast("请先允许权限");
                return;
            }
            if (AppUtil.isAppInstall(this, testApkPackageName)) {
                showToast("应用已安装");
            } else {
                if (FileUtil.isFileExists(testApkPath)) {
                    AppUtil.installApp(this, testApkPath, getApplicationInfo().packageName + ".provider");
                } else {
                    try {
                        if (IOUtil.writeFileFromInputStream(testApkPath, getAssets().open("test_install.apk"), false)) { //写入成功
                            AppUtil.installApp(this, testApkPath, getApplicationInfo().packageName + ".provider");
                        } else {
                            showToast("文件写入失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("文件写入异常，" + e.getMessage());
                    }
                }
            }
        });
        btn5.setOnClickListener(v -> {
            if (AppUtil.isAppInstall(this, testApkPackageName)) {
                AppUtil.uninstallApp(this, testApkPackageName);
            } else {
                showToast("应用未安装");
            }
        });
        btn6.setOnClickListener(v -> {
            if (AppUtil.isAppInstall(this, testApkPackageName)) {
                AppUtil.openApp(this, testApkPackageName);
            } else {
                showToast("应用未安装");
            }
        });
        btn7.setOnClickListener(v -> {
            startActivity(IntentUtil.getDialIntent("13302480305"));
        });
        btn8.setOnClickListener(v -> {
            if (!PermissionUtil.requestPermissions(this, 1,
                    Manifest.permission.CALL_PHONE)) {
                showToast("请先允许权限");
                return;
            }
            startActivity(IntentUtil.getCallIntent("13302480305"));
        });
        btn9.setOnClickListener(v -> {
            startActivity(IntentUtil.getSendSmsIntent("13302480305", "短信内容"));
        });
    }

    private SpannableStringBuilder getAppInfo() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("应用图标： ");
        Drawable icon = AppUtil.getLocalAppIcon(this);
        if (icon != null) {
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(icon);
            builder.setSpan(span, 5, 6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        builder.append("\n应用名称：")
                .append(AppUtil.getLocalAppLabel(this))
                .append("\n安装路径：\n")
                .append(AppUtil.getLocalAppPath(this))
                .append("\nversionName：")
                .append(AppUtil.getLocalAppVersionName(this))
                .append("\nversionCode：")
                .append(String.valueOf(AppUtil.getLocalAppVersionCode(this)))
                .append("\n是否系统应用：")
                .append(String.valueOf(AppUtil.isLocalSystemApp(this)))
                .append("\n是否是Debug版本：")
                .append(String.valueOf(AppUtil.isLocalAppDebug(this)))
                .append("\n是否有root权限：")
                .append(String.valueOf(AppUtil.isLocalAppRoot()))
                .append("\nMD5值：\n")
                .append(AppUtil.getLocalAppSignatureMD5(this))
                .append("\nSHA1值：\n")
                .append(AppUtil.getLocalAppSignatureSHA1(this))
                .append("\n是否处于前台：")
                .append(String.valueOf(AppUtil.isLocalAppForeground(this)));
        LogUtil.e("AppInfo", " \n" + AppUtil.getLocalAppInfo(this));
        return builder;
    }

    //设备工具
    private void testDevice() {
        CommonLayoutUtil.initCommonLayout(this, "设备工具", false, true);
        StringBuilder sb = new StringBuilder();
        sb.append("设备是否root：").append(DeviceUtil.isDeviceRooted())
                .append("\n设备MAC地址：").append(DeviceUtil.getMacAddress(this))
                .append("\n设备厂商：").append(DeviceUtil.getManufacturer())
                .append("\n设备型号：").append(DeviceUtil.getModel())
                .append("\n设备品牌：").append(DeviceUtil.getBrand())
                .append("\nSDK版本号：").append(DeviceUtil.getSDKVersion())
                .append("\n系统版本号：").append(DeviceUtil.getOSVersion())
                .append("\n设备AndroidID：").append(DeviceUtil.getAndroidID(this))
                .append("\n分辨率：").append(DeviceUtil.getScreenResolution(this))
                .append("\n运营商名称：").append(DeviceUtil.getSimOperatorName(this))
                .append("\n系统语言：").append(DeviceUtil.getSystemLanguage())
                .append("\nIMEI：").append(DeviceUtil.getIMEI(this));
        tv.setText(sb.toString());
    }

}
