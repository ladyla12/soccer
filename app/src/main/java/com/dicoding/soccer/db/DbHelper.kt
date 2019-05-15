package com.dicoding.soccer.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

val Context.database: DbHelper
get() = DbHelper.getInstance(applicationContext)

class DbHelper(ctx: Context): ManagedSQLiteOpenHelper(ctx, "Favorite.db", null, 1) {
    companion object {
        private var instance: DbHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DbHelper {
            if (instance == null){
                instance = DbHelper(ctx.applicationContext)
            }
            return instance as DbHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            "MATCH_FAVORITE", true,
            "ID_" to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            "MATCH_ID" to TEXT + UNIQUE,
            "MATCH_DATE" to TEXT,
            "HOME_TEAM_ID" to TEXT,
            "HOME_TEAM_NAME" to TEXT,
            "HOME_TEAM_SCORE" to TEXT,
            "AWAY_TEAM_ID" to TEXT,
            "AWAY_TEAM_NAME" to TEXT,
            "AWAY_TEAM_SCORE" to TEXT
        )

        db.createTable(
            "TEAM_FAVORITE", true,
            "ID_" to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            "TEAM_ID" to TEXT + UNIQUE,
            "TEAM_NAME" to TEXT,
            "TEAM_BADGE" to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("MATCH_FAVORITE", true)
        db.dropTable("TEAM_FAVORITE", true)
    }
}