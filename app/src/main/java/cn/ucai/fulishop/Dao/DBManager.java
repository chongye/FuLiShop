package cn.ucai.fulishop.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import bean.UserAvatar;

/**
 * Created by Administrator on 2016/10/24.
 */

public class DBManager {
    private static DBManager dbMgr = new DBManager();
    private static DBOpenHelper mHelper;
    public DBManager(){
    }
    public static synchronized DBManager getInstance(){
        return dbMgr;
    }

    void onInit(Context context){
            mHelper = new DBOpenHelper(context);
    }

    public synchronized void closeDB(Context context){
        if(mHelper!=null){
            mHelper.closeDB();
        }
    }
    public synchronized boolean saveUserData(UserAvatar user){
        SQLiteDatabase database = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserAvatarDao.USER_NAME,user.getMuserName());
        values.put(UserAvatarDao.USER_NICK,user.getMuserNick());
        values.put(UserAvatarDao.USER_AVATAR_ID,user.getMavatarId());
        values.put(UserAvatarDao.USER_AVATAR_PATH,user.getMavatarPath());
        values.put(UserAvatarDao.USER_AVATAR_SUFFIX,user.getMavatarSuffix());
        values.put(UserAvatarDao.USER_AVATAR_TYPE,user.getMavatarType());
        values.put(UserAvatarDao.USER_AVATAR_LASTUPDATETIME,user.getMavatarLastUpdateTime());
        if(database.isOpen()){
            return database.replace(UserAvatarDao.USER_TABLE,null,values)!=-1;
        }
        return false;
    }
    public UserAvatar getUser(String userName){
        String sql = "select*from "+UserAvatarDao.USER_TABLE+" where "+UserAvatarDao.USER_NAME+" = ?";
        SQLiteDatabase database = mHelper.getReadableDatabase();
        Cursor cs = database.rawQuery(sql, new String[]{userName});
        UserAvatar user = null;
        if(cs.moveToNext()){
            user = new UserAvatar();
            user.setMuserName(cs.getString(cs.getColumnIndex(UserAvatarDao.USER_NAME)));
            user.setMuserNick(cs.getString(cs.getColumnIndex(UserAvatarDao.USER_NICK)));
            user.setMavatarId(cs.getInt(cs.getColumnIndex(UserAvatarDao.USER_AVATAR_ID)));
            user.setMavatarPath(cs.getString(cs.getColumnIndex(UserAvatarDao.USER_AVATAR_PATH)));
            user.setMavatarType(cs.getInt(cs.getColumnIndex(UserAvatarDao.USER_AVATAR_TYPE)));
            user.setMavatarSuffix(cs.getString(cs.getColumnIndex(UserAvatarDao.USER_AVATAR_SUFFIX)));
            user.setMavatarLastUpdateTime(cs.getString(cs.getColumnIndex(UserAvatarDao.USER_AVATAR_LASTUPDATETIME)));
            return user;
        }
        return user;
    }
    public synchronized boolean updateUser(UserAvatar user){
        int result = -1;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        String sql = UserAvatarDao.USER_NICK +" = ?";
        ContentValues values = new ContentValues();
        values.put(UserAvatarDao.USER_NICK,user.getMuserNick());
        if(database.isOpen()){
            result = database.update(UserAvatarDao.USER_TABLE,values,sql,new String[]{user.getMuserName()});
        }
        return result>0;
    }
}
