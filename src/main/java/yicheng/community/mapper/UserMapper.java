package yicheng.community.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import yicheng.community.model.User;

import javax.annotation.Resource;

//@Component(value = "UserMapper")
@Mapper
//@Resource
public interface UserMapper {
    @Insert("insert into user (name, account_id, token, gmt_create, gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(String token);

    @Select("select * from user where id = #{id}")
    User findById(@Param("id")Integer id);
}
