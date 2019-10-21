package com.ls.g4.mapper;

import com.ls.g4.po.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {
  /**
   * 查询用户
   * @param user
   * @return user 返回满足条件的用户
   */
  @Select("select id,name,state,lastLogin from user where name=#{user.name} and pwd=#{user.pwd}")
  @Results(
      @Result(property = "loginTime",column = "lastLogin")
  )
  User queryUser(@Param("user") User user);

  /**
   * 更新user的loginTime
   * @param userId 用户id
   * @param lastLogin 上次登陆时间
   */
  @Update("update user set lastLogin=#{lastLogin} where id=#{userId}")
  void  updateLoginTime(@Param("userId") int userId,@Param("lastLogin") long lastLogin);


}
