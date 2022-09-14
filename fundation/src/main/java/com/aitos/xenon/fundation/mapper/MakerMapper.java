package com.aitos.xenon.fundation.mapper;

import com.aitos.xenon.fundation.model.Maker;

import java.util.List;

public interface MakerMapper {

    Maker findById(Long id);

    Maker findByAddress(String address);

    List<Maker> findAll();

    Maker findByMaker(String makerName);
}
