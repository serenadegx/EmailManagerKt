package com.fantaike.emailmanagerkt.data.source.local

import androidx.room.*
import com.fantaike.emailmanagerkt.data.Account

@Dao
interface AccountDao {
    @Insert
    fun insert(account: Account)

    @Insert
    fun insertAll(accounts: List<Account>)

    @Delete
    fun delete(account: Account)

    @Delete
    fun deleteAll(accounts: List<Account>)

    @Update
    fun update(account: Account)

    @Query("SELECT * FROM accounts WHERE cur=1 limit 1")
    fun queryCur(): Account

    @Query("SELECT * FROM accounts")
    fun queryAll(): List<Account>

}