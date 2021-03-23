package edu.mit.scansite.client.ui.presenter.tutorial;

import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.tutorial.SequenceMatchTutorialPageView;

/**
 * Created by Thomas on 4/18/2017.
 * According to the previously implemented TutorialPageView.java
 */
public class SequenceMatchTutorialPagePresenter extends Presenter {
    private SequenceMatchTutorialPageView view;

    public SequenceMatchTutorialPagePresenter(SequenceMatchTutorialPageView view) {
        this.view = view;
    }

    @Override
    public void bind() {

    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
    }
}
