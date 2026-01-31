import {RatingDTO, RatingTypes} from "../DTO/rating.types";
import globalAxios from "axios";
import {createRatingFromInterfaces} from "./ratingUtilities";

//TODO: Make this Api consistent with userx >w<


/**
 * Fetch all ratings for a product from the backend
 * @returns Promise<RatingDTO[]> a promise that resolves with an array of RatingDTO objects
 * @throws Error if the request fails
 */
const fetchAllRatingsByProduct = async (productId: number): Promise<RatingDTO[]> => {
    try {
        const response = await globalAxios.get(`/api/products/${productId}/ratings`);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error fetching ratings for product: ${err?.message ?? String(err)}`);
    }
}

/**
 * Create a new rating
 * @param productId for the product to rate
 * @param ratingToCreate ratingDTO containing the new rating
 * @returns Promise<RatingTypes> a promise that resolves with the created rating
 * @throws Error if the request fails
 */
const createRating = async (productId: number, ratingToCreate: RatingDTO): Promise<RatingTypes> => {
    try {
        const ratingInstance = createRatingFromInterfaces(ratingToCreate);
        const response = await globalAxios.post(`/api/products/${productId}/ratings`, ratingInstance.toCreateJSON());
        return RatingTypes.fromJSON(response.data);
    } catch (err: any) {
        throw new Error(`Error creating rating ${err?.message ?? String(err)}`);
    }
}

/**
 * Update an existing rating
 * @param productId product associated with the rating
 * @param ratingId Id of the existing rating
 * @param ratingToUpdate rating to replace the original rating
 * @returns Promise<RatingTypes> a promise that resolves with the updated rating
 * @throws Error if the request fails
 */
const updateRating = async (productId: number, ratingId: number, ratingToUpdate: RatingDTO): Promise<RatingDTO> => {
    try {
        const ratingInstance = createRatingFromInterfaces(ratingToUpdate);
        const response = await globalAxios.patch(`/api/products/${productId}/ratings/${ratingId}`, ratingInstance.toUpdateJSON());
        return RatingTypes.fromJSON(response.data);
    } catch (err: any) {
        throw new Error(`Error updating rating: ${err?.message ?? String(err)}`);
    }
}

/**
 * Delete an existing user
 * @param productId product associated with the rating
 * @param ratingToDelete selected rating to delete (Users can delete their own rating)
 * @returns Promise<any> a promise that resolves with the response data
 * @throws Error if the request fails
 */
const deleteRating = async (productId: number, ratingToDelete: RatingDTO) => {
    try {
        return await globalAxios.delete(`/api/products/${productId}/ratings/${ratingToDelete.id}`);
    } catch (err: any) {
        throw new Error(`Error deleting rating: ${err?.message ?? String(err)}`);
    }
}


/**
 * Return a specific rating for a given product
 * @returns Promise<RatingDTO> a promise that resolves with the response data
 * @throws Error if the request fails
 */
const g
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