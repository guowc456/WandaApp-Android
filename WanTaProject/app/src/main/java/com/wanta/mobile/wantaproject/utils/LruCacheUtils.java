package com.wanta.mobile.wantaproject.utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by WangYongqiang on 2016/12/23.
 */
public class LruCacheUtils {
    private LruCache<String, Bitmap> mMemoryCache;
    int MAXMEMONRY = (int) (Runtime.getRuntime() .maxMemory() / 1024);
    public LruCacheUtils() {
        if (mMemoryCache == null)
            mMemoryCache = new LruCache<String, Bitmap>(
                    MAXMEMONRY / 8) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                    return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
                }

                @Override
                protected void entryRemoved(boolean evicted, String key,
                                            Bitmap oldValue, Bitmap newValue) {
                    Log.v("LruCacheUtils", "hard cache is full , push to soft cache");

                }
            };
    }
    public void clearCache() {
        if (mMemoryCache != null) {
            if (mMemoryCache.size() > 0) {
                Log.v("LruCacheUtils",
                        "mMemoryCache.size() " + mMemoryCache.size());
                mMemoryCache.evictAll();
                Log.v("LruCacheUtils", "mMemoryCache.size()" + mMemoryCache.size());
            }
            mMemoryCache = null;
        }
    }

    public synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (mMemoryCache.get(key) == null) {
            if (key != null && bitmap != null)
                mMemoryCache.put(key, bitmap);
        } else
            Log.w("LruCacheUtils", "the res is aready exits");
    }

    public synchronized Bitmap getBitmapFromMemCache(String key) {
        Bitmap bm = mMemoryCache.get(key);
        if (key != null) {
            return bm;
        }
        return null;
    }

    /**
     * 移除缓存
     *
     * @param key
     */
    public synchronized void removeImageCache(String key) {
        if (key != null) {
            if (mMemoryCache != null) {
                Bitmap bm = mMemoryCache.remove(key);
                if (bm != null)
                    bm.recycle();
            }
        }
    }
}
