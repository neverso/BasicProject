package com.android.frame.mvp

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.android.base.BaseApplication
import com.android.basicproject.R
import com.android.frame.mvp.extra.LoadingDialog.LoadingDialog
import com.android.frame.mvp.extra.NetReceiver
import com.android.util.NetworkUtil
import com.android.util.ToastUtil
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by xuzhb on 2019/12/29
 * Desc:基类Fragment(MVP)
 */
abstract class BaseFragment<V : IBaseView, P : BasePresenter<V>> : Fragment(), IBaseView,
    SwipeRefreshLayout.OnRefreshListener {

    protected val mGson = Gson()
    protected var mPresenter: P? = null

    //防止RxJava内存泄漏
    private var mCompositeDisposable = CompositeDisposable()

    //加载框
    private var mLoadingDialog: LoadingDialog? = null

    //通用的下拉刷新组件，需在布局文件中固定id名为swipe_refresh_layout
    protected var mSwipeRefreshLayout: SwipeRefreshLayout? = null

    //通用的RecyclerView组件，需在布局文件中固定id名为R.id.recycler_view
    protected var mRecyclerView: RecyclerView? = null

    //网路异常的布局
    private var mNetErrorFl: FrameLayout? = null
    private var mNetReceiver: NetReceiver? = null

    protected var mActivity: FragmentActivity? = null
    protected var mContext: Context? = null
    protected var mRootView: View? = null

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //第一次加载时，setUserVisibleHint会比onAttach先回调，此时布局为null;
        //之后切换Fragment时，当Fragment变得可见或可见时都会回调setUserVisibleHint，此时布局不为null
        mRootView?.let {
            if (isVisibleToUser) {
                onVisible()
            } else {
                onInvisible()
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = this.activity
        mContext = context
        mPresenter = getPresenter()
        mPresenter?.attachView(this as V)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false)
        }
        initBaseView()
        initNetReceiver()
        return mRootView
    }

    //初始化一些通用控件，如加载框、SwipeRefreshLayout、网络错误提示布局
    protected open fun initBaseView() {
        mLoadingDialog = LoadingDialog(context!!, R.style.LoadingDialogStyle)
        //获取布局中的SwipeRefreshLayout组件，重用BaseCompatActivity的下拉刷新逻辑
        //注意布局中SwipeRefreshLayout的id命名为swipe_refresh_layout，否则mSwipeRefreshLayout为null
        //如果SwipeRefreshLayout里面只包含RecyclerView，可引用<include layout="@layout/layout_recycler_view" />
        mSwipeRefreshLayout = mRootView?.findViewById(R.id.swipe_refresh_layout)
        //如果当前布局文件不包含id为swipe_refresh_layout的组件则不执行下面的逻辑
        mSwipeRefreshLayout?.let {
            it.setOnRefreshListener(this)
            it.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        }
        //获取布局中的RecyclerView组件，注意布局中RecyclerView的id命名为recycler_view，否则mRecyclerView为null
        mRecyclerView = mRootView?.findViewById(R.id.recycler_view)
        //在当前布局的合适位置引用<include layout="@layout/layout_net_error" />，则当网络出现错误时会进行相应的提示
        mNetErrorFl = mRootView?.findViewById(R.id.net_error_fl)

        /*
         * 完整的一次下拉刷新过程
         * 1、在布局文件中包含id为swipe_refresh_layout的SwipeRefreshLayout组件；
         * 2、下拉时SwipeRefreshLayout的onRefresh()调用BasePresenter的loadData()重新加载数据；
         * 3、请求数据结束或请求数据异常调用IBaseView的loadFinish()收起SwipeRefreshLayout的刷新头部，完成一次下拉刷新；
         * 所以实现下拉刷新只需要：
         * 1、在布局文件中包含id为swipe_refresh_layout的SwipeRefreshLayout组件；
         * 2、重写BasePresenter的loadData()方法；
         * 3、在刷新结束时调用IBaseView的loadFinish()方法收起刷新头部，而在自定义Observer类CustomObserver中已经实现了
         *    这部分逻辑，在请求数据结束或请求数据出现异常时会调用IBaseView的loadFinish()
         */
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleView(savedInstanceState)
        initListener()
    }

    //执行onCreate接下来的逻辑
    abstract fun handleView(savedInstanceState: Bundle?)

    //所有的事件回调均放在该层，如onClickListener等
    abstract fun initListener()

    //获取布局
    abstract fun getLayoutId(): Int

    //获取Activity对应的Presenter，对于不需要额外声明Presenter的Activity，可以选择继承CommonBaseActivity
    abstract fun getPresenter(): P

    //页面可见且布局不为null时回调
    protected open fun onVisible() {

    }

    //页面不可见且布局不为null时回调
    protected open fun onInvisible() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterNetReceiver()
        //销毁加载框
        mLoadingDialog?.dismiss()
        mLoadingDialog = null

        //解绑activity和presenter
        mPresenter?.detachView()
        mPresenter = null
    }

    override fun onDetach() {
        super.onDetach()
        //取消所有正在执行的订阅
        mCompositeDisposable.clear()
    }

    //显示加载框
    override fun showLoading(message: String, cancelable: Boolean) {
        activity?.runOnUiThread {
            mLoadingDialog?.show(message, cancelable)
        }
    }

    //取消加载框
    override fun dismissLoading() {
        activity?.runOnUiThread {
            mLoadingDialog?.dismiss()
        }
    }

    //显示Toast
    override fun showToast(text: CharSequence, isCenter: Boolean, longToast: Boolean) {
        activity?.runOnUiThread {
            ToastUtil.showToast(text, isCenter, longToast)
        }
    }

    //数据加载失败
    override fun loadFail() {
        showNetErrorLayout()
    }

    //数据加载完成，收起下拉刷新组件SwipeRefreshLayout的刷新头部
    override fun loadFinish() {
        //如果布局文件中不包含id为swipe_refresh_layout的控件，则swipeRefreshLayout为null
        activity?.runOnUiThread {
            mSwipeRefreshLayout?.let {
                if (it.isRefreshing) {
                    it.isRefreshing = false  //停止刷新
                }
            }
        }
    }

    //跳转到登录界面
    override fun gotoLogin() {
        BaseApplication.instance.finishAllActivities()
        val intent = Intent()
        intent.setAction("登录页的action")
        startActivity(intent)
    }

    //RxJava建立订阅关系，方便Fragment销毁时取消订阅关系防止内存泄漏
    override fun addDisposable(d: Disposable) {
        mCompositeDisposable.add(d)
    }

    //下拉刷新
    override fun onRefresh() {
        mPresenter?.loadData()  //重新加载数据
        dismissLoading()  //下拉时就不显示加载框了
    }

    //网络断开连接提示
    fun showNetErrorLayout() {
        //如果当前布局文件中不包含layout_net_error则netErrorFl为null，此时不执行下面的逻辑
        activity?.runOnUiThread {
            mNetErrorFl?.visibility =
                if (NetworkUtil.isConnected(activity!!)) View.GONE else View.VISIBLE
        }
    }

    //启动指定的Activity
    protected fun startActivity(clazz: Class<*>, extras: Bundle? = null) {
        activity?.let {
            val intent = Intent()
            extras?.let {
                intent.putExtras(it)
            }
            intent.setClass(it, clazz)
            startActivity(intent)
        }
    }

    //启动指定的Activity并接收返回的结果
    protected fun startActivityForResult(
        clazz: Class<*>,
        requestCode: Int,
        extras: Bundle? = null
    ) {
        activity?.let {
            val intent = Intent()
            extras?.let {
                intent.putExtras(it)
            }
            intent.setClass(it, clazz)
            startActivityForResult(clazz, requestCode)
        }
    }

    //注册广播动态监听网络变化
    private fun initNetReceiver() {
        //如果不含有layout_net_error，则不注册广播
        if (mNetErrorFl == null) {
            return
        }
        //动态注册，Android 7.0之后取消了静态注册方式
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        mNetReceiver = NetReceiver()
        context?.registerReceiver(mNetReceiver, filter)
        mNetReceiver!!.setOnNetChangeListener {
            showNetErrorLayout()
        }
    }

    //注销广播
    private fun unregisterNetReceiver() {
        if (mNetErrorFl == null) {
            return
        }
        context?.unregisterReceiver(mNetReceiver)
        mNetReceiver = null
    }

}