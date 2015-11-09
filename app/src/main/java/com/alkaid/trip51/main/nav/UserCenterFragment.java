package com.alkaid.trip51.main.nav;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.usercenter.MyBalanceActivity;
import com.alkaid.trip51.usercenter.MyPointsActivity;

/**
 * Created by alkaid on 2015/11/9.
 */
public class UserCenterFragment extends BaseFragment implements View.OnClickListener {
    private ViewGroup layContent;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View v = inflater.inflate(R.layout.main_user_center_fragment, container, false);
        initTitleBar(v);
        initView(v);
        layContent = (ViewGroup) v.findViewById(R.id.content);
        initItem(R.drawable.user_icon_fav, "我的收藏");
        initItem(R.drawable.user_icon_comment, "我的评论");
        initItem(R.drawable.user_icon_share, "分享好友");
        initItem(R.drawable.user_icon_mall, "积分商城");
        initItem(R.drawable.user_icon_server, "服务中心");
        initItem(R.drawable.user_icon_praise, "去鼓励我");
        return v;
    }

    private void initTitleBar(View v) {
        View layTitleBar = v.findViewById(R.id.title_bar);
        TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        ImageButton btnLeft = (ImageButton) v.findViewById(R.id.btn_back_wx);
        ImageButton btnRight = (ImageButton) v.findViewById(R.id.notify);
        tvTitle.setText("我的");
        btnLeft.setImageResource(R.drawable.home_navibar_icon_bell);
        btnRight.setImageResource(R.drawable.trip51_setting);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
            }
        });
    }

    private void initView(View v) {
        v.findViewById(R.id.ll_my_balance).setOnClickListener(this);
        v.findViewById(R.id.ll_my_points).setOnClickListener(this);
    }

    private void initItem(int resIconId, String name) {
        ViewHolder holder = new ViewHolder();
        holder.lay = (ViewGroup) inflater.inflate(R.layout.item_user_center, layContent, false);
        holder.icon = (ImageView) holder.lay.findViewById(R.id.ivItemIcon);
        holder.itemName = (TextView) holder.lay.findViewById(R.id.tvItemName);
        holder.icon.setImageResource(resIconId);
        holder.itemName.setText(name);
        layContent.addView(holder.lay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_my_balance:
                startActivity(new Intent(getActivity(), MyBalanceActivity.class));
                break;
            case R.id.ll_my_points:
                startActivity(new Intent(getActivity(), MyPointsActivity.class));
                break;
            default:
                break;
        }
    }

    private static class ViewHolder {
        private ViewGroup lay;
        private ImageView icon;
        private TextView itemName;
    }

}