package cn.edu.sicau.pfdistribution.dao;


        import cn.edu.sicau.pfdistribution.entity.User;

        import java.util.List;
/**
 * @ClassName cn.saytime.dao.UserDao
 * @Description
 * @date 2017-07-04 22:48:45
 */
public interface UserDao {
    User getUserById(Integer id);
    public List<User> getUserList();
    public int add(User user);
    public int update(Integer id, User user);
    public int delete(Integer id);
}

