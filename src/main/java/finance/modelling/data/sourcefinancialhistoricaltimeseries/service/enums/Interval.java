package finance.modelling.data.sourcefinancialhistoricaltimeseries.service.enums;

public enum Interval {
    DAY("d"),
    WEEK("w"),
    MONTH("m");

    private final String label;

    Interval(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
