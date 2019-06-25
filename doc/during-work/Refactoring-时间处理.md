# Refactoring-时间处理

@2019/06/25 周二 11:48:26.58



## AgeUtil

### The original code

Please be advised that the original code did not have test codes.

```java
public class AgeUtil {

    public static int getAgeByBirth(Date birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);
            if (birth.after(now)) {
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {
            return 0;
        }
    }
}
```

### Refactoring

#### Add unit test

```java
@Test
public void testAge() throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date date = format.parse("2018-06-24");
    assertEquals(1, getAgeByBirth(date));

    date = format.parse("2018-06-25");
    assertEquals(1, getAgeByBirth(date));

    date = format.parse("2018-06-26");
    assertEquals(0, getAgeByBirth(date));

    date = format.parse("2000-01-22");
    assertEquals(19, getAgeByBirth(date));

    date = format.parse("2004-02-29");
    assertEquals(15, getAgeByBirth(date));

    date = format.parse("1990-11-18");
    assertEquals(28, getAgeByBirth(date));
}        
```

**Found there is a logical problem**

## DateUtil

原来的code

```java
public class DateUtil {

    public static Date addDay(Date date, int day) {
        Calendar gc = prepare(date);
        gc.add(Calendar.DAY_OF_MONTH, day);
        return gc.getTime();
    }

    private static Calendar prepare(Date date) {
        Calendar gc = Calendar.getInstance();
        gc.setTime(date);
        return gc;
    }
}
```

#### Add test

```java
public class DateUtilTest {
    @Test
    public void addDayTest() {
        Instant instant = LocalDateTime.of(2008, 10, 12, 2, 30)
                                       .atZone(ZoneId.systemDefault())
                                       .toInstant();
        Instant addOneDay = LocalDateTime.of(2008, 10, 13, 2, 30)
                                         .atZone(ZoneId.systemDefault())
                                         .toInstant();
        Date date = addDay(Date.from(instant), 1);
        assertEquals(Date.from(addOneDay), date);
    }
}
```

#### Refactor

```java
public class DateUtil {
    public static Date addDay(Date date, int day) {
        LocalDateTime localDateTime = date.toInstant()
                                          .atZone(ZoneOffset.systemDefault())
                                          .toLocalDateTime()
                                          .plusDays(day);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault())
                                      .toInstant());
    }
}
```

