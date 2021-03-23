package edu.mit.scansite.client.ui.view.tutorial;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.view.PageView;

/**
 * Created by Thomas on 4/18/2017.
 * According to the previously implemented TutorialPageView.java
 */
public class SequenceMatchTutorialPageView extends PageView {
    interface SequenceMatchTutorialPageViewUiBinder extends
            UiBinder<Widget, SequenceMatchTutorialPageView> {
    }

    private static SequenceMatchTutorialPageViewUiBinder uiBinder = GWT
            .create(SequenceMatchTutorialPageViewUiBinder.class);

    public SequenceMatchTutorialPageView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public String getPageTitle() {
        return null;
    }

    @Override
    public boolean isMajorNavigationPage() {
        return false;
    }

    @Override
    public String getMajorPageId() {
        return null;
    }

    @Override
    public String getPageId() {
        return null;
    }

    @Override
    public String getBreadcrumbs() {
        return null;
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}
