package com.project.lxk.home.ds;

import com.project.lxk.home.util.CodableEnumTypeHandler;
import com.project.lxk.home.util.SpringBootVFS;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@org.springframework.context.annotation.Configuration
@MapperScan(annotationClass = Mapper.class, basePackages = "com.project.lxk.home", sqlSessionFactoryRef = "sqlSessionFactory")
public class DsMConfiguration {
  @Bean
  @Primary
  @ConfigurationProperties("home.ds.m")
  public DsProperties dsProperties() {
    return new DsProperties();
  }

  @Bean
  @ConfigurationProperties("home.ds.m.hikari")
  public HikariConfig hikariConfig(DsProperties dsProperties) {
    final HikariConfig hikariConfig = new HikariConfig();
    final String jdbcUrl = String.format(
        "jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=utf8&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&logger=com.mysql.jdbc.log.Slf4JLogger&profileSQL=%b",
        dsProperties.getHost(),
        dsProperties.getSchema(),
        dsProperties.isProfileSql()
    );
    hikariConfig.setJdbcUrl(jdbcUrl);
    hikariConfig.setUsername(dsProperties.getUsername());
    hikariConfig.setPassword(dsProperties.getPassword());
    hikariConfig.setPoolName("HOME-DS-M");
    hikariConfig.setConnectionInitSql("SET NAMES utf8mb4");
    return hikariConfig;
  }

  @Bean
  @Primary
  public HikariDataSource dataSource(HikariConfig hikariConfig) {
    return new HikariDataSource(hikariConfig);
  }

  @Bean
  @Primary
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    Configuration configuration = new Configuration();
    configuration.setUseGeneratedKeys(true);
    configuration.setUseColumnLabel(true);
    configuration.setMapUnderscoreToCamelCase(true);
    configuration.getTypeHandlerRegistry().register(CodableEnumTypeHandler.class);

    SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
    factory.setDataSource(dataSource);
    factory.setVfs(SpringBootVFS.class);
    factory.setConfiguration(configuration);
    // TODO: need to remove, we should use full qualify name
    factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/**/*.xml"));

    return factory.getObject();
  }
}
