package exam.views;

import io.dropwizard.views.common.View;

public abstract class BaseView extends View {

    private final String title;
    private final String activePath;

    protected BaseView(String templateName, String title, String activePath) {
        super(templateName);
        this.title = title;
        this.activePath = activePath;
    }

    public String getTitle() {
        return title;
    }

    public String getActivePath() {
        return activePath;
    }
}