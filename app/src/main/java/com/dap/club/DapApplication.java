package com.dap.club;

import android.app.Application;
import android.content.Context;
import com.avos.avoscloud.AVOSCloud;
import com.dap.club.util.DapLog;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by dap on 16/1/27.
 */
public class DapApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initFresco(this);
        AVOSCloud.initialize(this,"1ootsw3y4smje21veqtkn2w6xtk1al2hdn6xs05vvzp0ed4k","p0ir4kljnq05g4jm7udb7u2lrk45cy1c5hawkufgtkcck00p");
    }
    private static DapApplication instance;


    /**
     * 获取 application
     *
     * @return
     */
    public static DapApplication getInstance() {
        return instance;
    }

    private static void initFresco(Context context) {
        Fresco.initialize(context);
    }
}
