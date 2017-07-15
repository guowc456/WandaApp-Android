package com.wanta.mobile.wantaproject.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.addpictag.PictureTagLayout;
import com.wanta.mobile.wantaproject.customview.FilterColorView;
import com.wanta.mobile.wantaproject.domain.CommentsInfo;
import com.wanta.mobile.wantaproject.domain.ImageItem;
import com.wanta.mobile.wantaproject.domain.MostPopularInfo;
import com.wanta.mobile.wantaproject.domain.TagMsgInfo;
import com.wanta.mobile.wantaproject.domain.TopicsInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangYongqiang on 2016/10/12.
 */
public class Constants {

    public final static String INTERNET_URL="http://1zou.me/api/";
    //问卷调查的url
    public final static String ASK_REQUEST_URL="http://1zou.me/apisq/user/qa";

//    public final static String ASK_REQUEST_URL="http://169.254.138.124:8080/NetPractice/VolleyPostJsonService";
    //文件上传
   public final static String FILE_AND_IMAGE_URL="http://1zou.me/transferFile";

    //首页图片下载地址
    public final static String FIRST_PAGE_IMAGE_URL = "http://1zou.me/apisq/images/icons/";

    //服务器的地址
    public final static String SERVICE_URL = "http://1zou.me/apisq/";

    //获取国内地图的数据库
    public static String WANTA_DB_NAME = "wanta.db";
    public static int WANTA_VERSION = 7;

    public final static int TO_MAIN = 0;//跳转到主界面
    public final static int MOSTPOPULAR_FRESH = 1;//最热界面的刷新
    public static int PHONE_WIDTH = 0;//手机屏幕的宽度
    public static int PHONE_HEIGHT = 0;//手机屏幕的高度
    public static float PHONE_DENSITY = 0;//手机屏幕像素的大小
    public static int Count = 0;//

    /**
     * 第二级和第三级菜单的高度
     */
    public static final int SECOND_ITEM_HEIGHT=110;
    public static final int THIRD_ITEM_HEIGHT=80;

    public static List<String> TAGS_LIST = new ArrayList<>();//记录当前选中的标签的信息
    public static List<String> LINK_LIST = new ArrayList<>();//记录当前链接的标签的信息
    public static List<String> IMAGE_URL = new ArrayList<>();//记录当前图片的位置信息
    public static String Cloth_catogry_number[] = new String[8];//记录不同衣服的数量
    public static String[] cloth_catogry_chinese = { "上衣","裤子","裙子","帽子","鞋子", "围巾","腰带","包"};
    public static String[][] cloth_catogry_thrid = {
            {"按颜色","按长度","按气温"},
            {"按颜色","连身裤","裤装","按长度","按裤型"},
            {"按颜色","半身裙","连衣裙","按长度","按气温"},
            {"按颜色"},
            {"按颜色","按鞋跟","轻便","易穿"},
            {"按颜色","按大小","按气温"},
            {"按颜色","按宽窄"},
            {"按颜色","按大小","按形变"}};
    public static String[][] cloth_catogry_thrid_url = {
            {"http://1zou.me/api/sq/useritems/","http://1zou.me/api/sq/upperbylength/","http://1zou.me/api/sq/upperbytemper/"},
            {"http://1zou.me/api/sq/useritems/","http://1zou.me/api/sq/jumpsuit/","http://1zou.me/api/sq/trouser/","http://1zou.me/api/sq/trouserbytemper/","http://1zou.me/api/sq/trouserbytype/"},
            {"http://1zou.me/api/sq/useritems/","http://1zou.me/api/sq/userskirts/","http://1zou.me/api/sq/userdresses/","http://1zou.me/api/sq/skirtbylength/","http://1zou.me/api/sq/upperbytemper/"},
            {"http://1zou.me/api/sq/useritems/"},
            {"http://1zou.me/api/sq/useritems/","http://1zou.me/api/sq/shoebyheight/","http://1zou.me/api/sq/shoebyportable/","http://1zou.me/api/sq/shoebyeasy/"},
            {"http://1zou.me/api/sq/useritems/","http://1zou.me/api/sq/scarfbysize/","http://1zou.me/api/sq/scarfbytemper/"},
            {"http://1zou.me/api/sq/useritems/","http://1zou.me/api/sq/waistbywidth/"},
            {"http://1zou.me/api/sq/useritems/","http://1zou.me/api/sq/bagbysize/","http://1zou.me/api/sq/bagbystable/"}};
    public static String[][] cloth_catogry_thrid_url1 = {
            {"/upper","/","/"},
            {"/trouser","/","/","/","/"},
            {"/skirt","/","/","/","/"},
            {"/hat"},
            {"/shoe","/","/","/"},
            {"/scarf","/","/"},
            {"/waist","/"},
            {"/bag","/","/"}};
    public static String cloth_catogry_english[] = {"upper", "trouser", "skirt", "hat", "shoe", "scarf", "waist", "bag"};

