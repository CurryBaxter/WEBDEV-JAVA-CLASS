package exam.views;

public class HomeView extends BaseView {

    private final long customerCount;

    public HomeView(long customerCount) {
        super("home.ftl", "Startseite - CRM", "/ui");
        this.customerCount = customerCount;
    }

    public long getCustomerCount() {
        return customerCount;
    }
}