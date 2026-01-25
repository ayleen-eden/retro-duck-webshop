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
 * Rating DTO
 */

export interface RatingDTO {
    id?: number;
    timestamp?: Date | null;
    rating?: RatingScale;
    comment: String;
    authorId?: number;
    productId: number | null;
}

export class RatingTypes implements RatingDTO {
    id?: number;
    timestamp?: Date | null;
    rating?: RatingScale;
    comment: String;
    authorId?: number;
    productId: number | null;

    constructor(data: RatingDTO) {
        this.id = data.id;
        this.timestamp = data.timestamp ? new Date(data.timestamp): null;
        this.rating = data.rating;
        this.comment = data.comment;
        this.authorId = data.authorId;
        this.productId = data.productId;
    }

    toJSON(): RatingDTO {
        return {
            id: this.id,
            timestamp: this.timestamp,
            rating: this.rating,
            comment: this.comment,
            authorId: this.authorId,
            productId: this.productId
        };
    }

    toCreateJSON(): Pick<RatingDTO, 'rating' | 'comment' | 'authorId' | 'productId'> {
        return {
            rating: RatingScale[this.rating as RatingScale],
            comment: this.comment,
            authorId: this.authorId,
            productId: this.productId
        };
    }

    toUpdateJSON(): Pick<RatingDTO, 'id' | 'rating' | 'comment' | 'authorId' | 'productId'> {
        return {
            id: this.id,
            rating: this.rating,
            comment: this.comment,
            authorId: this.authorId,
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