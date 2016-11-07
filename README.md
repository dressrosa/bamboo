# frameForAppOrWeb
frame based on spring boot privides api data for app or web

基于springboot mybatis redis的一个提供json数据的框架,可以服务于app或者web端.
a frame based on spring boot,mybatis and redis provide json datas for app or web.

jar包部署运行时有个问题:
发现mybatis dao层的xml里面无法正常映射到实体类,只能采用全路径的形式.但是本地运行却没错,
如果你看到了这里,知道的话请告诉我怎么解决,谢谢!
there is a question when running the jar:
the xml for the dao cannot find the entity,only the full path of the entity can,but it is normal when in the eclipse of my computer.
if you find here and just know the solution, please tell me,thanks!

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
返回类型必须全路径才能找到:
only the resultType is the full path can map the entity
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
