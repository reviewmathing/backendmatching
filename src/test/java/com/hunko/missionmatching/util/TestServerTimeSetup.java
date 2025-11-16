package com.hunko.missionmatching.util;

import org.junit.jupiter.api.AfterEach;

public interface TestServerTimeSetup {

    default void setUpServerTimeFactory(LocalDateTimeFactory localDateTimeFactory){
        ServerTime.SERVER_TIME_FACTORY = localDateTimeFactory;
    }

    @AfterEach
    default void rollbackServerTime(){
        ServerTime.SERVER_TIME_FACTORY = new LocalDateTimeFactory();
    }
}
