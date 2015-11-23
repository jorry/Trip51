package com.alkaid.trip51.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alkaid.base.common.LogUtil;
import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.dataservice.mapi.CacheType;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResFoodList;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * 商户菜单明细列表
 */
/**
 * Created by jyz on 2015/11/8.
 */
public class MenuListFragment extends BaseFragment{
    private long shopid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.menu_list_fragment,container,false);
        shopid=getArguments().getLong(MenuFragment.BUNDLE_KEY_SHOPID);
        if(shopid<=0){
            throw new RuntimeException("没有设置currShopid,请检查代码！");
        }
        return v;
    }

    private void loadData(){
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        unBeSignform.put("shopid", shopid+"");
//        unBeSignform.put("pageindex", "1");
//        unBeSignform.put("pagesize", "20");
        final String tag="smscode"+(int)(Math.random()*1000);
        setDefaultPdgCanceListener(tag);
        showPdg();
        App.mApiService().exec(new MApiRequest(CacheType.NORMAL, MApiService.URL_SHOP_FOODS, beSignForm, unBeSignform, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtil.v(response.toString());
                Gson gson = new Gson();
                ResFoodList resdata = gson.fromJson(response, ResFoodList.class);
                if (resdata.isSuccess()) {
                    //TODO 刷新UI
                } else {
                    dismissPdg();
                    //TODO 暂时用handleException 应该换成失败时的正式UI
                    handleException(TradException.create(resdata.getMsg()));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.v(error.toString());
                dismissPdg();
                //TODO 暂时用handleException 应该换成失败时的正式UI
                handleException(new TradException());
            }
        }), tag);
    }
}
