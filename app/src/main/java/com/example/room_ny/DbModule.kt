package com.example.room_ny

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
        context = app,
        klass = UserDatabase::class.java,
        name = "user_db"
    )
        .addMigrations(MIGRATION1_2)
        .addMigrations(MIGRATION2_3)
        .addMigrations(MIGRATION3_4)
        .addMigrations(MIGRATION4_5)
        .addMigrations(MIGRATION5_6)
        .addMigrations(MIGRATION6_7)
        .addMigrations(MIGRATION7_8)
        .addMigrations(MIGRATION8_9)
        .build()

    private val MIGRATION1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE user_table ADD COLUMN surname TEXT NOT NULL DEFAULT ''")
        }
    }

    private val MIGRATION2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE user_table ADD COLUMN employeeBranchCode INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val MIGRATION3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Create a new table without the employeeId column
            db.execSQL(
                "CREATE TABLE user_table_new (" +
                        "userId INTEGER PRIMARY KEY NOT NULL, " +  // Add a comma after each column
                        "name TEXT NOT NULL, " +
                        "age INTEGER NOT NULL, " +
                        "surname TEXT NOT NULL)"
            )

            // Copy data from the old table to the new table
            db.execSQL(
                "INSERT INTO user_table_new (" +
                        "userId, name, age, surname) " +
                        "SELECT userId, name, age, surname FROM user_table"
            )

            // Drop the old table
            db.execSQL("DROP TABLE user_table")

            // Rename the new table to the original table name
            db.execSQL("ALTER TABLE user_table_new RENAME TO user_table")
        }
    }

    private val MIGRATION4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE user_table_5 (" +
                        "userId INTEGER PRIMARY KEY NOT NULL," +
                        "firstName TEXT NOT NULL," +
                        "age INTEGER NOT NULL," +
                        "surname TEXT NOT NULL )"
            )
            db.execSQL(
                "INSERT INTO user_table_5 (" +
                        "userId, firstName, age, surname)" +
                        "SELECT userId, name, age, surname FROM user_table"
            )
            db.execSQL("DROP TABLE user_table")

            db.execSQL("ALTER TABLE user_table_5 RENAME TO user_table")
        }
    }

    private val MIGRATION5_6 = object : Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE user_table_new (" +
                        "userId INTEGER PRIMARY KEY NOT NULL," +
                        "firstName TEXT NOT NULL," +
                        "age TEXT NOT NULL," +
                        "surname TEXT NOT NULL )"
            )
            db.execSQL(
                "INSERT INTO user_table_new (" +
                        "userId, firstName, age, surname)" +
                        "SELECT userId, firstName, CAST(age AS TEXT), surname FROM user_table"
            )
            db.execSQL("DROP TABLE user_table")
            db.execSQL("ALTER TABLE user_table_new RENAME TO user_table")
        }
    }

    private val MIGRATION6_7 = object : Migration(6, 7) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE user_table_new (" +
                        "userId INTEGER PRIMARY KEY NOT NULL," +
                        "firstName TEXT NOT NULL," +
                        "age TEXT NOT NULL," +
                        "surname TEXT NOT NULL DEFAULT 'Ansari')"
            )
            db.execSQL(
                "INSERT INTO user_table_new (" +
                        "userId, firstName, age)" +
                        "SELECT userId, firstName, age FROM user_table"
            )
            db.execSQL("DROP TABLE user_table")
            db.execSQL("ALTER TABLE user_table_new RENAME TO user_table")
        }
    }

    private val MIGRATION7_8 = object : Migration(7, 8) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE user_table ADD COLUMN createdDate INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val MIGRATION8_9 = object : Migration(8, 9){
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE user_table ADD COLUMN updatedDate INTEGER NOT NULL DEFAULT ''")
        }

    }

    @Provides
    @Singleton
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.getUserDao()
    }
}