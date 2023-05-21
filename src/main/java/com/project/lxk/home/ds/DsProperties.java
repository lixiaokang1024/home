package com.project.lxk.home.ds;

import lombok.Data;

@Data
public class DsProperties {
  private String host;
  private String schema;
  private String username;
  private String password;
  private boolean profileSql = false;
}
