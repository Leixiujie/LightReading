package com.cqupt.readingcloud.book.dao;

import com.cqupt.readingcloud.common.pojo.book.DataDictionary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataDictionaryMapper {
    DataDictionary selectById(Integer id);

    DataDictionary selectByDicTypeAndCode(@Param("dicType") String dicType, @Param("code") Integer code);

    List<DataDictionary> findPageWithResult(@Param("dicType") String dicType);

    Integer findPageWithCount(@Param("dicType") String dicType);
}
