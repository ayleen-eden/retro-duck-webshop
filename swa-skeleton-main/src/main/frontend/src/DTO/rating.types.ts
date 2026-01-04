/**
 * Rating scale
 */

export enum RatingScale {
    ONE_STAR = '⭐',
    TWO_STARS = '⭐⭐',
    THREE_STARS = '⭐⭐⭐',
    FOUR_STARS = '⭐⭐⭐⭐',
    FIVE_STARS = '⭐⭐⭐⭐⭐'
}


/**
 * Rating DTO
 */

export interface RatingDTO {
    id?: number;
    timestamp?: Date | null;
    rating?: RatingScale | null;
    comment: String;
    authorId?: number | null;
    productId?: number | null;
}

export class RatingTypes implements RatingDTO {
    id?: number;
    timestamp?: Date | null;
    rating?: RatingScale | null;
    comment: String;
    authorId?: number | null;
    productId?: number | null;

    constructor(data: RatingDTO) {
        this.id = data.id;
        this.timestamp = data.timestamp ? new Date(data.timestamp): null;
        this.rating = data.rating;
        this.comment = data.comment;
        this.authorId = data.authorId ?? null;
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

    toCreateJSON(): Pick<RatingDTO, 'rating' | 'comment'> {
        return {
            rating: this.rating,
            comment: this.comment
        };
    }

    toUpdateJSON(): Pick<RatingDTO, 'id' | 'rating' | 'comment'> {
        return {
            id: this.id, //Not certain why this would be needed
            rating: this.rating,
            comment: this.comment
        };
    }

    static empty(): RatingTypes {
        return new RatingTypes({
            id: undefined,
            timestamp: null,
            rating: null,
            comment: '',
            authorId: null,
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