    public static List<String> Wardrobe_detail_imags_url = new ArrayList<>();//记录当前衣橱要显示图片的地址
    public static List<String> Wardrobe_detail_imags_id = new ArrayList<>();//记录当前图片id的信息
    public static List<String> PERSON_DESIGN_MESSAGE = new ArrayList<>();//记录当前用户设置的信息
    public static int current_number = 0;//记录当前某种衣服的数目
//    public static List<Bitmap> detail_images = new ArrayList<>();//记录当前要显示的图片
    public static HashMap<String,Bitmap> detail_images = new HashMap<>();//记录当前要显示的图片

    public static boolean isWardrobeInitPopwindow = true;//第一次进入到我的衣橱的时候是否显示出来第一个弹出框
    public static boolean isWardrobePhotoPopwindow = true;//点击我的衣橱里面添加的按钮的时候是否显示出来第一个弹出框
    public static boolean isClickDeleteImage = false;//点击我的衣橱里面添加的按钮的时候中弹出框中的第一个子弹出框是否删除了
    public static boolean isClickDeleteImage1 = false;//点击我的衣橱里面添加的按钮的时候中弹出框中的第二个子弹出框是否删除了

    public static HashMap<Integer,FilterColorView> display_images = new HashMap<>();//记录当前viewpager要显示的图片
    public static List<PictureTagLayout> all_picture_tag_view = new ArrayList<>();//记录当前viewpager要显示的图片
    public static List<Bitmap> upload_images = new ArrayList<>();//记录当前要上传的图片
    public static List<String> upload_images_url = new ArrayList<>();//记录当前要上传的图片的地址
    public static LruCache<String,Bitmap> upload_images_lrucache = new LruCache<>((int) (Runtime.getRuntime() .maxMemory() / 1024));//保存图片

    public static boolean isAskQuestion = false;//当前没有设置问卷调查
    public static String askquestion_msg[] = new String[8];//记录问卷调查中的信息

    public static List<Bitmap> modify_bitmap_list = new ArrayList<>();//保存修改后的图片
    public static List<String> modify_bitmap_list_url = new ArrayList<>();//保存修改后的图片的地址
    public static LruCache<String,Bitmap> modify_bitmap_list_lrucache = new LruCache<>((int) (Runtime.getRuntime() .maxMemory() / 1024));//保存图片

    public static int isChange = 1;
    public static int mostpopular_load = 1;//最热界面的标志位


    //记录当前职业的信息
    public static String currentProfession = "";//记录当前的职业
    public static String currentLocation = "";//记录当前的居住地

    public static List<String> imageList = new ArrayList<>();//记录当前浏览大图的所有图片

    //记录当前是否登陆
    public static boolean isLogin = false;//判断当前是否登陆
    public static Bitmap authorIcon = null;//判断当前是否已经有选中的图片了
    public static String authorIconUrl = null;//判断当前是否已经有选中的图片了

    public static final int CROP_ACTIVITY_RESULT=100;

    //记录当前的userid
    public static int USER_ID = 50;
    public static String USER_NAME = "m_wdanonymous";
    public static String  STATUS = "status";
    public static String  AVATAR = "";//当前图片的信息
    public static String  HEIGHT = "";//当前的高度
    public static String  WEIGHT = "";//当前体重
    public static String  BUST = "";//当前胸围
    public static String  BRA = "";
    public static String  UADRESS = "";

    //记录当前是否表示喜欢
    public static boolean islike = false;
    public static boolean isCare = false;
    public static int likenum = 0;

    public static boolean isTopicLike = false;
    public static int topicsLikeNum = 0;

    //当前评论的信息
    public static List<CommentsInfo> mCommentsInfoList = new ArrayList<>();

    //当前是否收藏
    public static boolean isstore = false;
    public static int storenum = 0;


   //当前的所在的市区
   public static String  CURRENT_ADDRESS = "";
   public static String  CURRENT_CITY = "";

    //保存当前的同风格搭配和同风格单品的数据
    public static List<MostPopularInfo> style_data = new ArrayList<>();
    //获取当前confid的最小值
    public static int minConfid = 0 ;

    //记录当前tags中图片的URL
    public static List<String> TAGS_IAMGE_URL = new ArrayList<>();

    //设置当前选中需要添加标签的图片的地址
    public static List<String> TAGS_SELECT_IAMGE_URL = new ArrayList<>();

    public static List<MostPopularInfo> heartNum = new ArrayList<>();//记录heart的数目
    public static List<TopicsInfo> topicsHeartNum = new ArrayList<>();//记录话题heart的数目

    //记录当前总的评论数
    public static int allComments = 0;

    //记录当前评论点赞的状态
    public static boolean isLikeComment = false;
    //记录当前评论点赞的数目
    public static int currentCommnetNum = 0;

    //当前衣橱中选择的条目
    public static int firstItem = 0;
    public static int thridItem = 0;


    //记录当前选中标签属性的第几个
    public static int selectTagsAttribute = 0;
    //记录每个标签中选中的图片的信息
    public static List<String> imagesOfEachTag = new ArrayList<>();

