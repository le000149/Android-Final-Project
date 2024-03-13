package org.algonquin.cst2355.finalproject.dictionary.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.algonquin.cst2355.finalproject.dictionary.Definition;

import java.util.List;

@Dao
public interface DefinitionDao {

    @Query("select distinct word from Definition")
    List<String> getAllDefinitionDistinct();

    @Query("select * from Definition where word = :word")
    List<Definition> getDefinitions(String word);

    @Insert
    void saveDefinitions(List<Definition> definitions);

    @Query("delete from Definition where word = :word")
    void deleteDefinition(String word);

}