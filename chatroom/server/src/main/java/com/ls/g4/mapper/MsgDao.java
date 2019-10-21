package com.ls.g4.mapper;

import com.ls.g4.po.Message;
import com.ls.g4.po.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MsgDao {
  /***
   * p：添加一条消息
   * @param message
   */
  @Insert("INSERT INTO msg (senderName,receiverName,msg,sendTime,status) "
         +"VALUES (#{message.senderName}, #{message.receiverName}, #{message.msg}, #{message.time},#{message.status})")
  @Options(useGeneratedKeys=true, keyProperty="message.id", keyColumn="id")
  void addMsg(@Param("message") Message message);

  /**
   * 查询消息
   *
   * @param u1
   * @param u2
   * @param count 查询历史记录条数
   * @return 历史消息
   */
@Select("SELECT id,senderName,receiverName,msg,sendTime,status FROM msg "
    + "WHERE senderName=#{u1.name} AND receiverName=#{u2.name} "
    + "OR senderName=#{u2.name} AND receiverName=#{u1.name} "
    + "ORDER BY sendTime DESC LIMIT 0,#{count}")
@Results(
    @Result(property = "time",column = "sendTime")
)
  List<Message> queryMessages(@Param("u1") User u1,@Param("u2") User u2, @Param("count") int count);

  /**
   *把message的status（默认0）改为1
   * @param id
   */
  @Update("update msg set status = 1 WHERE id = #{id}")
  void updateMsg(@Param("id") int id);

}
