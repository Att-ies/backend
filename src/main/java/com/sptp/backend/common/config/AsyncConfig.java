package com.sptp.backend.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // 비동기처리
@Slf4j
public class AsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor(){ // 스레드 풀 직접 지정
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("processor count {}", processors);
        executor.setCorePoolSize(processors); // 1) 코어 개수: if(러닝 쓰레드 개수 < 코어 개수) 코어 개수 다다르기까지 남은 쓰레드 사용;
        executor.setMaxPoolSize(processors); // 3) 맥스 개수: if(러닝 쓰레드 개수 >= 큐 용량) 맥스 개수 다다르기까지 새로운 쓰레드 만들어 처리;
        executor.setQueueCapacity(50); // 2) 큐 용량: if(러닝 쓰레드 개수 >= 코어 개수) 큐 용량 다쓸때까지 큐에 쌓음;
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();
        return executor;
    }
}
