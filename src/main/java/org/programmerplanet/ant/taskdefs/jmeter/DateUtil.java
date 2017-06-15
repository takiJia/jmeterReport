package org.programmerplanet.ant.taskdefs.jmeter;



import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jiaou
 * Date: 16-7-23
 * Time: 下午10:51
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {

    /**
     * 获取当前时间，"HH:mm:ss"格式返回
     * @return
     */
    public static String getCurrentTime()
    {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

    /**
     * 获取当前日期，"yyyy-MM-dd HH:mm:ss"格式返回
     * @return
     */
    public static String getCurrentDate()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

    /**
     * 获取format格式的日期
     * @param format
     * @return
     */
    public static String getDate(String format)
    {
        SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
        return df.format(new Date());
    }


}

