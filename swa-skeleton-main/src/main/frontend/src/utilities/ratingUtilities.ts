import {RatingDTO, RatingTypes} from "../DTO/rating.types";

export type RatingValidationnResult = {
    valid: boolean;
    message?: string;
    fieldErrors?: Partial<Record<keyof RatingDTO, string>>
}

export const createRatingFromInterfaces = (data: RatingDTO): RatingTypes => {
    return new RatingTypes(data)
}