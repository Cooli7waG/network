package com.aitos.xenon.block.service.impl;

import com.aitos.xenon.account.api.RemoteIPFSService;
import com.aitos.xenon.account.api.RemoteTransactionService;
import com.aitos.xenon.block.api.domain.dto.BlockSearchDto;
import com.aitos.xenon.block.api.domain.vo.BlockVo;
import com.aitos.xenon.block.domain.Block;
import com.aitos.xenon.block.domain.PoggCommit;
import com.aitos.xenon.block.mapper.BlockMapper;
import com.aitos.xenon.block.service.BlockService;
import com.aitos.xenon.block.service.PoggService;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.JSONUtils;
import com.aitos.xenon.core.utils.MerkleTree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BlockServiceImpl implements BlockService {

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private RemoteTransactionService remoteTransactionService;
    @Autowired
    private RemoteIPFSService remoteIPFSService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void genBlock() {
        Block block= getCurrentBlock();

        Block newBlock=new Block();
        if(block==null){
            newBlock.setHeight(1L);
            newBlock.setBlockIntervalTime(0L);
            newBlock.setParentHash(block.getHash());
        }else{
            newBlock.setHeight(block.getHeight()+1);

            newBlock.setCreateTime(LocalDateTime.now());
            Duration duration = Duration.between(block.getCreateTime(), newBlock.getCreateTime());
            newBlock.setBlockIntervalTime(duration.toSeconds());

            //计算merkle tree
            Result<List<String>> hashListResult = remoteTransactionService.findHashByHeight(block.getHeight());
            if(hashListResult.getCode()!= ApiStatus.SUCCESS.getCode()){
                return;
            }
            List<String> hashList = hashListResult.getData();
            String merkleRoot = MerkleTree.merkleTree(hashList);
            block.setMerkleRoot(merkleRoot);

            //计算hash
            String json = JSONUtils.getJSON(block, Lists.newArrayList("height", "blockIntervalTime", "parentHash", "merkleRoot"),
                    Lists.newArrayList("hash", "id", "createTime", "createBy", "updateTime", "updateBy", "delFlag"));
            log.info("block.json={}",json);
            String txHash = DigestUtils.sha256Hex(json);
            block.setHash(txHash);
            newBlock.setParentHash(block.getHash());

            blockMapper.updateMerkleRoot(block.getHeight(),block.getHash(),merkleRoot);
        }
        blockMapper.save(newBlock);
    }

    @Override
    public Block getCurrentBlock() {
        return blockMapper.getCurrentBlock();
    }

    @Override
    public IPage<BlockVo> list(BlockSearchDto queryParams) {
        Page<BlockVo> page=new Page<BlockVo>(queryParams.getOffset(),queryParams.getLimit());
        IPage<BlockVo> pageResult=blockMapper.list(page,queryParams);
        return pageResult;
    }

    @Override
    public List<BlockVo> findListByHeight(Long startHeight, Long endHeight) {
        return blockMapper.findListByHeight(startHeight,endHeight);
    }


}
