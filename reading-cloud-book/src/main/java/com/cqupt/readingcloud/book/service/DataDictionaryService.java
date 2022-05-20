package com.cqupt.readingcloud.book.service;

import com.cqupt.readingcloud.common.pojo.book.DataDictionary;

import java.util.Map;

public interface DataDictionaryService {
    Map<String, DataDictionary> getDictionarys(String dicType, String field);
}
