package cn.edu.sicau.pfdistribution.entity;

import java.util.Date;
/**
 * @ClassName cn.saytime.bean.User
 * @Description
 * @date 2017-07-04 22:47:28
 */
public class User {

    private int id;
    private String username;
    private int age;
    private Date ctm;

    public User() {
    }
    public User(String username, int age) {
        this.username = username;
        this.age = age;
        this.ctm = new Date();
    }

    public String getUsername() {
        return this.username;
    }

    public int getAge() {
        return this.age;
    }

    // Getter、Setter
}
