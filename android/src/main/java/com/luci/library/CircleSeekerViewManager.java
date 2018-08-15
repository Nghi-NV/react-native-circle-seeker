package com.luci.library;

import android.view.View;
import android.widget.ProgressBar;

import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

public class CircleSeekerViewManager extends SimpleViewManager<CircleSeekerView> {
    public static final String REACT_CLASS = "CircleSeeker";

    public static final int defaultRadius = 10;
    public static final int defaultSampling = 10;

    private static ThemedReactContext context;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public CircleSeekerView createViewInstance(ThemedReactContext ctx) {
        context = ctx;

        return new CircleSeekerView(ctx);
    }

    @ReactProp(name = "colorPoint", customType = "Color")
    public void setColorPointEnd(CircleSeekerView view, int color) {
        view.setColorPointStart(color);
    }

    @ReactProp(name = "withLine", defaultInt = 20)
    public void setWithLine(CircleSeekerView view, int with) {
        view.setWithLine(with);
    }

    @ReactProp(name = "colorCircle", customType = "Color")
    public void setColorCircle(CircleSeekerView view, int color) {
        view.setColorCircle(color);
    }

    @ReactProp(name = "colorCircleTo", customType = "Color")
    public void setColorCircleTo(CircleSeekerView view, int color) {
        view.setGradientColorCircle(new int[]{color, color});
    }

    @ReactProp(name = "colorCircleFrom", customType = "Color")
    public void setColorCircleFrom(CircleSeekerView view, int color) {
        view.setGradientColorCircle(new int[]{color, color});
    }

    @ReactProp(name = "colorCircleBackground", customType = "Color")
    public void setColorCircleBackground(CircleSeekerView view, int color) {
        view.setColorCircleBackground(color);
    }

    @ReactProp(name = "withLineBackground", defaultInt = 20)
    public void setWithLineBackground(CircleSeekerView view, int with) {
        view.setWithLineBackground(with);
    }


    @ReactProp(name = "value", defaultFloat = 0.0f)
    public void setValue(CircleSeekerView view, float with) {
        view.setCurrentRadianEnd(with);
    }

    @ReactProp(name = "enable", defaultBoolean = true)
    public void setEnable(CircleSeekerView view, boolean enable) {
        view.setEnable(enable);
    }

}