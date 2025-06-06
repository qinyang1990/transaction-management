## 主要接口：
交易系统提供4个(创建、修改、查看、删除)接口。

+ 创建接口: 提供存入/支取/转账功能
+ 修改： 提供对之前交易的金额修改功能
+ 查看: 提供多条件查询交易流水
+ 删除:  提供对之前交易

## 设计思路:
### 工程分为三个模块:
+ <font style="color:#080808;background-color:#ffffff;">transaction-api: 提供底层rpc接口的声明</font>
+ <font style="color:#080808;background-color:#ffffff;">transcation-sdk: 服务间公共sdk包，负责全局模型定义、缓存、异常等</font>
+ <font style="color:#080808;background-color:#ffffff;">transaction-service: 交易系统服务</font>

### 主要考虑角度:
### 业务: 
实现的主要功能为账户、余额、交易流水。(包括细节的账户状态、交易币种、客户号、操作人)

### 设计: 
业务功能分为不同层次，最外层的参数校验、中间层的各种指标检查、底层的关键rpc原子接口。当然真实业务场景很复杂，需要仔细梳理各层的功能边界，及整体流程的调用链路

### 技术: 
#### 已实现：
树形的异常体系规划、及全局接口异常捕获、再配合多语言i18n进行提示

根据业务场景适当使用设计模式、不同层级的模型划分(输入参数param/输出参数dto,核心业务层bo,持久层po)

服务间公共的部分尽量放在sdk，达到复用效果，后续也方便扩展。

为了提高执行效率，可适当的根据硬件调整webserver的线程数及数据库连接池大小

针对账户这种修改频率较低的，使用缓存可提高效率、减轻db压力

对于交易流水号频繁判断场景，利用布隆过滤器增加效率(不存在则不需要查db)

对于余额更新在服务层要处理 查询到更新中间的冲突，使用db层方式解决。

#### 已简单示例:
最外层restful接口负责交互、但最底层使用rpc方式效率会更优

复杂场景数据库的事务应该从单机转为分布式事务

出于安全角度、调用方于服务端通信内容应该是加密的，服务端收到后统一解密再传给具体接口

#### 未实现的思考:
敏感属性在返回时需要脱敏

压力还是在db层，可以在同步交易与异步之间取折中，将多个请求的持久层操作在一定时间窗口内合并，用批量方式提升性能。



额外引用库说明:

<font style="color:#080808;background-color:#ffffff;">mybatis-plus: 数据库orm框架，简化开发复杂度</font>

<font style="color:#080808;background-color:#ffffff;">springboot: 提供webServer及bean管理、及其他公共、模板功能</font>

<font style="color:#080808;background-color:#ffffff;">springboot-cache&caffeine : 以内存方式实现缓存，提升效率，减轻db压力</font>

<font style="color:#080808;background-color:#ffffff;">guava: 使用现成的布隆过滤器</font>

