package com.digital.mapper.elasticsearch;

import com.digital.model.entity.ParentingEncyclopedia;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  20:14
 */
@Repository
public interface ParentingEncyclopediaRepository extends ElasticsearchRepository<ParentingEncyclopedia, Integer> {

}
