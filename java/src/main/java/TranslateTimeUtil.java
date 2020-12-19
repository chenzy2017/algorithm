import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenzeyong
 * @description 计算出两个时间之间相差了几天几小时几分钟几秒
 * @date 2020-12-19 15:28
 */
@Data
@Slf4j
public class TranslateTimeUtil {

    public static String translateTime(Date start, Date end) {
        if (start == null || end == null) {
            return "";
        }
        long time = end.getTime() - start.getTime();
        long days = time / (1000 * 60 * 60 * 24);
        long hours = (time - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (time - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (time - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
        String descript = "";
        if (days > 0) {
            descript = descript + (days + "天");
        }
        if (hours > 0) {
            descript = descript + (hours + "小时");
        }
        if (minutes > 0) {
            descript = descript + (minutes + "分");
        }
        if (seconds > 0) {
            descript = descript + (seconds + "秒");
        }
        return descript;
    }

    /**
     * string类型转换为date类型
     *
     * @param dateStr string类型的时间
     * @param pattern 转换的时间模式  yyyy-MM-dd HH:mm:ss
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String dateStr, String pattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(dateStr);
        return date;
    }

    public static void main(String[] args) throws ParseException {
        String a1 = "2020-12-14 08:52:08";
        String a2 = "2020-12-14 20:20:37";

        String b1 = "2020-12-15 08:59:03";
        String b2 = "2020-12-15 20:20:37";

        String c1 = "2020-12-16 08:51:42";
        String c2 = "2020-12-16 22:00:33";

        String d1 = "2020-12-17 08:49:28";
        String d2 = "2020-12-18 01:11:42";

        String e1 = "2020-12-19 11:39:52";
        String e2 = "2020-12-19 22:50:10";

        System.out.println(translateTime(stringToDate(a1, "yyyy-MM-dd HH:mm:ss"), stringToDate(a2, "yyyy-MM-dd HH:mm:ss")));
        System.out.println(translateTime(stringToDate(b1, "yyyy-MM-dd HH:mm:ss"), stringToDate(b2, "yyyy-MM-dd HH:mm:ss")));
        System.out.println(translateTime(stringToDate(c1, "yyyy-MM-dd HH:mm:ss"), stringToDate(c2, "yyyy-MM-dd HH:mm:ss")));
        System.out.println(translateTime(stringToDate(d1, "yyyy-MM-dd HH:mm:ss"), stringToDate(d2, "yyyy-MM-dd HH:mm:ss")));
        System.out.println(translateTime(stringToDate(e1, "yyyy-MM-dd HH:mm:ss"), stringToDate(e2, "yyyy-MM-dd HH:mm:ss")));

    }
}