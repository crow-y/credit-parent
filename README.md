**目录描述**
````
credit-parent                                  # 根目录
+- credit-spring-boot-parent                   # 公共目录 (如常量、枚举、工具类等)
|   +- creidt                                  # 信贷平台公共包
|
+- demo-parent                                 # demo目录
|   +- demo-logback                            # logback组件demo
|   +- demo-simple-server                      # 服务集合demo（组件集合）
|
+- spring-boot-starters-parent                 # spring-starter目录 (服务依赖组装)
|   +- simple-server-spring-boot-starter       # simple starters
|
+- tools-parent                                # 工具包目录
    +- tools-datasource-druid                  # druid数据库连接池
    +- tools-exception-handler                 # 异常工具
    +- tools-log-appender-logback              # 日志工具
    +- tools-middleware-kafka                  # kafka中间件
    +- tools-scheduler-control-custom          # 调度中心(自研)
    +- tools-security-jwt-custom               # jwt用户令牌(自研)
````
