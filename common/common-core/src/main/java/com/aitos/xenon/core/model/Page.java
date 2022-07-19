package com.aitos.xenon.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Page<T> {
    private Long total=0L;

    private List<T> items=new ArrayList<>();

}