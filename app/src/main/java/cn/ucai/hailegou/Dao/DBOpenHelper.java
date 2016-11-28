package cn.ucai.hailegou.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.ucai.hailegou.utils.I;

/**
 * Created by Administrator on 2016/10/24.
 */

public class DBOpenHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_USER_TABLE = "create table "
            +UserAvatarDao.USER_TABLE+" ("
            +UserAvatarDao.USER_NAME+" text primary key, "
            +UserAvatarDao.USER_NICK+" text, "
            +UserAvatarDao.USER_AVATAR_ID+" integer, "
            +UserAvatarDao.USER_AVATAR_PATH+" text, "
            +UserAvatarDao.USER_AVATAR_SUFFIX+" text, "
            +UserAvatarDao.USER_AVATAR_TYPE+" integer, "
            +UserAvatarDao.USER_AVATAR_LASTUPDATETIME+")";
    private static DBOpenHelper instance;

    public DBOpenHelper(Context context) {
        super(context,getUserDataBaseName(), null, DATABASE_VERSION);
    }
    public static DBOpenHelper getInstance(Context context){
        if(instance == null){
            instance = new DBOpenHelper(context.getApplicationContext());
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static String getUserDataBaseName() {
        return I.User.TABLE_NAME+"_demo.db";
    }
    public void closeDB(){
        if(instance!=null){
            SQLiteDatabase db = getWritableDatabase();
            db.close();
            instance = null;
        }
    }
}
