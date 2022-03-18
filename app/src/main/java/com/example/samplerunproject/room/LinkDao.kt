package com.example.samplerunproject.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.samplerunproject.Result

//DataBase erişimini sağladık
@Dao
interface LinkDao {

    //Select, insert, update, delete

    @Insert(onConflict = OnConflictStrategy.IGNORE)    //INSERT INTO userInfo () values ()
    fun insertLink(vararg result: Result)

    @Query("Select * from result ")
    fun getLinkList() : LiveData<List<Result>>

    @Query("DELETE FROM result WHERE code = :code")
    fun deleteLink(code: String) : Int

}