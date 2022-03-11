package com.example.samplerunproject

import androidx.lifecycle.LiveData
import androidx.room.*

//DataBase erişimini sağladık
@Dao
interface LinkDao {

    //Select, insert, update, delete

    @Insert    //INSERT INTO userInfo () values ()
    fun insertLink(vararg result: Result)

    @Query("Select * from result ")
    fun getLinkList() : LiveData<List<Result>>

    @Query("DELETE FROM result WHERE uid = :uuid")
    fun deleteLink(uuid: Int) : Int

}