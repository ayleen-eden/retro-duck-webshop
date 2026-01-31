package at.qe.skeleton.model;

public enum RatingScale {
    ONE_STAR(1),
    TWO_STARS(2),
    THREE_STARS(3),
    FOUR_STARS(4),
    FIVE_STARS(5);

    private final int stars;

    RatingScale(int stars) {
        this.stars = stars;
    }

    /**
     * Used for the conversion of the int value in the Frontend to an enum in the database
     * @param stars int value representing the rating in the frontend
     * @return Corresponding enum to save the rating in the database
     */
    public RatingScale fromInteger(int stars) {
        return switch (stars) {
            case 1 -> RatingScale.ONE_STAR;
            case 2 -> RatingScale.TWO_STARS;
            case 3 -> RatingScale.THREE_STARS;
            case 4 -> RatingScale.FOUR_STARS;
            case 5 -> RatingScale.FIVE_STARS;
            default -> throw new IllegalArgumentException("Conversion from Frontend Rating failed");
        };
    }
}
