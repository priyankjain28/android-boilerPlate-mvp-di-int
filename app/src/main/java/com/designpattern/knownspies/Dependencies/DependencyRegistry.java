package com.designpattern.knownspies.Dependencies;

import android.os.Bundle;

import com.google.gson.Gson;
import com.designpattern.knownspies.Activities.Details.SpyDetailsActivity;
import com.designpattern.knownspies.Activities.Details.SpyDetailsPresenter;
import com.designpattern.knownspies.Activities.Details.SpyDetailsPresenterImpl;
import com.designpattern.knownspies.Activities.SecretDetails.SecretDetailsActivity;
import com.designpattern.knownspies.Activities.SecretDetails.SecretDetailsPresenter;
import com.designpattern.knownspies.Activities.SecretDetails.SecretDetailsPresenterImpl;
import com.designpattern.knownspies.Activities.SpyList.SpyListActivity;
import com.designpattern.knownspies.Activities.SpyList.SpyListPresenter;
import com.designpattern.knownspies.Activities.SpyList.SpyListPresenterImpl;
import com.designpattern.knownspies.Coordinators.RootCoordinator;
import com.designpattern.knownspies.Helpers.Constants;
import com.designpattern.knownspies.ModelLayer.Database.DataLayer;
import com.designpattern.knownspies.ModelLayer.Database.DataLayerImpl;
import com.designpattern.knownspies.ModelLayer.ModelLayer;
import com.designpattern.knownspies.ModelLayer.ModelLayerImpl;
import com.designpattern.knownspies.ModelLayer.Network.NetworkLayer;
import com.designpattern.knownspies.ModelLayer.Network.NetworkLayerImpl;
import com.designpattern.knownspies.ModelLayer.Translation.SpyTranslator;
import com.designpattern.knownspies.ModelLayer.Translation.SpyTranslatorImpl;
import com.designpattern.knownspies.ModelLayer.Translation.TranslationLayer;
import com.designpattern.knownspies.ModelLayer.Translation.TranslationLayerImpl;

import java.util.NoSuchElementException;

import io.realm.Realm;

public class DependencyRegistry {
    public static  DependencyRegistry shared = new DependencyRegistry();

    //region External Dependencies

    private Gson gson = new Gson();

    private Realm realm = Realm.getDefaultInstance();
    public Realm newRealmInstanceOnCurrentThread() {
        return Realm.getInstance(realm.getConfiguration());
    }

    //endregion

    //region Coordinators

    public RootCoordinator rootCoordinator = new RootCoordinator();

    //endregion

    //region Singletons

    public SpyTranslator spyTranslator = new SpyTranslatorImpl();

    public TranslationLayer translationLayer = createTranslationLayer();
    private TranslationLayer createTranslationLayer() {
        return new TranslationLayerImpl(gson, spyTranslator);
    }

    public DataLayer dataLayer = createDataLayer();
    private DataLayer createDataLayer() {
        return new DataLayerImpl(realm, this::newRealmInstanceOnCurrentThread);
    }

    public NetworkLayer networkLayer = new NetworkLayerImpl();

    public ModelLayer modelLayer = createModelLayer();

    private ModelLayer createModelLayer() {
        return new ModelLayerImpl(networkLayer, dataLayer, translationLayer);
    }

    //endregion

    //region Injection Methods

    public void inject(SpyDetailsActivity activity, Bundle bundle) throws NoSuchElementException {
        int spyId = idFromBundle(bundle);

        SpyDetailsPresenter presenter = new SpyDetailsPresenterImpl(spyId, activity, modelLayer);
        activity.configureWith(presenter, rootCoordinator);
    }

    public void inject(SecretDetailsActivity activity, Bundle bundle) throws NoSuchElementException {
        int spyId = idFromBundle(bundle);

        SecretDetailsPresenter presenter = new SecretDetailsPresenterImpl(spyId, modelLayer);
        activity.configureWith(presenter, rootCoordinator);
    }

    public void inject(SpyListActivity activity) {
        SpyListPresenter presenter = new SpyListPresenterImpl(modelLayer);
        activity.configureWith(presenter, rootCoordinator);
    }

    //endregion

    //region Helper Methods

    private int idFromBundle(Bundle bundle) {
        if(bundle == null) throw new NoSuchElementException("Unable to get spy id from bundle");

        int spyId = bundle.getInt(Constants.spyIdKey);
        if(spyId == 0) throw new NoSuchElementException("Unable to get spy id from bundle");
        return spyId;
    }

    //endregion

}
