import {RatingDTO, RatingTypes} from "../DTO/rating.types";

export const createRatingFromInterfaces = (data: RatingDTO): RatingTypes => {
    return new RatingTypes(data)
}