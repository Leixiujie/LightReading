package com.cqupt.readingcloud.book.service.impl;

import com.cqupt.readingcloud.book.dao.DataDictionaryMapper;
import com.cqupt.readingcloud.book.service.DataDictionaryService;
import com.cqupt.readingcloud.common.cache.RedisExpire;
import com.cqupt.readingcloud.common.cache.RedisService;
import com.cqupt.readingcloud.common.pojo.book.DataDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private DataDictionaryMapper mapper;

    /**
     * 获取小说分类字典模块
     * @param dicType
     * @param field
     * @return
     * 通过redis查，查到了就直接转为<k,v>返回，
     * 否则去数据库查询，查询完存入redis
     */
    @Override
    public Map<String, DataDictionary> getDictionarys(String dicType, String field) {
        String key = "dictionary:" + dicType;
        HashMap<String, DataDictionary> map = new HashMap<>();
        List<DataDictionary> dictionaries = this.redisService.getHashListVal(key, field, DataDictionary.class);

        //获取分类字典信息,没有就去数据库查，查完存到redis
        if(dictionaries.size() == 0){
            dictionaries = this.mapper.findPageWithResult(dicType);
            if(dictionaries.size() > 0){
                map = this.getMap(dictionaries);
                this.redisService.setHashValsExpire(key, map, RedisExpire.DAY);
            }
        }
        else{
            map = this.getMap(dictionaries);
        }

        return map;
    }

    private HashMap<String, DataDictionary> getMap(List<DataDictionary> dictionaries) {
        HashMap<String, DataDictionary> map = new HashMap<>();
        for(int i=0; i<dictionaries.size(); i++){
            DataDictionary dictionary= dictionaries.get(i);
            map.put(dictionary.getCode().toString(), dictionary);
        }

        return map;
    }
}
