主要介绍如何构建和使用 Apache Kylin 的文档。
# kylin是什么

> Extreme OLAP Engine for Big Data

For more details, see the website [http://kylin.apache.org](http://kylin.apache.org).

# 内容

* kylin-RESTFful	
* kylin-mybatis
* kylin-echar
* Kylin-client-tool

## kylin-RESTFful
 提供通过RESTful方式操作kylin,

## kylin-mybatis
kylin提供了标准的JDBC接口，能够和传统BI工具进行很好的集成。例子使用mybatis框架连接kylin,将来可以跟spring、springMVC等框架进行整合提供服务

URL格式:`jdbc:kylin://IP:7070/项目名`

##kylin-echar
使用echar展示kylin的内容

##Kylin-client-tool
Kylin-client-tool是一个用python编写的，完全基于kylin的rest api的工具。可以实现kylin的cube创建，按时build cube，job的提交、调度、查看、取消与恢复。

Kylin-client-tool更详细的说明请看[Kylin Client Tool 使用教程](http://kylin.apache.org/cn/docs/tutorial/kylin_client_tool.html)

Forked from [https://github.com/apache/kylin/tree/1.3.x/tools/kylin_client_tool](https://github.com/apache/kylin/tree/1.3.x/tools/kylin_client_tool)
