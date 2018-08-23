package com.designpattern.knownspies.Activities.SpyList;

import com.designpattern.knownspies.ModelLayer.DTOs.SpyDTO;
import com.designpattern.knownspies.ModelLayer.Enums.Source;
import com.designpattern.knownspies.ModelLayer.ModelLayer;

import java.util.List;

import io.reactivex.functions.Consumer;
import io.realm.Realm;

public class SpyListPresenterImpl implements SpyListPresenter {

    private static final String TAG = "SpyListPresenter";

    ModelLayer modelLayer;


    private Realm realm = Realm.getDefaultInstance();

    public SpyListPresenterImpl(ModelLayer modelLayer) {
        this.modelLayer = modelLayer;
    }

    //region Presenter Methods

    @Override
    public void loadData(Consumer<List<SpyDTO>> onNewResults, Consumer<Source> notifyDataReceived) {
        modelLayer.loadData(onNewResults, notifyDataReceived);
    }

    //endregion

}
