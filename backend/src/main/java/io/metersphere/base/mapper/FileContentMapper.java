package io.metersphere.base.mapper;

import io.metersphere.base.domain.FileContent;
import io.metersphere.base.domain.FileContentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FileContentMapper {
    long countByExample(FileContentExample example);

    int deleteByExample(FileContentExample example);

    int deleteByPrimaryKey(String id);

    int insert(FileContent record);

    int insertSelective(FileContent record);

    List<FileContent> selectByExampleWithBLOBs(FileContentExample example);

    List<FileContent> selectByExample(FileContentExample example);

    FileContent selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") FileContent record, @Param("example") FileContentExample example);

    int updateByExampleWithBLOBs(@Param("record") FileContent record, @Param("example") FileContentExample example);

    int updateByExample(@Param("record") FileContent record, @Param("example") FileContentExample example);

    int updateByPrimaryKeySelective(FileContent record);

    int updateByPrimaryKeyWithBLOBs(FileContent record);
}