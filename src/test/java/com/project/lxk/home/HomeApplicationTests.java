package com.project.lxk.home;

import com.project.lxk.home.dao.DemoDao;
import com.project.lxk.home.dao.DemoReadDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HomeApplicationTests {

  @Autowired
  private DemoDao demoDao;

  @Autowired
  private DemoReadDao demoReadDao;

  @Test
  void contextLoads() {
    System.out.println("M db count = " + demoDao.count());
    System.out.println("R db count = " + demoReadDao.count());
  }

}
