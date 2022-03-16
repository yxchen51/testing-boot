#what's testing-boot?
+  这是一个基于spring-boot开发的接口测试框架，支持Excel数据驱动，运行方法排序，动态数据源切换，API包测试代码自动生成，日志收集等功能
+  项目基于SpringBoot+Junit4实现，扩展了部分Junit4的功能，实现了部分Junit5的特性，封装了部分通用组件

## 注解说明
+ `@DisplayName`可作用于测试类或方法上，标识当前测试模块或者方法的中文名称便于生成报告
+ `@DataDriver`作用于测试方法，数据驱动注解，用于静态或者动态读取测试用例
+ `@RunWeight`作用于测试方法，对测试方法优先级进行排序
+ `@ConditionalIgnore`作用于测试方法，传入实现`DriverRule.IgnoreCondition`接口的类，可以根据条件动态跳过测试
## 数据库支持
+  为方便快速进行数据操作，放弃了引入ORM框架直接使用JDBCTemplate操作进行SQL操作
+  多数据源支持及动态切换，在测试项目中配置好要操作的多个数据源，使用的时候需要哪个用哪个
+  动态数据源切换，使用`@UseDB`注解在方法上传入配置好的数据源，实现动态切换
## RPC接口测试支持
+  对于已经声明API的Jar包可以采用类似`Feign`的形式实现本地化的RPC调用，API规范默认使用`SpringMvcContract`
+  自定义实现的``RestTemplate`可以实现接口请求前后的自定义拦截，如：参数打印，通用响应码判断，动态请求头等
## 多环境配置文件支持
+  框架中支持配置多个环境的配置信息，如：数据库，Api接口地址，通用中间件等
+  运行过程中通过`run.env`参数动态加载环境配置文件，实现测试脚本与环境的分离，真正做到一套脚本多处运行
## 日志收集与展示
+  框架启动时会初始化日志收集数据，并增加一个关闭钩子，运行过程中自动记录日志信息，包括运行的模块、用例、参数、通过情况及错误原因
+  为支持大规模测试运行，目前框架运行完成后将日志数据发送至Kafka队列中
+  配套的自动化平台会消费Kafka队列进行日志的持久化、页面图形化展示

后续会专门做一个使用本框架的测试项目demo...

有任何问题欢迎随时探讨:yxchen51@126.com
#测试示例项目和使用文档完善中... 

