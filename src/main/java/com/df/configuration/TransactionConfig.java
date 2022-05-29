package com.df.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan
@Configuration
@EnableTransactionManagement
public class TransactionConfig {

}
