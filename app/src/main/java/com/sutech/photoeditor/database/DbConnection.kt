package com.sutech.photoeditor.database

import android.content.Context
import com.sutech.photoeditor.widget.text.database.DataBase
import java.io.IOException

/**
 * Class connects to Database, get all data here
 */
class DbConnection(mContext: Context) : IDatabaseConfig {
    private val mDBHelper: DataBase = DataBase(mContext, IDatabaseConfig.DATABASE_NAME)

    /**
     * close database
     */
    private fun closeDB() {
        mDBHelper.close()
    }

    /**
     * open and connect to database
     */
    private fun openAndConnectDB() {
        try {
            mDBHelper.createDataBase()
            mDBHelper.openDataBase()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun execSQL(sqlQuery: String) {
        mDBHelper.getmDatabase()!!.execSQL(sqlQuery)
    } //    //get by name

//
//    fun saveDataNote(noteDb: NoteObj): Long {
//        synchronized(this) {
//            openAndConnectDB()
//            val values = ContentValues().apply {
//                put(DataBaseUtil.ID, noteDb.id)
//                put(DataBaseUtil.TITLE, noteDb.title)
//                put(DataBaseUtil.CONTENT, noteDb.content)
//                put(DataBaseUtil.CONTENT, noteDb.createDate)
//                put(DataBaseUtil.CONTENT, noteDb.modifiedDate)
//                put(DataBaseUtil.CONTENT, noteDb.dateTime)
//                put(DataBaseUtil.CONTENT, noteDb.color)
//                put(DataBaseUtil.CONTENT, noteDb.type)
//                put(DataBaseUtil.CONTENT, noteDb.timeAlarm)
//                put(DataBaseUtil.CONTENT, noteDb.alarm)
//            }
//            val saveSuccess = mDBHelper.getmDatabase()!!.insertWithOnConflict(
//                DataBaseUtil.TABLE_NOTE, null,
//                values, SQLiteDatabase.CONFLICT_REPLACE
//            )
//            closeDB()
//
//            return saveSuccess
//        }
//    }
//
//    fun saveImage(image: ImageGalleryObj): Long {
//        synchronized(this) {
//            openAndConnectDB()
//            val values = ContentValues().apply {
//                put(DataBaseUtil.ID, image.id)
//                put(DataBaseUtil.PATH_IMAGE, image.pathImage)
//            }
//            val saveSuccess = mDBHelper.getmDatabase()!!.insertWithOnConflict(
//                DataBaseUtil.TABLE_IMAGE, null,
//                values, SQLiteDatabase.CONFLICT_REPLACE
//            )
//            closeDB()
//
//            return saveSuccess
//        }
//    }
//
//
//    fun saveSound(sound: SoundObj): Long {
//        synchronized(this) {
//            openAndConnectDB()
//            val values = ContentValues().apply {
//                put(DataBaseUtil.ID, sound.id)
//                put(DataBaseUtil.PATH_SOUND, sound.pathSound)
//                put(DataBaseUtil.TITLE, sound.title)
//            }
//            val saveSuccess = mDBHelper.getmDatabase()!!.insertWithOnConflict(
//                DataBaseUtil.TABLE_SOUND, null,
//                values, SQLiteDatabase.CONFLICT_REPLACE
//            )
//            closeDB()
//
//            return saveSuccess
//        }
//
//    }


    //    public ArrayList<AddressObj> getAllAddress(String key_search) {
    //        ArrayList<AddressObj> arrAddress = new ArrayList<>();
    //        String queryTinh = "SELECT * FROM " + TB_TINH + " WHERE " + KEY_NAME + " LIKE  '%" + key_search + "%'  COLLATE NOCASE LIMIT 100 ";
    //        String queryPhu = "SELECT * FROM " + TB_PHU + " WHERE " + KEY_NAME + " LIKE '%" + key_search + "%'   COLLATE NOCASE LIMIT 100 ";
    //        String queryHuyen = "SELECT * FROM " + TB_HUYEN + " WHERE " + KEY_NAME + " LIKE '%" + key_search + "%'  COLLATE NOCASE LIMIT 100 ";
    //        String queryTong = "SELECT * FROM " + TB_TONG + " WHERE " + KEY_NAME + " LIKE '%" + key_search + "%'  COLLATE NOCASE LIMIT 100 ";
    //        String queryXa = "SELECT * FROM " + TB_XA + " WHERE " + KEY_NAME + " LIKE '%" + key_search + "%'  COLLATE NOCASE LIMIT 100 ";
    //
    //        arrAddress.addAll(searchTinh(queryTinh));
    //        arrAddress.addAll(searchHuyen(queryHuyen));
    //        arrAddress.addAll(searchPhu(queryPhu));
    //        arrAddress.addAll(searchTong(queryTong));
    //        arrAddress.addAll(searchXa(queryXa));
    //        return arrAddress;
    //    }
    //
    //    public ArrayList<AddressObj> getTinh(String key_search) {
    //        return searchTinh("SELECT * FROM " + TB_TINH + " WHERE " + KEY_NAME + " LIKE '%" + key_search + "%' ORDER BY SUBSTR(name, 5) COLLATE LOCALIZED ASC LIMIT 500 ");
    //    }
    //
    //    public ArrayList<AddressObj> getPhu(String key_search) {
    //        return searchPhu("SELECT * FROM " + TB_PHU + " WHERE " + KEY_NAME + " LIKE '%" + key_search + "%'  ORDER BY SUBSTR(name, 4) LIMIT 500 ");
    //    }
    //
    //    public ArrayList<AddressObj> getHuyen(String key_search) {
    //        return searchHuyen("SELECT * FROM " + TB_HUYEN + " WHERE " + KEY_NAME + " LIKE '%" + key_search + "%'  ORDER BY SUBSTR(name, 6) LIMIT 500 ");
    //    }
    //
    //    public ArrayList<AddressObj> getTong(String key_search) {
    //        return searchTong("SELECT * FROM " + TB_TONG + " WHERE " + KEY_NAME + " LIKE '%" + key_search + "%'  ORDER BY SUBSTR(name, 5) LIMIT 500 ");
    //    }
    //
    //    public ArrayList<AddressObj> getXa(String key_search) {
    //        return searchXa("SELECT * FROM " + TB_XA + " WHERE " + KEY_NAME + " LIKE '%" + key_search + "%'  ORDER BY SUBSTR(name, 3) LIMIT 500 ");
    //    }
    //
    //    public ArrayList<AddressObj> getTinh(String key, int key_search) {
    //        return searchTinh("SELECT * FROM " + TB_TINH + " WHERE " + key + " LIKE '%" + key_search + "%'");
    //    }
    //
    //    public ArrayList<AddressObj> getPhu(String key, int key_search) {
    //        return searchPhu("SELECT * FROM " + TB_PHU + " WHERE " + key + " =  " + key_search + "");
    //    }
    //
    //    public ArrayList<AddressObj> getHuyen(String key, int key_search) {
    //        return searchHuyen("SELECT * FROM " + TB_HUYEN + " WHERE " + key + " =  " + key_search + "");
    //    }
    //
    //    public ArrayList<AddressObj> getTong(String key, int key_search) {
    //        return searchTong("SELECT * FROM " + TB_TONG + " WHERE " + key + " =  " + key_search + "");
    //    }
    //
    //    public ArrayList<AddressObj> getXa(String key, int key_search) {
    //        return searchXa("SELECT * FROM " + TB_XA + " WHERE " + key + " =  " + key_search + "");
    //    }
    //
    //
    //    //    search exist
    //    public ArrayList<AddressObj> searchPhuExits(String name, int id_parent) {
    //        return searchPhu("SELECT * FROM " + TB_PHU + " WHERE " + KEY_NAME + " = '" + name + "' AND " + KEY_ID_TINH + " = " + id_parent);
    //    }
    //
    //    public ArrayList<AddressObj> searchHuyenExits(String name, int id_parent) {
    //        return searchHuyen("SELECT * FROM " + TB_HUYEN + " WHERE " + KEY_NAME + " = '" + name + "' AND " + KEY_ID_PHU + " = " + id_parent);
    //    }
    //
    //    public ArrayList<AddressObj> searchTongExits(String name, int id_parent) {
    //        return searchTong("SELECT * FROM " + TB_TONG + " WHERE " + KEY_NAME + " = '" + name + "' AND " + KEY_ID_HUYEN + " = " + id_parent);
    //    }
    //
    //    public ArrayList<AddressObj> searchXaExits(String name, int id_parent) {
    //        return searchXa("SELECT * FROM " + TB_XA + " WHERE " + KEY_NAME + " = '" + name + "' AND " + KEY_ID_TONG + " = " + id_parent);
    //    }
    //
    //
    //    private ArrayList<AddressObj> searchTinh(String query) {
    //        this.openAndConnectDB();
    //        ArrayList<AddressObj> arrAddress = new ArrayList<>();
    //        Cursor mCursorTinh = this.mDBHelper.rawQuery(query);
    //
    //        int index_id = mCursorTinh.getColumnIndex(KEY_ID);
    //        int index_name = mCursorTinh.getColumnIndex(KEY_NAME);
    //        int index_mo_ta = mCursorTinh.getColumnIndex(KEY_MO_TA_NGAN);
    //        AddressObj addressObj;
    //        //tinh
    //        if (mCursorTinh.moveToFirst()) {
    //            do {
    //                addressObj = new AddressObj();
    //                addressObj.setId(mCursorTinh.getInt(index_id));
    //                addressObj.setName(mCursorTinh.getString(index_name));
    //                addressObj.setMoTa(mCursorTinh.getString(index_mo_ta));
    //                addressObj.setType("tinh");
    //                arrAddress.add(addressObj);
    //            } while (mCursorTinh.moveToNext());
    //        }
    //        closeDB();
    //        return arrAddress;
    //    }
    //
    //    private ArrayList<AddressObj> searchPhu(String query) {
    //        this.openAndConnectDB();
    //        ArrayList<AddressObj> arrAddress = new ArrayList<>();
    //
    //        Cursor mCursorPhu = this.mDBHelper.rawQuery(query);
    //
    //        int index_id = mCursorPhu.getColumnIndex(KEY_ID);
    //        int index_name = mCursorPhu.getColumnIndex(KEY_NAME);
    //        int index_mo_ta = mCursorPhu.getColumnIndex(KEY_MO_TA_NGAN);
    //        int index_id_tinh = mCursorPhu.getColumnIndex(KEY_ID_TINH);
    //
    //
    //        AddressObj addressObj;
    //
    //        //phu
    //        if (mCursorPhu.moveToFirst()) {
    //            do {
    //                addressObj = new AddressObj();
    //                addressObj.setId(mCursorPhu.getInt(index_id));
    //                addressObj.setName(mCursorPhu.getString(index_name));
    //                addressObj.setId_tinh(mCursorPhu.getInt(index_id_tinh));
    //                addressObj.setMoTa(mCursorPhu.getString(index_mo_ta));
    //                addressObj.setType("phu");
    //
    //                arrAddress.add(addressObj);
    //            } while (mCursorPhu.moveToNext());
    //        }
    //
    //
    //        closeDB();
    //        return arrAddress;
    //    }
    //
    //    private ArrayList<AddressObj> searchHuyen(String query) {
    //        this.openAndConnectDB();
    //        ArrayList<AddressObj> arrAddress = new ArrayList<>();
    //        Cursor mCursorHuyen = this.mDBHelper.rawQuery(query);
    //
    //        int index_id = mCursorHuyen.getColumnIndex(KEY_ID);
    //        int index_name = mCursorHuyen.getColumnIndex(KEY_NAME);
    //        int index_id_phu = mCursorHuyen.getColumnIndex(KEY_ID_PHU);
    //        int index_mo_ta = mCursorHuyen.getColumnIndex(KEY_MO_TA_NGAN);
    //
    //
    //        AddressObj addressObj;
    //        //huyen
    //        if (mCursorHuyen.moveToFirst()) {
    //            do {
    //                addressObj = new AddressObj();
    //                addressObj.setId(mCursorHuyen.getInt(index_id));
    //                addressObj.setName(mCursorHuyen.getString(index_name));
    //                addressObj.setId_phu(mCursorHuyen.getInt(index_id_phu));
    //                addressObj.setMoTa(mCursorHuyen.getString(index_mo_ta));
    //                addressObj.setType("huyen");
    //
    //                arrAddress.add(addressObj);
    //            } while (mCursorHuyen.moveToNext());
    //        }
    //
    //        closeDB();
    //        return arrAddress;
    //    }
    //
    //    private ArrayList<AddressObj> searchTong(String query) {
    //        this.openAndConnectDB();
    //        ArrayList<AddressObj> arrAddress = new ArrayList<>();
    //        Cursor mCursorTong = this.mDBHelper.rawQuery(query);
    //
    //        int index_id = mCursorTong.getColumnIndex(KEY_ID);
    //        int index_name = mCursorTong.getColumnIndex(KEY_NAME);
    //        int index_mo_ta = mCursorTong.getColumnIndex(KEY_MO_TA_NGAN);
    //        int index_id_huyen = mCursorTong.getColumnIndex(KEY_ID_HUYEN);
    //
    //
    //        AddressObj addressObj;
    //        //tong
    //        if (mCursorTong.moveToFirst()) {
    //            do {
    //                addressObj = new AddressObj();
    //                addressObj.setId(mCursorTong.getInt(index_id));
    //                addressObj.setName(mCursorTong.getString(index_name));
    //                addressObj.setId_huyen(mCursorTong.getInt(index_id_huyen));
    //                addressObj.setMoTa(mCursorTong.getString(index_mo_ta));
    //                addressObj.setType("tong");
    //
    //                arrAddress.add(addressObj);
    //            } while (mCursorTong.moveToNext());
    //        }
    //
    //
    //        closeDB();
    //        return arrAddress;
    //    }
    //
    //    private ArrayList<AddressObj> searchXa(String query) {
    //        this.openAndConnectDB();
    //        ArrayList<AddressObj> arrAddress = new ArrayList<>();
    //        Cursor mCursorXa = this.mDBHelper.rawQuery(query);
    //
    //        int index_id = mCursorXa.getColumnIndex(KEY_ID);
    //        int index_name = mCursorXa.getColumnIndex(KEY_NAME);
    //        int index_mo_ta = mCursorXa.getColumnIndex(KEY_MO_TA_NGAN);
    //
    //        int index_id_tong = mCursorXa.getColumnIndex(KEY_ID_TONG);
    //
    //
    //        AddressObj addressObj;
    //
    //        //xa
    //        if (mCursorXa.moveToFirst()) {
    //            do {
    //                addressObj = new AddressObj();
    //                addressObj.setId(mCursorXa.getInt(index_id));
    //                addressObj.setName(mCursorXa.getString(index_name));
    //                addressObj.setId_tong(mCursorXa.getInt(index_id_tong));
    //                addressObj.setMoTa(mCursorXa.getString(index_mo_ta));
    //                addressObj.setType("xa");
    //
    //                arrAddress.add(addressObj);
    //            } while (mCursorXa.moveToNext());
    //        }
    //
    //        closeDB();
    //        return arrAddress;
    //    }
    //
    //    public long insertTinh(String name) {
    //        this.openAndConnectDB();
    //        synchronized (this) {
    //            openAndConnectDB();
    //            ContentValues contentValues = new ContentValues();
    //            contentValues.put(KEY_NAME, name);
    //            long result = mDBHelper.getmDatabase().insertWithOnConflict(TB_TINH, null,
    //                    contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    //            closeDB();
    //            return result;
    //
    //        }
    //    }
    //
    //    public long insertPhu(String name, String id_tinh) {
    //        this.openAndConnectDB();
    //        synchronized (this) {
    //            openAndConnectDB();
    //            ContentValues contentValues = new ContentValues();
    //            contentValues.put(KEY_NAME, name);
    //            contentValues.put(KEY_ID_TINH, id_tinh);
    //            long result = mDBHelper.getmDatabase().insertWithOnConflict(TB_PHU, null,
    //                    contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    //            closeDB();
    //
    //            return result;
    //        }
    //    }
    //
    //    public long insertHuyen(String name, String id_phu, String id_tinh) {
    //        this.openAndConnectDB();
    //        synchronized (this) {
    //            openAndConnectDB();
    //            ContentValues contentValues = new ContentValues();
    //            contentValues.put(KEY_NAME, name);
    //            contentValues.put(KEY_ID_PHU, id_phu);
    //            contentValues.put(KEY_ID_TINH, id_tinh);
    //            long result = mDBHelper.getmDatabase().insertWithOnConflict(TB_HUYEN, null,
    //                    contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    //            closeDB();
    //            return result;
    //        }
    //    }
    //
    //    public long insertTong(String name, String id_huyen, String id_phu, String id_tinh) {
    //        this.openAndConnectDB();
    //        synchronized (this) {
    //            openAndConnectDB();
    //            ContentValues contentValues = new ContentValues();
    //            contentValues.put(KEY_NAME, name);
    //            contentValues.put(KEY_ID_HUYEN, id_huyen);
    //            contentValues.put(KEY_ID_PHU, id_phu);
    //            contentValues.put(KEY_ID_TINH, id_tinh);
    //            long result = mDBHelper.getmDatabase().insertWithOnConflict(TB_TONG, null,
    //                    contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    //            closeDB();
    //            return result;
    //        }
    //    }
    //
    //    public long insertXa(String name, String id_tong, String id_huyen, String id_phu, String id_tinh) {
    //        this.openAndConnectDB();
    //        synchronized (this) {
    //            openAndConnectDB();
    //            ContentValues contentValues = new ContentValues();
    //            contentValues.put(KEY_NAME, name);
    //            contentValues.put(KEY_ID_TONG, id_tong);
    //            contentValues.put(KEY_ID_HUYEN, id_huyen);
    //            contentValues.put(KEY_ID_PHU, id_phu);
    //            contentValues.put(KEY_ID_TINH, id_tinh);
    //            long result = mDBHelper.getmDatabase().insertWithOnConflict(TB_XA, null,
    //                    contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    //            closeDB();
    //            return result;
    //        }
    //    }
    //
    //
    ////    public boolean updateNhatKy(ObjNhatKy objNhatKy) {
    ////        synchronized (this) {
    ////            openAndConnectDB();
    ////            ContentValues contentValues = new ContentValues();
    ////            contentValues.put(KEY_ID, objNhatKy.getId());
    ////            contentValues.put(KEY_NGAY_VIET, objNhatKy.getNgay_viet());
    ////            contentValues.put(KEY_NGAY_CAP_NHAT, objNhatKy.getNgay_cap_nhat());
    ////            contentValues.put(KEY_NOI_DUNG, objNhatKy.getNoi_dung());
    ////            long result = mDBHelper.getmDatabase().updateWithOnConflict(TB_NHAT_KY,
    ////                    contentValues, KEY_ID + "=" + objNhatKy.getId(), null, SQLiteDatabase.CONFLICT_REPLACE);
    ////            closeDB();
    ////            return result >= 0;
    ////        }
    ////    }
    ////
    ////    public boolean removeNhatKy(int id) {
    //////        synchronized (this) {
    ////        openAndConnectDB();
    ////        long result = mDBHelper.getmDatabase().delete(TB_NHAT_KY, KEY_ID + "=" + id, null);
    ////        closeDB();
    ////        return result >= 0;
    //////    }
    ////    }

}