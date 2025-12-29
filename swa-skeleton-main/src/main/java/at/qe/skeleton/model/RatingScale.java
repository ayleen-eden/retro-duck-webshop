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

    public int getStars() {
        return stars;
    }
}
