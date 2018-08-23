package com.designpattern.knownspies.Activities.SpyList;

import com.designpattern.knownspies.ModelLayer.DTOs.SpyDTO;
import com.designpattern.knownspies.ModelLayer.Enums.Source;

import java.util.List;

import io.reactivex.functions.Consumer;

public interface SpyListPresenter {
    void loadData(Consumer<List<SpyDTO>> onNewResults, Consumer<Source> notifyDataReceived);
}
