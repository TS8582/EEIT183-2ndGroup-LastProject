package com.playcentric.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 調度器配置類
 * 
 * 啟用 Spring 的調度功能，允許在應用程序中使用 @Scheduled 註解。
 * 
 * @Configuration 註解表明這是一個 Spring 配置類。
 * @EnableScheduling 註解啟用 Spring 的任務調度功能。
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
}