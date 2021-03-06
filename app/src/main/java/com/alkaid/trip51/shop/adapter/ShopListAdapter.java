package com.alkaid.trip51.shop.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.dataservice.mapi.ImageLoaderManager;
import com.alkaid.trip51.dataservice.mapi.LruImageCache;
import com.alkaid.trip51.dataservice.mapi.SimpleImageLoaderManager;
import com.alkaid.trip51.model.shop.Shop;
import com.alkaid.trip51.util.UnitUtil;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alkaid on 2015/11/7.
 */
public class ShopListAdapter extends BaseAdapter implements ImageLoaderManager{
    private LayoutInflater mInflater;
    private List<Shop> shops=new ArrayList<Shop>();
    private SimpleImageLoaderManager imgloaderManager;

    public ShopListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.imgloaderManager=new SimpleImageLoaderManager(context);
    }

    public void setData(List<Shop> shops){
        this.shops=shops;
    }
    public List<Shop> getData(){
        return shops;
    }

    @Override
    public int getCount() {
        return /*shops.isEmpty()?20:*/shops.size();
    }

    @Override
    public Object getItem(int position) {
        return shops.get(position);
    }

    @Override
    public long getItemId(int position) {
//        if(shops==null||shops.isEmpty())
//            return position;
        return shops.get(position).getShopid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.main_home_list_item, null);
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvAvgPay = (TextView) convertView.findViewById(R.id.tvAvgPay);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            holder.tvAreaAndType = (TextView) convertView.findViewById(R.id.tvAreaAndType);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
            holder.rtbProductRating=(RatingBar)convertView.findViewById(R.id.rtbProductRating);
            holder.imgShopThumb= (NetworkImageView) convertView.findViewById(R.id.imgShopThumb);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
        }
//        if(shops.isEmpty()){
//            return convertView;
//        }
        Shop shop=shops.get(position);
        holder.rtbProductRating.setRating(shop.getTotallevel());
        holder.tvTitle.setText(shop.getShopname());
        holder.tvAvgPay.setText(shop.getAvgpay());
        String status="";
        if(shop.getPrivaterroomstatus()==1){
            status+="包房（<font color='green'>空闲</font>）       ";
        }else if(shop.getPrivaterroomstatus()==2){
            status+="包房（<font color='red'>紧张</font>）       ";
        }
        if(shop.getHallstatus()==1){
            status+="大厅（<font color='green'>空闲</font>）       ";
        }else if(shop.getHallstatus()==2){
            status+="大厅（<font color='red'>紧张</font>）       ";
        }
        holder.tvStatus.setText(Html.fromHtml(status));
        holder.tvAreaAndType.setText((shop.getAreaname() == null ? "" : shop.getAreaname() + " ") + (shop.getDiningtypename() == null ? "" : shop.getDiningtypename()));
        holder.tvDistance.setText(shop.getDistance()>0? UnitUtil.formatDistance(shop.getDistance()):"");
        holder.imgShopThumb.setDefaultImageResId(R.drawable.temp_shop_thumb);
        holder.imgShopThumb.setErrorImageResId(R.drawable.temp_shop_thumb);
        holder.imgShopThumb.setImageUrl(shop.getImageurl(),imgloaderManager.getImgloader());
        return convertView;
    }

    @Override
    public void pauseImageLoad() {
        imgloaderManager.pauseImageLoad();
    }

    @Override
    public void resumeImageLoad() {
        imgloaderManager.resumeImageLoad();
    }

    private class ViewHolder{
        public TextView tvTitle,tvAvgPay,tvStatus,tvAreaAndType,tvDistance;
        public RatingBar rtbProductRating;
        public NetworkImageView imgShopThumb;
    }
}
