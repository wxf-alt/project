package javase.date;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;


public class DateAPI {

    @Test
    public void test1() {
        Date date = new Date();
        // 获取当前时间
        System.out.println(date); // Wed Dec 08 15:33:07 CST 2021
        // 1638948787672
        System.out.println(date.getTime());

        // 使用毫秒数 创建Date 时间
        Date date1 = new Date(1638948787672L);
        System.out.println(date1);
    }

    @Test
    public void test2() {
        java.sql.Date date = new java.sql.Date(1638948787672L);
        System.out.println(date.toString());// 2021-12-08
        System.out.println(date.getTime());// 1638948787672
    }

    @Test
    public void test3() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat();
        String sj = format.format(date);
        // 默认输出 21-12-8 下午3:43 格式
        System.out.println(sj);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sj1 = sdf.format(date);
        System.out.println(sj1); //

        // 将字符串转成日期
        try {
            // SimpleDateFormat 设置的时间格式和字符串内容的时间格式必须一致，否则转换失败
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = sdf2.parse("2021-12-09 08:57:50");
            System.out.println(parse);
        } catch (ParseException e) {
            System.out.println("----------");
        }
    }

    @Test
    public void test4() {
        // 日历类
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getClass().getName()); // java.util.GregorianCalendar

        System.out.println("----------------------------");
        // 获取当天是当月的第几天
        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(day_of_month); // 9
        // 获取星期几  --> 1是星期日 5是星期四
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        // 获取当天所在周在当月的第几周
        int day_of_week_in_month = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        System.out.println(day_of_week - 1); // 4
        System.out.println(day_of_week_in_month); // 2

        // 在当天是当月的第几天基础上加或者减一些天数
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        int day_of_month_add = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(day_of_month_add); // 11

        calendar.add(Calendar.DAY_OF_MONTH, -2);
        int day_of_month_remove = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(day_of_month_remove); // 9

        // 在当天是当月的第几天基础上修改成其他值
        calendar.set(Calendar.DAY_OF_MONTH, 30);
        int day_of_month_set = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(day_of_month_set); // 30

        // 获取日历所对应的日期和时间
        Date time = calendar.getTime();
        System.out.println(time); // Thu Dec 30 09:32:25 CST 2021
    }

    @Test
    public void test5() {
        // 获取当前日期
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate); // 2021-12-09
        // 获取当前时间
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime); // 09:59:44.957
        // 获取当前日期时间
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime); // 2021-12-09T09:59:44.957
        System.out.println(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))); // 2021-12-09 09:59:44

        // 根据指定日期/时间创建对象
        LocalDate localDate1 = LocalDate.of(2021, 12, 9);
        System.out.println(localDate1); // 2021-12-09
        LocalTime localTime1 = LocalTime.of(9, 49, 23);
        System.out.println(localTime1); // 09:49:23

        // 当天是 月/周/年 的第几天
        int dayOfMonth = localDate.getDayOfMonth();
        System.out.println(dayOfMonth); // 9
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        System.out.println(dayOfWeek.getValue()); // 4
        System.out.println(localDate.getDayOfYear()); // 343

        // 获取 年份/月份
        System.out.println(localDate.getYear()); // 2021
        System.out.println(localDate.getMonth().getValue()); // 12

        // 获取 小时/分钟
        System.out.println(localTime.getHour()); // 9
        System.out.println(localTime.getMinute()); // 59

        // 设置 2021-12-09 -》 2021-12-20 不会改变原值
        LocalDate localDate2 = localDate.withDayOfMonth(20);
        System.out.println(localDate2); // 2021-12-20

        // 在当前时间 加上天数
        LocalDate plusDays = localDate.plusDays(2);
        System.out.println(plusDays); // 2021-12-11
    }

    @Test
    public void test6() {

        // 返回默认 UTC 时区的 Instant 对象
        Instant instant = Instant.now();
        System.out.println(instant); // 2021-12-09T02:11:07.966Z

        // 获取毫秒数对应的时间
        Instant instant1 = Instant.ofEpochMilli(1639015995825L);
        System.out.println(instant1); // 2021-12-09T02:13:15.825Z

        // 获取时间对应的毫秒数
        long epochMilli = instant.toEpochMilli();
        System.out.println(epochMilli); // 1639016003435

        // 在中时区的基础上 + 上 8小时 (变相获取当前地区时间)
        OffsetDateTime time = Instant.now().atOffset(ZoneOffset.ofHours(8));
        System.out.println(time); // 2021-12-09T10:45:32.417+08:00

        // 获取东八区时间
        Instant now = Instant.now(Clock.offset(Clock.systemUTC(), Duration.ofHours(8)));
        System.out.println(now); // 2021-12-09T10:45:32.418Z
    }

    @Test
    public void test7() throws ParseException {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime); // 2021-12-09T11:05:51.613

        // 1.使用预定义的标准格式
        DateTimeFormatter isoLocalDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        // 将日期格式 转成 字符串
        String str = isoLocalDateTime.format(localDateTime);
        System.out.println(str); // 2021-12-09T11:05:51.613

        // 2.使用本地化相关的格式
        DateTimeFormatter sdf = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        String str1 = sdf.format(localDateTime); // 2021年12月9日 星期四
        System.out.println(str1);

        // 不能使用 FormatStyle.FULL 会报错,格式转换异常
        DateTimeFormatter sdf2 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG); // 2021年12月9日 上午11时07分11秒
        // DateTimeFormatter sdf2 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM); // 2021-12-9 11:05:51
        // DateTimeFormatter sdf2 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT); // 21-12-9 上午11:07
        String str2 = sdf2.format(LocalDateTime.now());
        System.out.println(str2);

        // 3.自定义格式
        // 将日期转成字符串格式
        DateTimeFormatter sdf3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String str3 = sdf3.format(LocalDateTime.now());
        System.out.println(str3); // 2021-12-09 11:09:55

        // 将字符串转成日期格式
        TemporalAccessor parse = sdf3.parse("2021-12-09 11:09:55");
        System.out.println(parse); // {},ISO resolved to 2021-12-09T11:09:55
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse("2021-12-09 11:09:55");
        System.out.println(date); // Thu Dec 09 11:09:55 CST 2021
    }



}
