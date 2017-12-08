package com.rainshieh.simplemvp.model.beans;

import android.os.Environment;

import java.io.File;

/**
 * author: xiecong
 * create time: 2017/12/7 17:30
 * lastUpdate time: 2017/12/7 17:30
 */

public class Constants {
    //================= PATH ====================
    public static final String PATH_DATA = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "SimpleMvp";
    public static final String PATH_CACHE = PATH_DATA + File.separator + "NetCache";
}
