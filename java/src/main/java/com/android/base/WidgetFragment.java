package com.android.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.android.frame.mvc.BaseFragment;
import com.android.java.R;
import com.android.widget.Dialog.TestDialogActivity;
import com.android.widget.TestSingleWidgetActivity;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:控件篇
 */
public class WidgetFragment extends BaseFragment {

    public static WidgetFragment newInstance() {
        return new WidgetFragment();
    }

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_widget;
    }

    @OnClick({R.id.popup_tv, R.id.piechart_tv, R.id.linechart_tv, R.id.dialog_tv, R.id.single_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.popup_tv:
                break;
            case R.id.piechart_tv:
                break;
            case R.id.linechart_tv:
                break;
            case R.id.dialog_tv:
                startActivity(TestDialogActivity.class);
                break;
            case R.id.single_tv:
                startActivity(TestSingleWidgetActivity.class);
                break;
        }
    }
}
