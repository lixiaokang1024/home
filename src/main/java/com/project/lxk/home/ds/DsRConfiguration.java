package com.project.lxk.home.ds;

import com.project.lxk.home.util.CodableEnumTypeHandler;
import com.project.lxk.home.util.SpringBootVFS;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@org.springframework.context.annotation.Configuration
@MapperScan(annotationClass = ReadMapper.class, basePackages = "com.project.lxk.home", sqlSessionFactoryRef = "readSqlSessionFactory")
public class DsRConfiguration {
  @Bean
  @ConfigurationProperties("home.ds.r")
  public DsProperties dsRProperties() {
    return new DsProperties();
  }

  @Bean
  @ConfigurationProperties("home.ds.r.hikari")
  public HikariConfig readHikariConfig(@Qualifier("dsRProperties") DsProperties dsRProperties) {
    final HikariConfig hikariConfig = new HikariConfig();
    final String jdbcUrl = String.format(
        "jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=utf8&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&logger=com.mysql.jdbc.log.Slf4JLogger&profileSQL=%b",
        dsRProperties.getHost(),
        dsRProperties.getSchema(),
        dsRProperties.isProfileSql()
    );
    hikariConfig.setJdbcUrl(jdbcUrl);
    hikariConfig.setUsername(dsRProperties.getUsername());
    hikariConfig.setPassword(dsRProperties.getPassword());
    hikariConfig.setPoolName("HOME-DS-R");
    hikariConfig.setConnectionInitSql("SET NAMES utf8mb4");
    return hikariConfig;
  }

  @Bean
  public HikariDataSource readDataSource(@Qualifier("readHikariConfig") HikariConfig readHikariConfig) {
    return new HikariDataSource(readHikariConfig);
  }

  @Bean
  public SqlSessionFactory readSqlSessionFactory(@Qualifier("readDataSource") DataSource readDataSource) throws Exception {
    Configuration configuration = new Configuration();
    configuration.setUseGeneratedKeys(true);
    configuration.setUseColumnLabel(true);
    configuration.setMapUnderscoreToCamelCase(true);
    configuration.getTypeHandlerRegistry().register(CodableEnumTypeHandler.class);

    SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
    factory.setDataSource(readDataSource);
    factory.setVfs(SpringBootVFS.class);
    factory.setConfiguration(configuration);
    // TODO: need to remove, we should use full qualify name
    factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/**/*.xml"));

    return factory.getObject();
  }
}
