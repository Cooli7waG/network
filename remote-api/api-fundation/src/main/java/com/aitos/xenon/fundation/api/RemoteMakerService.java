package com.aitos.xenon.fundation.api;


import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.fundation.api.domain.vo.MakerVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 */
@FeignClient(name = "fundation",url = "http://localhost:9505")
public interface RemoteMakerService {

    @GetMapping("/maker/{id}")
    Result<MakerVo> findById(@PathVariable("id") Long id);

    @GetMapping("/maker/{address}")
    Result<MakerVo> findByAddress(@PathVariable("address") String address);

    @GetMapping("/maker/findByMaker")
    Result<MakerVo> findByMaker(@RequestParam("maker")String maker);

    @GetMapping("/maker/findAll")
    Result<MakerVo> findAll();

}
