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

/**
 * Auxillary function to convert ratingScale from the backend to a number
 * @param rating ratingScale of the existing rating
 */
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
 * Auxillary function to convert number from the rating component to ratingScale
 * @param ratingValue numeric value for the rating
 */
export function numberToRatingScale(ratingValue: number | undefined): (RatingScale | undefined) {
    switch (ratingValue) {
        case 1:
            return RatingScale.ONE_STAR;
        case 2:
            return RatingScale.TWO_STARS;
        case 3:
            return RatingScale.THREE_STARS;
        case 4:
            return RatingScale.FOUR_STARS;
        case 5:
            return RatingScale.FIVE_STARS;
        default:
            return undefined;
    }
}

/**
 * Rating DTO
 */

export interface RatingDTO {
    id?: number;
    timestamp?: Date | null;
    ratingScale?: RatingScale;
    comment: String;
    authorId?: number;
    username: String;
    productId: number | null;
}

export class RatingTypes implements RatingDTO {
    id?: number;
    timestamp?: Date | null;
    ratingScale?: RatingScale;
    comment: String;
    authorId?: number;
    username: String;
    productId: number | null;

    constructor(data: RatingDTO) {
        this.id = data.id;
        this.timestamp = data.timestamp ? new Date(data.timestamp): null;
        this.ratingScale = data.ratingScale;
        this.comment = data.comment;
        this.authorId = data.authorId;
        this.username = data.username;
        this.productId = data.productId;
    }

    toJSON(): RatingDTO {
        return {
            id: this.id,
            timestamp: this.timestamp,
            ratingScale: this.ratingScale,
            comment: this.comment,
            authorId: this.authorId,
            username: this.username,
            productId: this.productId
        };
    }

    toCreateJSON(): Pick<RatingDTO, 'ratingScale' | 'comment' | 'authorId' | 'username' |'productId'> {
        return {
            ratingScale: RatingScale[this.ratingScale as RatingScale],
            comment: this.comment,
            authorId: this.authorId,
            username: this.username,
            productId: this.productId
        };
    }

    toUpdateJSON(): Pick<RatingDTO, 'id' | 'ratingScale' | 'comment' | 'authorId' | 'username' | 'productId'> {
        return {
            id: this.id,
            ratingScale: this.ratingScale,
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
            ratingScale: undefined,
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