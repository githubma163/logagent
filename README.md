1. 日常开发中都需要打印日志，这样出问题时可以通过日志来定位问题，不过打日志的代码比较固定，有过几年开发经验的同学应该会立马想到通过aop来打印日志，不过aop方式对应用有一定的侵入性。这里提供一种通过字节码的方式来打印日志，对应用做到零侵入

2. 主要技术思想
* 日志查看有elk这种完善的技术栈，这里继续是用elk，不过注意版本是6.x
* Java字节码修改是用javasisst，其实字节码修改有很多框架，javasisst使用相对简单
* 为了保证零侵入性，使用java agent技术，在启动程序的时候添加agent jar包来修改字节码
* es数据操作采用了rest api方式，这样不用依赖jpa相关jar
* 数据源设计的时候支持多个，目前采用的es
* 为了防止日志采集对应用的影响，采用了批量保存日志和异步保存设计

3. 具体使用
* 下载代码，使用mvn clean package命令打包，得到logagent.jar
* 安装elk，这里使用elk 6.x版本，并且使用docker-compose来构建，docker-compose文件在文件的resources目录下，启动命令是docker-compose up -d
* 编写个空的springboot项目，添加一个controller，保证能够正常访问，这里假设springboot程序是log-test-1.0-SNAPSHOT.jar
* 启动程序，命令如下，注意include，url字段需要换成实际值
```
java -javaagent:/Users/max/Documents/docker/logagent.jar=app=log,include=com.max.log.test.controller,exclude=,send=es,url=http://127.0.0.1:9200,async=false,batch=10 -jar /Users/max/Documents/docker/log-test-1.0-SNAPSHOT.jar
```
* 访问controller接口，需要超过10次，上面的batch变量是10，然后去kibana中查看

4.java agent使用场景
* btrace https://www.jianshu.com/p/1b52561e3848
* athars https://arthas.aliyun.com/doc/
* greys https://github.com/oldmanpushcart/greys-anatomy
* es apm https://www.elastic.co/cn/blog/monitoring-applications-with-elasticsearch-and-elastic-apm
* apache skywalking https://skywalking.apache.org/
