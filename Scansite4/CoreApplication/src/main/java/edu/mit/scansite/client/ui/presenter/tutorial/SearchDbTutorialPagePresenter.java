package edu.mit.scansite.client.ui.presenter.tutorial;

import com.google.gwt.user.client.ui.HasWidgets;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.tutorial.SearchDbTutorialPageView;

/**
 * Created by Thomas on 4/18/2017.
 * Based on TutorialPagePresenter als template
 */
public class SearchDbTutorialPagePresenter extends Presenter {
    private SearchDbTutorialPageView view;

    public SearchDbTutorialPagePresenter(SearchDbTutorialPageView view) {
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
