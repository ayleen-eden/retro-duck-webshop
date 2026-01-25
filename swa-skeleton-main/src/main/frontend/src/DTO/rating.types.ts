/**
 * Rating scale
 */

export enum RatingScale {
    ONE_STAR = "ONE_STAR",
    TWO_STARS = "TWO_STARS",
    THREE_STARS = "THREE_STARS",
    FOUR_STARS = "FOUR_STARS",
    FIVE_STARS = "FIVE_STARS"
}

export function ratingScaleToNumber(rating: RatingScale | undefined): number {
    switch (rating) {
        case RatingScale.ONE_STAR:
            return 1;
        case RatingScale.TWO_STARS:
            return 2;
        case RatingScale.THREE_STARS:
            return 3;
        case RatingScale.FOUR_STARS:
            return 4;
        case RatingScale.FIVE_STARS:
            return 5;
        default:
            return 0;
    }
}

/**
 * Rating DTO
 */

export interface RatingDTO {
    id?: number;
    timestamp?: Date | null;
    rating?: RatingScale;
    comment: String;
    authorId?: number;
    username: String;
    productId: number | null;
}

export class RatingTypes implements RatingDTO {
    id?: number;
    timestamp?: Date | null;
    rating?: RatingScale;
    comment: String;
    authorId?: number;
    username: String;
    productId: number | null;

    constructor(data: RatingDTO) {
        this.id = data.id;
        this.timestamp = data.timestamp ? new Date(data.timestamp): null;
        this.rating = data.rating;
        this.comment = data.comment;
        this.authorId = data.authorId;
        this.username = data.username;
        this.productId = data.productId;
    }

    toJSON(): RatingDTO {
        return {
            id: this.id,
            timestamp: this.timestamp,
            rating: this.rating,
            comment: this.comment,
            authorId: this.authorId,
            username: this.username,
            productId: this.productId
        };
    }

    toCreateJSON(): Pick<RatingDTO, 'rating' | 'comment' | 'authorId' | 'username' |'productId'> {
        return {
            rating: RatingScale[this.rating as RatingScale],
            comment: this.comment,
            authorId: this.authorId,
            username: this.username,
            productId: this.productId
        };
    }

    toUpdateJSON(): Pick<RatingDTO, 'id' | 'rating' | 'comment' | 'authorId' | 'username' | 'productId'> {
        return {
            id: this.id,
            rating: this.rating,
            comment: this.comment,
            authorId: this.authorId,
            username: this.username,
            productId: this.productId
        };
    }

    static empty(): RatingTypes {
        return new RatingTypes({
            id: undefined,
            timestamp: null,
            rating: undefined,
            comment: '',
            authorId: undefined,
            username: '',
            productId: null
        });
    }

    static fromJSON(json: any): RatingTypes {
        if (!json || typeof json !== 'object') {
            throw new Error('Invalid JSON for rating');
        }
        return new RatingTypes(json);
    }
}