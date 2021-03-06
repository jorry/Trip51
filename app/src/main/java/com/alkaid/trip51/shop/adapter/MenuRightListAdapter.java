package com.alkaid.trip51.shop.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.dataservice.mapi.ImageLoaderManager;
import com.alkaid.trip51.dataservice.mapi.SimpleImageLoaderManager;
import com.alkaid.trip51.model.shop.Food;
import com.alkaid.trip51.model.shop.FoodCategory;
import com.alkaid.trip51.model.shop.Shop;
import com.alkaid.trip51.shop.FoodListFragment;
import com.alkaid.trip51.util.BitmapUtil;
import com.alkaid.trip51.widget.Operator;
import com.alkaid.trip51.widget.pinnedheaderlistview.SectionedBaseAdapter;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class MenuRightListAdapter extends SectionedBaseAdapter implements ImageLoaderManager{

    private Context mContext;
    private String TAG = getClass().getName();
    private List<FoodCategory> foodCategories;
    private Shop currShop;
    private Handler mHandler;
    private SimpleImageLoaderManager imageLoaderManager;

    public MenuRightListAdapter(Context context, Shop currShop,Handler handler) {
        this.mContext = context;
        this.currShop = currShop;
        this.mHandler=handler;
        this.imageLoaderManager=new SimpleImageLoaderManager(context);
    }

    /**
     * 赋数据
     *
     * @param foodCategories 设置的数据
     */
    public void setMenuListData(List<FoodCategory> foodCategories) {
        this.foodCategories = foodCategories;
    }

    @Override
    public Object getItem(int section, int position) {
        if (foodCategories != null) {
            return foodCategories.get(section).getFoods().get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        if (foodCategories == null) {
            return 0;
        }
        return foodCategories.size();
    }

    @Override
    public int getCountForSection(int section) {
        if (foodCategories != null) {
            return foodCategories.get(section).getFoods().size();
        } else {
            return 0;
        }
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Food food = (Food) getItem(section, position);
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.menu_list_content_item, null);
            holder = new ViewHolder();
            holder.ivFood = (NetworkImageView) convertView.findViewById(R.id.iv_menu_food);
            holder.tvNameFood = (TextView) convertView.findViewById(R.id.tv_menu_food_name);
            holder.tvCountFood = (TextView) convertView.findViewById(R.id.tv_menu_food_count);
            holder.tvPriceFood = (TextView) convertView.findViewById(R.id.tv_menu_food_price);
            holder.opNum = (Operator) convertView.findViewById(R.id.op_food_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ivFood.setDefaultImageResId(R.drawable.temp_shop_thumb);
        holder.ivFood.setErrorImageResId(R.drawable.temp_shop_thumb);
        if (food != null) {
            holder.ivFood.setImageUrl(food.getFoodimg(),imageLoaderManager.getImgloader());
            holder.tvNameFood.setText(food.getFoodname());
            holder.tvCountFood.setText("已售:" + food.getSales() + "份");
            holder.tvPriceFood.setText(food.getPrice() + "元");
            holder.opNum.setOperationCallback(new Operator.OperationListener() {
                @Override
                public void onAddClick(int i) {
                    updateFoodCategories(food, holder.opNum.selectedCount);
                }

                @Override
                public void onSubClick(int i) {
                    updateFoodCategories(food, holder.opNum.selectedCount);
                }

                @Override
                public void onTextChange(int i) {
                    //updateFoodCategories(food, holder.opNum.selectedCount);
                }
            });
            holder.opNum.setValue(food.getFoodNum());
            LogUtil.d(TAG, "Section " + section + " Item " + position);
        }
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.menu_list_section_head, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        TextView tv = (TextView) layout.findViewById(R.id.textItem);
        if (foodCategories != null && foodCategories.get(section) != null) {
            tv.setText(foodCategories.get(section).getCategoryname());
        }
        layout.setClickable(false);
//        ((TextView) layout.findViewById(R.id.textItem)).setText(leftStr[section]);
        LogUtil.d(TAG, "Header for section " + section);
        return layout;
    }

    /**
     * 更新菜单的数据和购物车的数据，根据食物id
     *
     * @param food  需要改变的食物
     * @param foodNum 需要改变的食物的数量
     */
    public void updateFoodCategories(Food food, int foodNum) {
        food.setFoodNum(foodNum);
        if(food == null){
            return;
        }
        if (foodCategories != null) {
            for (int i = 0; i < foodCategories.size(); i++) {
                List<Food> foods = foodCategories.get(i).getFoods();
                for (int j = 0; j < foods.size(); j++) {
                    if (foods.get(j).getFoodid() == food.getFoodid()) {
                        foodCategories.get(i).getFoods().get(j).setFoodNum(foodNum);
                    }
                }
            }
        }
        App.shoppingCartService().updateFoodToCart(currShop.getShopid(),food);
        mHandler.sendEmptyMessage(FoodListFragment.UPDATE_SHOPPING_CART);
}

    @Override
    public void pauseImageLoad() {
        imageLoaderManager.pauseImageLoad();
    }

    @Override
    public void resumeImageLoad() {
        imageLoaderManager.resumeImageLoad();
    }


    private class ViewHolder {
    NetworkImageView ivFood;
    TextView tvNameFood;
    TextView tvCountFood;
    TextView tvPriceFood;
    Operator opNum;//选择的数量
}

}
