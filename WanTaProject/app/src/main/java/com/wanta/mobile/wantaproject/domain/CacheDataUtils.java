package com.wanta.mobile.wantaproject.domain;

import com.wanta.mobile.wantaproject.utils.Constants;

/**
 * Created by Administrator on 2017/5/18.
 */

public class CacheDataUtils {

    /**
     * 设置当前页面跳转需要保存的信息
     */
    public static void setCurrentNeedToSaveData(int icnfid, String title, String content, int likenum,
                                         int storenum, int userid, String username, String useravatar,
                                         String favourstate, String storedstate, String lng, String lat,
                                         String address, String uaddress, String createdat, String images, String followstate,
                                         String height, String weight, String bust, String bra,
                                         int cmtnum,String topics,String browsenum,String title_cn,String content_cn){
//        Constants.saveCacheDataList.clear();
        MostPopularInfo mostPopularInfo = new MostPopularInfo();
        mostPopularInfo.setIcnfid(icnfid);
        mostPopularInfo.setTitle(title);
        mostPopularInfo.setContent(content);
        mostPopularInfo.setLikenum(likenum);
        mostPopularInfo.setStorenum(storenum);
        mostPopularInfo.setUserid(userid);
        mostPopularInfo.setUsername(username);
        mostPopularInfo.setUseravatar(useravatar);
        mostPopularInfo.setFavourstate(favourstate);
        mostPopularInfo.setStoredstate(storedstate);
        mostPopularInfo.setLng(lng);
        mostPopularInfo.setLat(lat);
        mostPopularInfo.setAddress(address);
        mostPopularInfo.setUaddress(uaddress);
        mostPopularInfo.setCreatedat(createdat);
        mostPopularInfo.setImages(images);
        mostPopularInfo.setFollowstate(followstate);
        mostPopularInfo.setHeight(height);
        mostPopularInfo.setWeight(weight);
        mostPopularInfo.setBust(bust);
        mostPopularInfo.setBra(bra);
        mostPopularInfo.setCmtnum(cmtnum);
        mostPopularInfo.setTopics(topics);
        mostPopularInfo.setBrowsenum(browsenum);
        mostPopularInfo.setTitle_cn(title_cn);
        mostPopularInfo.setContent_cn(content_cn);
        Constants.saveCacheDataList.add(mostPopularInfo);
    }
    /**
     * 获取当前页面跳转需要保存的信息
     */
    public static MostPopularInfo getCurrentNeedToSaveData(){
        MostPopularInfo mostPopularInfo = Constants.saveCacheDataList.get(Constants.saveCacheDataList.size()-1);
        return mostPopularInfo;
    }

    public static void addCurrentNeedMsg(){
        MostPopularInfo mostPopularInfo = new MostPopularInfo();
        mostPopularInfo.setUsername("");
        Constants.saveCacheDataList.add(mostPopularInfo);
    }
    /**
     * 删除当前页面保存的信息
     */
    public static void removeCurrentCacheDate(){
        Constants.saveCacheDataList.remove(Constants.saveCacheDataList.size()-1);
    }

    /**
     * 设置当前选中了第几个话题信息
     */
    public static void setWhichTopics(int position){
        Constants.whichTopicsPosition = position;
    }

    /**
     * 设置发现中点击图片的超链接
     */
    public static void setFindImageLink(String url){
        Constants.findImageLindUrl = url;
    }

    /**
     * 设置当前选择的年月日
     */
    public static void setSelectYearMonthDay(int year,int month,int day){
        Constants.currentYear = year;
        Constants.currentMonth = month;
        Constants.currentDay = day;
    }

    /**
     * 设置当前图片选中的位子
     */
    public static void setSelectImagePosition(int fisrtItem,int thridItem){
        Constants.selectImageFirstItem = fisrtItem;
        Constants.selectImageThridItem = thridItem;
    }

    /**
     * 设置往单个页面传递的信息
     */
    public static void setJumpToSingleActivity(String url,String clothCatogry,String clothId){
        Constants.single_activity_url = url;
        Constants.single_activity_clothCatogry = clothCatogry;
        Constants.single_activity_clothId = clothId;
    }
}
