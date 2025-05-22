package com.digital.model.entity.search;

import com.digital.model.entity.ParentingEncyclopedia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-18  20:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultForPar {
    private List<ParentingEncyclopedia> parentingEncyclopediaList;
    private long total;


}
