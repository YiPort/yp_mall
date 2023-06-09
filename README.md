<p align="center">
  <h3 align="center">YiPortMall</h3>
  <p align="center">
💥该项目基于SpringCloud 微服务💥
    <br/> 🌟这是一个在线交易平台🌟
    <br/>
    <a href="https://github.com/YiPort/yp_mall"><strong>交易平台后端项目 »</strong></a><br/>  
    <a href="https://github.com/YiPort/yp_mall_frontend"><strong>交易平台前端项目 »</strong></a>
    <br/>
  </p>

---

### 一、架构图

![img](assets/yp_001.png)


### 二、包含的微服务

![img](assets/yp_wfw.png)


### 2.1 网关微服务

> 架构图

![img](assets/yp_002.png)


不管是来自于客户端（PC或移动端）的请求，还是服务内部调用。一切对服务的请求都会经过Zuul这个网关，然后再由网关来实现 鉴权、动态路由等等操作。Zuul就是我们服务的统一入口。

> 配置

```zuul
zuul:
  prefix: /api
  routes:
    item-service: /item/**  #商品微服务的映射路径
    search-service: /search/** #搜索微服务
    user-service: /user/** #用户微服务
    auth-service: /auth/** # 授权中心微服务
    cart-service: /cart/** #购物车微服务
  add-host-header: true #携带请求本身的head头信息
  sensitive-headers:  #配置禁止使用的头信息，这里设置为null,否则set-cookie无效
```

服务网关是微服务架构中一个不可或缺的部分。通过服务网关统一向外系统提供REST API的过程中，除了具备服务路由、均衡负载功能之外，它还具备了`权限控制`等功能。为微服务架构提供了前门保护的作用，同时将权限控制这些较重的非业务逻辑内容迁移到服务路由层面，使得服务集群主体能够具备更高的可复用性和可测试性。

> 主要功能

身份认证与安全：识别每个资源的验证要求，并拒绝那些与要求不相符的请求。（对jwt鉴权）

动态路由：动态地将请求路由到不同的后端集群。

负载均衡和熔断

### 2.2 授权中心微服务

> 结合RSA的鉴权

![img](assets/yp_003.png)

- 首先利用RSA生成公钥和私钥。私钥保存在授权中心，公钥保存在Zuul和各个微服务
- 用户请求登录
- 授权中心校验，通过后用私钥对JWT进行签名加密
- 返回jwt给用户
- 用户携带JWT访问
- Zuul直接通过公钥解密JWT，进行验证，验证通过则放行
- 请求到达微服务，微服务直接用公钥解析JWT，获取用户信息，无需访问授权中心

> 授权中心的主要职责

1. 用户鉴权：接收用户的登录请求，通过用户中心的接口进行校验，通过后生成JWT。使用私钥生成JWT并返回
2. 服务鉴权：微服务间的调用不经过Zuul，会有风险，需要鉴权中心进行认证。原理与用户鉴权类似，但逻辑稍微复杂一些（未实现）。

### 2.3 购物车微服务

> 功能需求

- 用户可以在登录状态下将商品添加到购物车

放入数据库

放入redis（采用）

- 用户可以在未登录状态下将商品添加到购物车
- 放入localstorage
- 用户可以使用购物车一起结算下单
- 用户可以查询自己的购物车
- 用户可以在购物车中修改购买商品的数量。
- 用户可以在购物车中删除商品。
- 在购物车中展示商品优惠信息
- 提示购物车商品价格变化

> 流程图

![img](assets/yp_004.png)


这幅图主要描述了两个功能：新增商品到购物车、查询购物车。

- 新增商品：

  - 判断是否登录
    - 是：则添加商品到后台Redis中
    - 否：则添加商品到本地的Localstorage

- 无论哪种新增，完成后都需要查询购物车列表：

  - 判断是否登录
    - 否：直接查询localstorage中数据并展示
    - 是：已登录，则需要先看本地是否有数据，
      - 有：需要提交到后台添加到redis，合并数据，而后查询
      - 否：直接去后台查询redis，而后返回

### 2.4 配置中心微服务

> 需求

在分布式系统中，由于服务数量巨多，为了方便服务配置文件统一管理，实时更新，所以需要分布式配置中心组件。在Spring Cloud中，有分布式配置中心组件spring cloudconfig ，它支持配置服务放在配置服务的内存中（即本地），也支持放在远程Git仓库中。

使用SpringCloudBus来实现配置的自动更新。

> 组成结构

在spring cloud config 组件中，分两个角色，一是config server，二是config client。

Config Server是一个可横向扩展、集中式的配置服务器，它用于集中管理应用程序各个环境下的配置，默认使用Git存储配置文件内容，也可以使用SVN存储，或者是本地文件存存储

Config Client是Config Server的客户端，用于操作存储在Config Server中的配置内容。微服务在启动时会请求Config Server获取配置文件的内容，请求到后再启动容器

