/**
 * Rating scale
 */

export enum RatingScale {
    ONE_STAR = 1,
    TWO_STARS = 2,
    THREE_STARS = 3,
    FOUR_STARS = 4,
    FIVE_STARS = 5
}


/**
 * Rating DTO
 */

export interface RatingDTO {
    id?: number;
    timestamp?: Date | null;
    rating?: RatingScale | null;
    comment: String;
    authorId?: number;
    productId: number | null;
}

export class RatingTypes implements RatingDTO {
    id?: number;
    timestamp?: Date | null;
    rating?: RatingScale | null;
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
            rating: this.rating,
            comment: this.comment,
            authorId: this.authorId,
            productId: this.productId
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