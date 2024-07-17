package todo.service.model;

public enum TaskStatus {
    CREATED,
    STARTED,
    COMPLETED;

    public static TaskStatus fromString(String status) {
        for (TaskStatus enumValue : TaskStatus.values()) {
            if (enumValue.name().equalsIgnoreCase(status)) {
                return enumValue;
            }
        }

        throw new IllegalArgumentException("Unknown TaskStatus: " + status);
    }
}

