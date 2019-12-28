package com.music.fm544.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 格式工具类
 */
public class FormatUtils {

    /**
     * 将时间转化为分秒格式
     * @param length
     * @return
     */
    public static String formatTime(int length) {

        Date date = new Date(length);//调用Date方法获值

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");//规定需要形式

        String TotalTime = simpleDateFormat.format(date);//转化为需要形式

        return TotalTime;

    }

}
