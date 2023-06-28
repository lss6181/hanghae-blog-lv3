package com.sparta.hanghaebloglv3;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.ZoneId;
import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication
public class HanghaeBlogLv3Application {

    @PostConstruct
    public void setDefaultTimezone() {
        // DB에는 고정된 타임존을 지정하여 삽입하는 것이 일반적이므로, 해당 어플리케이션의 타임존을 UTC로 고정한다.
        // -> default는 해당 pc의 타임존 세팅을 따라간다. 일반적으로 +09:00인데, 한국에서만 서비스하는 백엔드인 경우
        //    타임존 세팅은 크게 상관 없으나, 여러 국가에서 서비스 해야 하는 경우에는 타임존을 무조건 통일해야 한다.
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
    }

    public static void main(String[] args) {
        SpringApplication.run(HanghaeBlogLv3Application.class, args);
    }

}
