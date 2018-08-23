package com.designpattern.knownspies.ModelLayer;

import com.designpattern.knownspies.ModelLayer.DTOs.SpyDTO;
import com.designpattern.knownspies.ModelLayer.Enums.Source;

import java.util.List;

import io.reactivex.functions.Consumer;

public interface ModelLayer {
    void loadData(Consumer<List<SpyDTO>> onNewResults, Consumer<Source> notifyDataReceived);

    SpyDTO spyForId(int spyId);
}
