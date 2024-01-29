package com.dfrecvcle.dfsystem.live.mappers;

import com.dfrecvcle.dfsystem.live.entities.GameTalk;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GameTalkMapper {

    int batchInsertGameTalk(List<GameTalk> list);

    void updateGameTalkDisband(String roundDate);

    int updateGameTalk(GameTalk gameTalk);

    void deleteGameTalk(String groupName);

    void updateGameTalkDisbandByGroupId(String groupId);

    List<GameTalk> selectGameTalk(String roundDate, Integer disband);

    List<GameTalk> findGameTalkByRoundDate(String begin, String end);

    GameTalk findGameTalkByGroupName(@Param("name") String name);
}
