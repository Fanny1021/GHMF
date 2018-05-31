package com.fanny.ghmf.Presenter;

import com.fanny.ghmf.ICActivity;

/**
 * Created by Fanny on 18/4/2.
 */

public class IcPresenter extends BasePresenter {

    private ICActivity activity;
    @Override
    protected void paraseJson(String json) {

    }

    @Override
    protected void showErrorMessage(String message) {

    }

    public IcPresenter(ICActivity icActivity){
        this.activity=icActivity;
    }


}
