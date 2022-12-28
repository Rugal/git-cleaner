# LingLong maven plugin

用于对本仓库对应的远端代码进行流水线工作。

## 用法

必须配置好蚂蚁集团的maven test仓库。

### 版本

1. [SNAPSHOT](http://mvn.test.alipay.net/artifactory/content/repositories/Alipay-Snapshot/com/alipay/cloudide/linglong-maven-plugin )
2. [RELEASE](http://mvn.test.alipay.net/artifactory/content/repositories/Alipay-Releases/com/alipay/cloudide/linglong-maven-plugin )

### 基本使用方法

```xml
<plugin>
  <groupId>com.alipay.cloudide</groupId>
  <artifactId>linglong-maven-plugin</artifactId>
  <version>${VERSION}</version>
  <configuration>
    <skip>false</skip>
    <failOnError>true</failOnError>
  </configuration>
</plugin>
```

# 开发指南

[文档](./DEVELOPMENT.md )
