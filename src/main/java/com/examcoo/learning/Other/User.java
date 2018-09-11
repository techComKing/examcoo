/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: User
 * Author:   lida
 * Date:     2018/9/10 13:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 * lida         2018/9/10 13:47     V1.0              描述
 */
package com.examcoo.learning.Other;

/**
 * 〈测试lombok插件〉<br>
 * 〈测试GenerateAllSetter插件〉
 *
 * @author lida
 * @create 2018/9/10
 * @since 1.0.0
 */
public class User {
    private String name;
    private int age;
    private String address;
    private String telphone;

    public User() {
    }

    public static void main(String[] args) {
        User user = new User();
        user.setName("");
        user.setAge(0);
        user.setAddress("");
        user.setTelphone("");


    }

    protected boolean canEqual(Object other) {
        return other instanceof User;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public String getAddress() {
        return this.address;
    }

    public String getTelphone() {
        return this.telphone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        final User other = (User) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        if (this.getAge() != other.getAge()) {
            return false;
        }
        final Object this$address = this.getAddress();
        final Object other$address = other.getAddress();
        if (this$address == null ? other$address != null : !this$address.equals(other$address)) {
            return false;
        }
        final Object this$telphone = this.getTelphone();
        final Object other$telphone = other.getTelphone();
        if (this$telphone == null ? other$telphone != null : !this$telphone.equals(other$telphone)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        result = result * PRIME + this.getAge();
        final Object $address = this.getAddress();
        result = result * PRIME + ($address == null ? 43 : $address.hashCode());
        final Object $telphone = this.getTelphone();
        result = result * PRIME + ($telphone == null ? 43 : $telphone.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "User(name=" + this.getName() + ", age=" + this.getAge() + ", address=" + this.getAddress() + ", telphone=" + this.getTelphone() + ")";
    }
}