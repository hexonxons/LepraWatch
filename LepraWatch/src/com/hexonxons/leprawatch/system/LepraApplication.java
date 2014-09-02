package com.hexonxons.leprawatch.system;

import org.koroed.lepra.Lepra;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;

import com.hexonxons.leprawatch.R;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class LepraApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        
        // Config image loader.
        DisplayImageOptions dio = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
            .resetViewBeforeLoading(true)
            .showImageOnLoading(R.drawable.image_loading)
            .displayer(new FadeInBitmapDisplayer(300, true, true, true))
            .bitmapConfig(Bitmap.Config.ARGB_8888)
            .build();
        
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .memoryCache(new LruMemoryCache((int) (Runtime.getRuntime().maxMemory() / 8)))
            .threadPoolSize(Runtime.getRuntime().availableProcessors() * 2)
            .defaultDisplayImageOptions(dio)
            .build();
        
        ImageLoader.getInstance().init(config);
        
        // Init lepra.
        Lepra.getInstance().init(getApplicationContext());
        
        // Start network service.
        Intent networkServiceIntent = new Intent(this, NetService.class);
        startService(networkServiceIntent);
    }
}
