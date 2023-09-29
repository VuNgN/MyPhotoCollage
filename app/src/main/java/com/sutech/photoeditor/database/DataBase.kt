package com.sutech.photoeditor.widget.text.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.sutech.photoeditor.database.IDatabaseConfig
import com.sutech.photoeditor.database.IDatabaseConfig.DATABASE_NAME
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DataBase(private val mContext: Context, private val mNameDb: String) :
    SQLiteOpenHelper(mContext, mNameDb, null, IDatabaseConfig.DATABASE_VERSION), IDatabaseConfig {
    private var mDatabase: SQLiteDatabase? = null
    private val DB_PATH: String


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_NOTE_TABLE)
        db.execSQL(CREATE_IMAGE_TABLE)
        db.execSQL(CREATE_SOUND_TABLE)

    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
//        //Xoá bảng cũ
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
//        //Tiến hành tạo bảng mới
        onCreate(db);
//        thêm cột vào bảng ở từng version cập nhật
//        if (oldVersion < 2) {
//            db.execSQL(DATABASE_ALTER_TEAM_1)
//        }
//        if (oldVersion < 3) {
//            db.execSQL(DATABASE_ALTER_TEAM_2)
//        }
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    @Throws(IOException::class)
    fun copyDataBase() {
        var mInput: InputStream? = null
        try {
            /**
             * Open your local db as the input stream
             */
            mInput = mContext.assets.open(mNameDb)
            /**
             * Path to the just created empty db
             */
            val outFileName = DB_PATH + mNameDb

            /**
             * Open the empty db as the output stream
             */
            val mOutput: OutputStream = FileOutputStream(outFileName)

            /**
             * transfer bytes from the inputfile to the outputfile
             */
            val buffer = ByteArray(1024)
            var length: Int
            while (mInput.read(buffer).also { length = it } > 0) {
                mOutput.write(buffer, 0, length)
            }
            mOutput.flush()
            mOutput.close()
            mInput.close()
            Log.i(TAG, "New database created...")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private fun checkDataBase(): Boolean {
        var checkDB: SQLiteDatabase? = null
        try {
            val myPath = DB_PATH + mNameDb
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY)
        } catch (e: SQLiteException) {
            Log.v(TAG, "Database not exist")
            e.printStackTrace()
        }
        checkDB?.close()
        return checkDB != null
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    @Throws(IOException::class)
    fun createDataBase() {
        val dbExist = checkDataBase()
        if (!dbExist) {
            /**
             * By calling this method and empty database will be created into
             * the default system path of your application so we are gonna be
             * able to overwrite that database with our database.
             */
            this.readableDatabase
            close()
            try {
                copyDataBase()
            } catch (e: IOException) {
                throw Error("Error copying database")
            }
        }
    }

    /**
     * open database
     *
     * @throws SQLException
     */
    @Throws(SQLException::class)
    fun openDataBase() {
        /**
         * Open the database
         */
        val mPath = DB_PATH + mNameDb
        mDatabase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE)
    }

    @Synchronized
    override fun close() {
        if (mDatabase != null) {
            mDatabase!!.close()
        }
        super.close()
    }

    /**
     * Query table
     *
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @return
     */
    fun query(
        table: String?,
        columns: Array<String?>?,
        selection: String?,
        selectionArgs: Array<String?>?,
        groupBy: String?,
        having: String?,
        orderBy: String?
    ): Cursor {
        return mDatabase!!.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
    }

    fun rawQuery(sql: String?): Cursor {
        return mDatabase!!.rawQuery(sql, null)
    }

    /**
     * Updates value for table in database
     *
     * @param table
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return
     */
    fun update(
        table: String?,
        values: ContentValues?,
        whereClause: String?,
        whereArgs: Array<String?>?
    ): Int {
        return mDatabase!!.update(table, values, whereClause, whereArgs)
    }

    fun getmDatabase(): SQLiteDatabase? {
        return mDatabase
    }

    fun setmDatabase(mDatabase: SQLiteDatabase?) {
        this.mDatabase = mDatabase
    }

    companion object {
        private const val TAG = "DatabaseHelper"
        private const val DATABASE_ALTER_TEAM_1 = ("ALTER TABLE "
                + "TABLE_TEAM" + " ADD COLUMN " + "COLUMN_COACH" + " string;")
        private const val DATABASE_ALTER_TEAM_2 = ("ALTER TABLE "
                + "TABLE_TEAM" + " ADD COLUMN " + "COLUMN_STADIUM" + " string;")

        private const val CREATE_NOTE_TABLE =
            "CREATE TABLE ${DataBaseUtil.TABLE_NOTE} (" +
                    "${DataBaseUtil.ID} INTEGER PRIMARY KEY," +
                    "${DataBaseUtil.FOLDER_ID} TEXT," +
                    "${DataBaseUtil.TITLE} TEXT," +
                    "${DataBaseUtil.CONTENT} TEXT," +
                    "${DataBaseUtil.CREATE_DATE} TEXT," +
                    "${DataBaseUtil.MODIFIED_DATE} TEXT," +
                    "${DataBaseUtil.DATE_TIME} TEXT," +
                    "${DataBaseUtil.COLOR} TEXT," +
                    "${DataBaseUtil.TYPE} TEXT," +
                    "${DataBaseUtil.TIME_ALARM} TEXT," +
                    "${DataBaseUtil.ALARM} TEXT)"

        private const val CREATE_IMAGE_TABLE =
            "CREATE TABLE ${DataBaseUtil.TABLE_IMAGE} (" +
                    "${DataBaseUtil.ID} INTEGER PRIMARY KEY," +
                    "${DataBaseUtil.PATH_IMAGE} TEXT)"

        private const val CREATE_SOUND_TABLE =
            "CREATE TABLE ${DataBaseUtil.TABLE_SOUND} (" +
                    "${DataBaseUtil.ID} INTEGER PRIMARY KEY," +
                    "${DataBaseUtil.TITLE} TEXT," +
                    "${DataBaseUtil.PATH_SOUND} TEXT)"
    }

    init {
        DB_PATH = "data/data/" + mContext.packageName + "/databases/"
    }
}