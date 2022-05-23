## 快速启动
### 启动主站
`PrimaryStationFactory.create(从站主机地址, 从站端口, IEC102).run();`

### 启动从站
`SecondaryStationFactory.create(开放端口, IEC102).run();`

## 使用问题

### 从站如何声明待上传文件？
1. 构造 Iec102UploadFile 实体。
2. 调用 Iec102DataConfig 的静态函数 addFile。

### 如果启动从站文件成功传输后如何获取回执？
#### Spring Boot 项目
1. 在启动类上开启注解`@EnableCallback`。
2. 实现接口 Iec102Callback 并注入到容器。