> 实现

创建配置中心，对Config Server进行配置，然后在其它微服务中配置Config Client。最后使用Github上的Webhooks进行配置的动态刷新，所以还要使用内网穿透工具，同时要在配置中心中添加过滤器，因为使用Webhooks提交请求时会加上一段Payload，而本地是无法解析这个Payload的，所以要将它过滤掉。

### 2.5 页面静态化微服务

商品详情浏览量比较大，并发高，所以单独开启一个微服务用来展示商品详情，并且对其进行静态化处理，保存为静态html文件。在用户访问商品详情页面时，让nginx对商品请求进行监听，指向本地静态页面，如果本地没找到，才反向代理到页面详情微服务端口。 

### 2.6 后台管理微服务

主要是对商品分类、品牌、商品的规格参数以及商品的CRUD，为后台管理提供各种接口。 


### 2.7 注册中心

> 基本架构

![img](assets/yp_005.png)


- Eureka：就是服务注册中心（可以是一个集群），对外暴露自己的地址
- 提供者：启动后向Eureka注册自己信息（地址，提供什么服务）
- 消费者：向Eureka订阅服务，Eureka会将对应服务的所有提供者地址列表发送给消费者，并且定期更新
- 心跳(续约)：提供者定期通过http方式向Eureka刷新自己的状态

主要功能就是对各种服务进行管理。

### 2.8 搜索微服务

主要是对Elasticsearch的应用，将所有商品数据封装好后添加到Elasticsearch的索引库中，然后进行搜索过滤，查询相应的商品信息。 


### 2.9 短信微服务

因为系统中不止注册一个地方需要短信发送，因此将短信发送抽取为微服务：`yiport-sms-service`，凡是需要的地方都可以使用。

另外，因为短信发送API调用时长的不确定性，为了提高程序的响应速度，短信发送我们都将采用异步发送方式，即：

- 短信服务监听MQ消息，收到消息后发送短信。
- 其它服务要发送短信时，通过MQ通知短信微服务。

### 2.10 文件上传微服务

使用分布式文件系统FastDFS实现图片上传。

> FastDFS架构

![img](assets/yp_006.png)


FastDFS两个主要的角色：Tracker Server 和 Storage Server 。

- Tracker Server：跟踪服务器，主要负责调度storage节点与client通信，在访问上起负载均衡的作用，和记录storage节点的运行状态，是连接client和storage节点的枢纽。
- Storage Server：存储服务器，保存文件和文件的meta data（元数据），每个storage server会启动一个单独的线程主动向Tracker cluster中每个tracker server报告其状态信息，包括磁盘使用情况，文件同步情况及文件上传下载次数统计等信息
- Group：文件组，多台Storage Server的集群。上传一个文件到同组内的一台机器上后，FastDFS会将该文件即时同步到同组内的其它所有机器上，起到备份的作用。不同组的服务器，保存的数据不同，而且相互独立，不进行通信。
- Tracker Cluster：跟踪服务器的集群，有一组Tracker Server（跟踪服务器）组成。
- Storage Cluster ：存储集群，有多个Group组成。

> 上传流程

![img](assets/yp_006up.png)


1. Client通过Tracker server查找可用的Storage server。
2. Tracker server向Client返回一台可用的Storage server的IP地址和端口号。
3. Client直接通过Tracker server返回的IP地址和端口与其中一台Storage server建立连接并进行文件上传。
4. 上传完成，Storage server返回Client一个文件ID，文件上传结束。

> 下载流程

![img](assets/yp_006down.png)


1. Client通过Tracker server查找要下载文件所在的的Storage server。
2. Tracker server向Client返回包含指定文件的某个Storage server的IP地址和端口号。
3. Client直接通过Tracker server返回的IP地址和端口与其中一台Storage server建立连接并指定要下载文件。
4. 下载文件成功。

### 2.11 用户中心微服务

提供的接口：

- 检查用户名和手机号是否可用
- 发送短信验证码
- 用户注册
- 用户查询

### 三、如何启动项目

在虚拟机中进行以下中间件的配置：

- ES：搜索
- FDFS：文件上传
- nginx：代理FDFS中的图片及静态图片
- Rabbitmq：数据同步
- Redis：缓存

并将配置文件中所有和虚拟机相关的ip进行修改

本机中需要的配置：

- nginx：前端所有请求统一代理到网关，域名的反向代理
- host：实现域名访问（详情查看，请和Nginx文件搭配使用）
```host
127.0.0.1 manage.yiport.cn
127.0.0.1 api.yiport.cn 
192.168.10.123 image.yiport.cn  #FastDFS服务器地址
127.0.0.1 www.yiport.cn
127.0.0.1 yiport.cn 
127.0.0.1 www.yiport.cn 
```

### 四、数据库文件
[SQL文件传送门](sql/yp_mall.sql)

### 五、Nginx配置文件

[Nginx配置文件传送门](assets/nginx.conf)