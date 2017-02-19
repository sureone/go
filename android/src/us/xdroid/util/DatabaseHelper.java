package us.xdroid.util;

import java.util.concurrent.atomic.AtomicInteger;

import us.xdroid.util.RemoteResource;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * "accounts.db" is used to manage account information. 2 tables are created
 * 1) myaccount - local account
 * 2) account - cache of other accounts'
 * information
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String LOG_TAG = "DatabaseHelper";
    // name of the database file for your application -- change to something
    // appropriate for your app
    private static final String DATABASE_NAME = "app.db";
    // any time you make changes to your database objects, you may have to
    // increase the database version
    private static final int DATABASE_VERSION = 1;

    private Dao<RemoteResource, Integer> resourceDao = null;


    private static final AtomicInteger usageCounter = new AtomicInteger(0);

    // we do this so there is only one helper
    private static DatabaseHelper helper = null;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Get the helper, possibly constructing it if necessary. For each call
     * to this method, there should be 1 and only 1 call to {@link #close()}
     * .
     */
    public static synchronized DatabaseHelper getHelper(
        Context context) {
        // FIXME - increment counter here???
        usageCounter.incrementAndGet();

        if (helper == null) {
            helper = new DatabaseHelper(context);
        }
        return helper;
    }

    /**
     * This is called when the database is first created. Usually you should
     * call createTable statements here to create the tables that will store
     * your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db,
                         ConnectionSource connectionSource) {

        try {

            TableUtils.createTableIfNotExists(connectionSource, RemoteResource.class);
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher
     * version number. This allows you to adjust the various data to match
     * the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db,
                          ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            Log.i(LOG_TAG, "upgrading database");
            TableUtils.dropTable(connectionSource,
                                 DatabaseHelper.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (java.sql.SQLException e) {
            Log.e(LOG_TAG, "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }


    public Dao<RemoteResource, Integer> getResourceDao() {
        if (resourceDao == null) {
            try {
                resourceDao = getDao(RemoteResource.class);
            } catch (java.sql.SQLException e) {
                Log.e(LOG_TAG, "Can not get Resource DAO");
            }
        }
        return resourceDao;
    }

    /**
     * Close the database connections and clear any cached DAOs. For each
     * call to {@link #getHelper(Context)}, there should be 1 and only 1
     * call to this method. If there were 3 calls to
     * {@link #getHelper(Context)} then on the 3rd call to this method, the
     * helper and the underlying database connections will be closed.
     */
    @Override
    public void close() {
        if (usageCounter.decrementAndGet() == 0) {
            super.close();
            resourceDao = null;
        }
    }
}
