package com.example.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.liu.common.R;
import com.example.liu.model.domain.RankList;
import com.example.liu.model.request.RankListRequest;

public interface RankListService extends IService<RankList> {

    R addRank(RankListRequest rankListAddRequest);

    R rankOfSongListId(Long songListId);

    R getUserRank(Long consumerId, Long songListId);

}
