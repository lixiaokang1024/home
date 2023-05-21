package com.project.lxk.home.controller;

import com.project.lxk.home.dao.DemoDao;
import com.project.lxk.home.dao.DemoReadDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

  @Autowired
  private DemoDao demoDao;

  @Autowired
  private DemoReadDao demoReadDao;

  @GetMapping
  public String index() {
    demoDao.count();
    demoReadDao.count();
    return "/index";
  }
}
