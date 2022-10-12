package com.key.win.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.file.model.ChunkFile;

import java.util.List;


public interface ChunkFileService extends IService<ChunkFile> {

    ChunkFile upload(ChunkFile chunkFile) throws Exception;

    /**
     * 保存文件块
     *
     * @param chunk
     */
    boolean saveChunk(ChunkFile chunk);

    /**
     * 检查文件块是否存在
     *
     * @param identifier
     * @param chunkNumber
     * @return
     */
    boolean checkChunk(String identifier, Long chunkNumber);

    boolean merge(ChunkFile fileInfo) throws Exception ;


    void delete(ChunkFile chunkFile);

    ChunkFile getById(String id);

    PageResult<ChunkFile> findFileInfoByPaged(PageRequest<ChunkFile> t);

    List<ChunkFile> findChunkFile(ChunkFile chunkFile);
}
