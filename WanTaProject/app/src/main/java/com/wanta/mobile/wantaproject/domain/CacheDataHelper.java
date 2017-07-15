package com.wanta.mobile.wantaproject.domain;

/**
 * Created by Administrator on 2017/6/2.
 */

public class CacheDataHelper {
    /**
     * 添加传递的参数的信息
     */
    public static void addHasArgumentsMethod(){
        CacheDataUtils.setCurrentNeedToSaveData(CacheDataUtils.getCurrentNeedToSaveData().getIcnfid(),
                CacheDataUtils.getCurrentNeedToSaveData().getTitle(),CacheDataUtils.getCurrentNeedToSaveData().getContent(),
                CacheDataUtils.getCurrentNeedToSaveData().getLikenum(),CacheDataUtils.getCurrentNeedToSaveData().getStorenum(),
                CacheDataUtils.getCurrentNeedToSaveData().getUserid(),CacheDataUtils.getCurrentNeedToSaveData().getUsername(),
                CacheDataUtils.getCurrentNeedToSaveData().getUseravatar(),CacheDataUtils.getCurrentNeedToSaveData().getFavourstate(),
                CacheDataUtils.getCurrentNeedToSaveData().getStoredstate(),CacheDataUtils.getCurrentNeedToSaveData().getLng(),
                CacheDataUtils.getCurrentNeedToSaveData().getLat(),CacheDataUtils.getCurrentNeedToSaveData().getAddress(),
                CacheDataUtils.getCurrentNeedToSaveData().getUaddress(),CacheDataUtils.getCurrentNeedToSaveData().getCreatedat(),
                CacheDataUtils.getCurrentNeedToSaveData().getImages(),CacheDataUtils.getCurrentNeedToSaveData().getFollowstate(),
                CacheDataUtils.getCurrentNeedToSaveData().getHeight(),CacheDataUtils.getCurrentNeedToSaveData().getWeight(),
                CacheDataUtils.getCurrentNeedToSaveData().getBust(),CacheDataUtils.getCurrentNeedToSaveData().getBra(),
                CacheDataUtils.getCurrentNeedToSaveData().getCmtnum(),CacheDataUtils.getCurrentNeedToSaveData().getTopics(),
                CacheDataUtils.getCurrentNeedToSaveData().getBrowsenum(),CacheDataUtils.getCurrentNeedToSaveData().getTitle_cn(),
                CacheDataUtils.getCurrentNeedToSaveData().getContent_cn());
    }

    /**
     * 添加空对象的方法
     */
    public static void addNullArgumentsMethod(){
        CacheDataUtils.addCurrentNeedMsg();
    }
}
