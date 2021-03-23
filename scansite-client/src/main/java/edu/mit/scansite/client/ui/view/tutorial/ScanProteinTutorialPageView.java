package edu.mit.scansite.client.ui.view.tutorial;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.event.NavigationEvent;

/**
 * Created by Thomas on 4/18/2017.
 * According to the previously implemented TutorialPageView.java
 */
public class ScanProteinTutorialPageView extends PageView {
    interface ScanProteinTutorialPageViewUiBinder extends
            UiBinder<Widget, ScanProteinTutorialPageView> {
    }

    private static ScanProteinTutorialPageViewUiBinder uiBinder = GWT
            .create(ScanProteinTutorialPageViewUiBinder.class);

    public ScanProteinTutorialPageView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public String getPageTitle() {
        return "Protein Scan Tutorial";
    }

    @Override
    public boolean isMajorNavigationPage() {
        return false;
    }

    @Override
    public String getMajorPageId() {
        return getPageId();
    }

    @Override
    public String getPageId() {
        return NavigationEvent.PageId.PSCAN_TUTORIAL.getId();
    }

    @Override
    public String getBreadcrumbs() {
        return Breadcrumbs.PSCAN_TUTORIAL;
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}
