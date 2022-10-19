package com.aitos.xenon.fundation.service.impl;

import com.aitos.xenon.fundation.mapper.MakerMapper;
import com.aitos.xenon.fundation.model.Maker;
import com.aitos.xenon.fundation.service.MakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MakerServiceImpl implements MakerService {

    @Autowired
    private MakerMapper makerMapper;

    @Override
    public Maker findById(Long id) {
        return makerMapper.findById(id);
    }

    @Override
    public Maker findByAddress(String address) {
        return makerMapper.findByAddress(address);
    }

    @Override
    public List<Maker> findAll() {
        return makerMapper.findAll();
    }

    @Override
    public Maker findByMaker(String makerName) {
        return makerMapper.findByMaker(makerName);
    }
}
