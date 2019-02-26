package com.example.idutil.distributedidgenerator.sequtil;

import com.example.idutil.distributedidgenerator.core.SqlSeqUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : gaohui
 * @Date : 2019/2/18 15:57
 * @Description :
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test01 {


    @Test
    public void test01() {
//        long id = SqlSeqUtil.getId("intention");
//        System.out.println(id);

        for (int i = 0; i < 16; i++) {
            long id = SqlSeqUtil.getId("intention");
            System.out.println(id);
        }
    }
}
