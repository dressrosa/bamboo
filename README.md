# bamboo
**简介**:
bamboo是基于springboot搭建的,主要是因为之前在用spring开发时觉得很麻烦,为了将自己平常开发过程的知识总结下来,于是搭了一个可用于快速开发接口的小项目.

---
**主要技术**:
- [springboot](https://github.com/spring-projects/spring-boot):
springboot是spring的简化版,非常适用于快速开发和微服务搭建,个人认为基于约定大于配置的原则非常的好,目前除非是工作必须,一般我只用springboot开发.
- [mybatis](https://github.com/mybatis/mybatis-3): 这里用的是mybatis的xml配置,个人感觉配置mybatis还是xml感觉方便,sql写起来也比较简洁,如果非要用代码写的话也无可厚非
- [redis](https://github.com/xetorthio/jedis)这里用的是jedis.jedis是redis的java实现版.这里用于对接口的权限拦截和访问限制
- [pageHelper ](https://github.com/pagehelper/Mybatis-PageHelper)一款开源的用于mysql的分页插件,用法很简单,详细的可查看其github官网.这里的配置写在mybatis-config.xml里面,可自行调节
- 日志用的是logback,网上说的很快,其实觉得log4j也差不多,习惯用哪个就用哪个.配置在logback.xml里面,可自行调节

**代码结构**:
######  common:
- configuration:一些基础的如数据库,事务等的配置
- base:放一些全局的类,如数据的封装和基类
- utils:主要是日常开发中常用util类 
###### modules:
- biz:用于写业务代码
- controller:用于写接口类
###### mappers:
用于放置mybatis的xml类
###### application.properties:
全局配置,如端口,mysql地址
###### banner.txt:
启动时的欢迎语,我主要喜欢放一些自己喜欢的诗词等
###### logback.xml:
日志的配置
###### mybatis-config.xml:
mybatis的配置

---

**用法**:
在biz里面建立自己的业务包,分别建好dao,entity,service包,并建好对应类.

User:

```
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private String loginName; // 姓名
    private String password; // 登录密码

    public String getLoginName() {
        return loginName;
    }

    public User setLoginName(String loginName) {
        this.loginName = loginName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

}
```
在数据库设计时一般都有共同的字段,比如create_date,update_date,这时候可以提取出来放在BaseEntity里面.

UserDao:
```
@Repository
public interface UserDao extends BaseDao<User> {

}
```
这里BaseDao里面放置了基于泛型的基础的增删查改

UserService:

```
@Service
@Transactional
public class UserService extends BaseService<UserDao, User> {

    private Map<String, Object> user2Map(User u) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("userId", u.getId());
        map.put("name", u.getLoginName());
        map.put("password", u.getPassword());
        return map;
    }

    public String userInfo(HttpServletResponse response, String userId) {
        ResponseMapper mapper = ResponseMapper.createMapper();
        User u = new User();
        u.setId(userId);
        User user = this.get(u);

        mapper.setDatas(this.user2Map(user));
        return mapper.getResultJson();
    }
}
```
service 开启事务需要加注解@Transactional. BaseService提取了共有的增删查改,可以重写,也可以不继承.
这里的service层可以设计成接口的形式,如UserService和UserServiceImpl,从而可以用于构建dubbo搭建的微服务设计.

UserController:

```
@RestController
public class UserController {

    protected Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("public/xiaoyu/users/login/v1")
    public String login(HttpServletRequest request, HttpServletResponse response, String loginName, String pwd) {
        ResponseMapper mapper = ResponseMapper.createMapper();
        try {
            return this.userService.login(response, loginName, pwd);
        } catch (Exception e) {
            return mapper.setCode(ResultConstant.EXCEPTION).getResultJson();
        }
    }
```

controller坚决不要写业务代码,可以用来对数据合法性的检测.

UserDao.xml:


```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.xiaoyu.modules.biz.users.dao.UserDao">

    <sql id="userColumns">
        a.id AS "id",
        a.login_name AS "loginName",
        a.password as
        "password",
        a.create_date AS "createDate"
    </sql>

    <select id="get" resultType="com.xiaoyu.modules.biz.users.entity.User">
        SELECT
        <include refid="userColumns" />
        FROM biz_user a
        where 1 = 1
        <if test="loginName !=null">
            and a.login_name = #{loginName}
        </if>
        <if test="id !=null">
            and a.id=#{id}
        </if>
        <if test="password !=null">
            and a.password=#{password}
        </if>
    </select>
</mapper>
```
其中namespace填写dao层的全路径,resultType写返回的类型,一般返回entity就行了,也可以返回map等,具体可以去学习mybatis的用法

**部署**:
- 打包:eclipse里面右键run as : maven build :命令是package,打包默认存放在项目底下的target里面
- 运行:linux下 
    nohup java -jar xxx.jar &
运行后,会默认在当前文件夹下生成nohup.out文件,用于存储日志

- 打印日志:
    tail -f nohup.out

**请求**:
http://host:port/xxx/xx 格式就行了

**遇见的问题**:

jar包部署运行时有个问题:
发现mybatis dao层的xml里面无法正常映射到实体类,只能采用全路径的形式.但是本地运行却没错,
如果你看到了这里,知道的话请告诉我怎么解决,谢谢!

there is a question when running the jar:
the xml for the dao cannot find the entity,only the full path of the entity can,but it is normal when in the eclipse of my computer.
if you find here and just know the solution, please tell me,thanks!


```
<select id="get" resultType="User">
        SELECT
        <include refid="userColumns" />
        FROM biz_user a
        where 1 = 1
        <if test="loginName !=null">
        and a.login_name = #{loginName}
        </if>
        <if test="id !=null">
        and a.id=#{id}      
        </if>
        <if test="password !=null">
        and a.password=#{password}
        </if>
    </select>
```

返回类型必须全路径才能找到:

only the resultType is the full path can map the entity

```
<select id="get" resultType="com.xiaoyu.modules.biz.users.entity.User">
        SELECT
        <include refid="userColumns" />
        FROM biz_user a
        where 1 = 1
        <if test="loginName !=null">
        and a.login_name = #{loginName}
        </if>
        <if test="id !=null">
        and a.id=#{id}      
        </if>
        <if test="password !=null">
        and a.password=#{password}
        </if>
    </select>
```




---
项目主要用于总结学习,很简单也很实用.

 by xiaoyu
