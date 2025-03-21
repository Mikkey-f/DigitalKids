package com.digital.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  20:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {
    private List<ParentingEncyclopedia> parentingEncyclopediaList;
    private long total;


}
