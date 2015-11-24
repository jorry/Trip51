package com.alkaid.trip51.model.shop;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alkaid on 2015/11/24.
 */
public class Baseinfo implements Serializable {
    private long shopid;
    private String shopname;
    private String avgpay;
    private String diningtypename;
    private String imgurl;
    private float starlevel;
    private String areanamel;
    private int privaterroomstatus;
    private int loungestatus;
    private int hallstatus;
    private int distance;
    private List<ShopImg> shopimgs;

    public static class ShopImg{
        public String shopimgurl;

    }

    public long getShopid() {
        return shopid;
    }

    public void setShopid(long shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getAvgpay() {
        return avgpay;
    }

    public void setAvgpay(String avgpay) {
        this.avgpay = avgpay;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public float getStarlevel() {
        return starlevel;
    }

    public void setStarlevel(float starlevel) {
        this.starlevel = starlevel;
    }

    public String getAreanamel() {
        return areanamel;
    }

    public void setAreanamel(String areanamel) {
        this.areanamel = areanamel;
    }

    public int getPrivaterroomstatus() {
        return privaterroomstatus;
    }

    public void setPrivaterroomstatus(int privaterroomstatus) {
        this.privaterroomstatus = privaterroomstatus;
    }

    public int getLoungestatus() {
        return loungestatus;
    }

    public void setLoungestatus(int loungestatus) {
        this.loungestatus = loungestatus;
    }

    public int getHallstatus() {
        return hallstatus;
    }

    public void setHallstatus(int hallstatus) {
        this.hallstatus = hallstatus;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<ShopImg> getShopimgs() {
        return shopimgs;
    }

    public void setShopimgs(List<ShopImg> shopimgs) {
        this.shopimgs = shopimgs;
    }

    public String getDiningtypename() {
        return diningtypename;
    }

    public void setDiningtypename(String diningtypename) {
        this.diningtypename = diningtypename;
    }
}