    //记录每个标签的信息
    public static List<String> TAGS_SELECT_IAMGE_URL1 = new ArrayList<>();
    public static List<String> TAGS_SELECT_IAMGE_URL2 = new ArrayList<>();
    public static List<String> TAGS_SELECT_IAMGE_URL3 = new ArrayList<>();
    public static List<String> TAGS_SELECT_IAMGE_URL4 = new ArrayList<>();
    public static List<String> TAGS_SELECT_IAMGE_URL5 = new ArrayList<>();
    public static List<String> TAGS_SELECT_IAMGE_URL6 = new ArrayList<>();
    public static List<String> TAGS_SELECT_IAMGE_URL7 = new ArrayList<>();
    public static List<String> TAGS_SELECT_IAMGE_URL8 = new ArrayList<>();
    public static List<String> TAGS_SELECT_IAMGE_URL9 = new ArrayList<>();


    //当前的图片进行拉伸的最大值和最小值
    public static int picStretchMin = 0;
    public static int picStretchMax = 255;

    //获取城市的信息
    public static List<String> GET_CITY_MSG = new ArrayList<>();

    //设置中保存每个条目的信息
    public static String[] PERSON_DESIGN_ITEM_MESSAGE = new String[27];
    public static String[] often_array = new String[]{
        "昵称","性别","常居地","生日","职业","个性签名"
    };
    public static String[] body_array = new String[]{
            "测量标准","身高","体重","底围","罩杯","腰围","臂围"
    };
    public static String[] waist_array = new String[]{
            "体长","肩腋围","前腰节高","臂长","上裆","腰腿长"
    };
    public static String[] head_array = new String[]{
            "头围","颈围","乳点围/胸围","实测胸底围","手臂围","肩宽","大腿围"
    };
    public static String[] back_array = new String[]{
            "背宽"
    };
    public static String[] often_description = new String[]{
            "m_wdanonymous","","","1900-1-1","",""
    };
    public static String[] body_description = new String[]{
            "","头顶到脚底","裸体体重","平时穿的内衣尺码","平时穿的内衣型号","肚脐一圈","臀部最粗一圈"
    };
    public static String[] waist_description = new String[]{
            "锁骨到脚面","腋下到肩头的一圈","锁骨-乳头-胸底围-肚脐","伸直手臂，肩头到手腕(不是从手下开始哦)","肚脐到大腿分叉","站直，肚脐到脚踝(脚踝骨头尖)",
    };
    public static String[] head_description = new String[]{
            "头部最粗一圈","颈根部四点连线(最粗的位置)","胸部最粗处一圈","RF底部的一圈","大臀最粗的一圈","站直挺胸两肩头连线","大腿最粗一圈"
    };
    public static String[] back_description = new String[]{
            "站直挺胸两腋下连线"
    };

    public static String[] topics_name = new String[]{
            "曲线美人","高阶尺码模特","形体正能量","超越尺寸的美","真实美人","反形体霸凌","心智健康意识","爱自己的身体","高阶尺码的自信角","穿出自信","反尺码歧视"
            ,"健康和美存在于任何尺码","丰满模特","瘦身产业的终极秘密"
    };

    public static List<String> all_location_msg = new ArrayList<>();

    public static boolean isCopyDataIntoDB = false;


    //设置从首页面或者二级界面向他人信息界面跳转的标志
    public static int jumpFlag = 0;

    //记录弹出框中的价格信息
    public static String other_author_pop_window_price = "";
    public static String other_author_pop_window_description = "";
    public static String other_author_pop_window_pinpai = "";
    public static String other_author_pop_window_gouyu = "";

    //记录当前图片的路径
    public static  List<ImageItem> SAVE_PHONE_PICTURES = new ArrayList<>();

    //当前选中的记录的个数
    public static final List<String> SELECT_PIC_URL = new ArrayList<>();
    //保存当前添加衣服的时候选中的衣服的类型
    public static String currentClothType = "";

    public static List<PictureTagLayout> Current_Picturetag_layout = new ArrayList<>();

    public static boolean  isContainThePoint = false;
    public static boolean  isOnTagSlide = false;

    //当前保存传递的参数信息
    public static List<MostPopularInfo> saveCacheDataList = new ArrayList<>();

    //保存当前的标签的信息
    public static Map<Integer,List<TagMsgInfo>> saveTagMsgMap = new HashMap<>();


    //设置当前选中的主fragment
    public static int selectFragmentNumber = 1;//1表示首页，2表示衣橱，3表示发现，4表示我的

    //设置当前选中的话题的位置
    public static int whichTopicsPosition = 0;

    //设置发现图片的超链接
    public static String findImageLindUrl = "";

    //设置当前的年月日
    public static int currentYear = 0;
    public static int currentMonth = 0;
    public static int currentDay = 0;


    //设置当前的位子
    public static int selectImageFirstItem = 0;
    public static int selectImageThridItem = 0;

    //设置到跳转到单个界面的数据
    public static String single_activity_url = "";
    public static String single_activity_clothCatogry = "";
    public static String single_activity_clothId = "";


    //当前选择的时间
    public static int wardar_calendar_year = 0;
    public static int wardar_calendar_month = 0;
    public static int wardar_calendar_day = 0;
}
