package com.dhd;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.dhd.webflux.TransactionsHandler;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TransactionsAppApplicationTests {

    @Autowired
    TransactionsHandler transactionsHandler;

    @Test
    public void contextLoads() {

        assertThat(transactionsHandler).isNotNull();
    }

}
