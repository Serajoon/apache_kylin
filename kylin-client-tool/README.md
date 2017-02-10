Forked from https://github.com/apache/kylin/tree/1.3.x/tools/kylin_client_tool

python kylin_client_tool.py -b -C prj0207_cube -B i -O 24 每隔24小时执行一次cube增量创建，但是REST请求第一次创建没问题，第二次就出现json解析错误，/jobs/build.py Class CubeBuildJob中，还要查看代码出错原因，用java请求就没问题。