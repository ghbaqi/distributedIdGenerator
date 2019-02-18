package com.example.idutil.distributedidgenerator.mapper;

import com.example.idutil.distributedidgenerator.entity.Idgenerator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : gaohui
 * @Date : 2019/2/18 11:34
 * @Description :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {

    @Autowired
    private IdgeneratorMapper mapper;

    @Test
    public void test01() {
        Idgenerator intention = mapper.selectByPrimaryKey("intention");
        System.out.println(intention.getCurrentValue());

    }
}
