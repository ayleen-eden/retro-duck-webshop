import {RatingDTO, RatingTypes} from "../DTO/rating.types";
import globalAxios from "axios";
import {createRatingFromInterfaces} from "./ratingUtilities";

//TODO: Make this Api consistent with userx >w<

const fetchAllRatingsByProduct = async (productId: number): Promise<RatingDTO[]> => {
    try {
        const response = await globalAxios.get(`/api/products/${productId}/ratings`);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error fetching ratings for product: ${err?.message ?? String(err)}`);
    }
}

const createRating = async (productId: number, ratingToCreate: RatingDTO): Promise<RatingTypes> => {
    try {
        const ratingInstance = createRatingFromInterfaces(ratingToCreate);
        const response = await globalAxios.post(`/api/products/${productId}/ratings`, ratingInstance.toCreateJSON());
        return RatingTypes.fromJSON(response.data);
    } catch (err: any) {
        throw new Error(`Error creating rating ${err?.message ?? String(err)}`);
    }
}

const updateRating = async (productId: number, ratingId: number, ratingToUpdate: RatingDTO): Promise<RatingDTO> => {
    try {
        const ratingInstance = createRatingFromInterfaces(ratingToUpdate);
        const response = await globalAxios.patch(`/api/products/${productId}/ratings/${ratingId}`, ratingInstance.toUpdateJSON());
        return RatingTypes.fromJSON(response.data);
    } catch (err: any) {
        throw new Error(`Error updating rating: ${err?.message ?? String(err)}`);
    }
}

const deleteRating = async (productId: number, ratingToDelete: RatingDTO) => {
    try {
        return await globalAxios.delete(`/api/products/${productId}/ratings/${ratingToDelete.id}`);
    } catch (err: any) {
        throw new Error(`Error deleting rating: ${err?.message ?? String(err)}`);
    }
}

const getRating = async (productId: number, ratingId: number): Promise<RatingDTO> => {
    try {
        const response = await globalAxios.get(`api/products/${productId}/ratings/${ratingId}`);
        return RatingTypes.fromJSON(response.data);
    } catch (err: any) {
        throw new Error('Error getting rating: ${err?.message ?? String(err)}');
    }
}

export const RatingApi = {
    createRating,
    updateRating,
    deleteRating,
    fetchAllRatingsByProduct,
    getRating,
}