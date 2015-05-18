package com.itxiaowu.fileOpreation;


import android.text.format.Time;

import java.io.File;

/**
 * Created by Chen Jerry on 3/9/2015.
 */
public class FileOperation {

    private static final String FOLDERNAME = "/yunsoo";
//    private static final String FILENAME = "/YunSooFile_";

    public static String createNewFileName(String FILENAME) {
        String fileName;
        Time t = new Time();
        t.setToNow();

        int i_year = t.year;
        int i_month = t.month+1;
        int i_day = t.monthDay;
        int i_th = t.hour;
        int i_tm = t.minute;
        int i_ts = t.second;
        String ty = String.valueOf(i_year);
        String tmon = String.valueOf(i_month);
        String td = String.valueOf(i_day);
        String th = String.valueOf(i_th);
        String tm = String.valueOf(i_tm);
        String ts = String.valueOf(i_ts);

        if (i_th < 10)
            th = "0" + th;

        if (i_tm < 10)
            tm = "0" + tm;

        if (i_ts < 10)
            ts = "0" + ts;

        if (i_month < 10)
            tmon = "0" + tmon;

        if (i_day < 10)
            td = "0" + td;

        if (i_th < 10)
            fileName = FILENAME + ty + "_" + tmon + "_" + td + "_01.txt";
        else if (i_th < 14)
            fileName = FILENAME + ty + "_" + tmon + "_" + td + "_02.txt";
        else if (i_th < 18)
            fileName = FILENAME + ty + "_" + tmon + "_" + td + "_03.txt";
        else if (i_th < 24)
            fileName = FILENAME + ty + "_" + tmon + "_" + td + "_04.txt";
        else
            fileName = FILENAME + ty + "_" + tmon + "_" + td + "_00.txt";

        String folderName = android.os.Environment.getExternalStorageDirectory() + FOLDERNAME;

        File file = new File(folderName);
        if (!file.exists())
            file.mkdirs();

        fileName = folderName + fileName;

        return fileName;
    }
}
