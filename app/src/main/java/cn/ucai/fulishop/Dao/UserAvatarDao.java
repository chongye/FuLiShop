package cn.ucai.fulishop.Dao;

import android.content.Context;

import bean.UserAvatar;

/**
 * Created by Administrator on 2016/10/24.
 * String sql = "create table "+TB_USER+"("
 +"id integer primary key autoincrement,"
 +"muserName varchar,"
 +"muserNick varchar,"
 +"mavatarId integer,"
 +"mavatarPath varchar,"
 +"mavatarSuffix varchar,"
 +"mavatarType integer,"
 +"mavatarLastUpdateTime varchar)";
 */

public class UserAvatarDao {
    public static final String USER_TABLE = "t_superweb_user";
    public static final String USER_NAME = "m_user_name";
    public static final String USER_NICK = "m_user_nick";
    public static final String USER_AVATAR_ID = "m_avatar_id";
    public static final String USER_AVATAR_PATH = "m_avatar_path";
    public static final String USER_AVATAR_SUFFIX = "m_avatar_suffix";
    public static final String USER_AVATAR_TYPE = "m_avatar_type";
    public static final String USER_AVATAR_LASTUPDATETIME = "m_avatar_lastupdatetime";

    public UserAvatarDao(Context context){
        DBManager.getInstance().onInit(context);
    }
    public boolean saveUserAvatar(UserAvatar user){
        return DBManager.getInstance().saveUserData(user);
    }
    public UserAvatar getUserAvatar(String username){
        return DBManager.getInstance().getUser(username);
    }
    public boolean updateUserAvatar(UserAvatar user){
        return DBManager.getInstance().updateUser(user);
    }
    public boolean deleteUserAvatar(UserAvatar user){
        return DBManager.getInstance().deletUser(user);
    }
}
