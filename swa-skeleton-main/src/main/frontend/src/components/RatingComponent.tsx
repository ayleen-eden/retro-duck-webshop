import React, {useEffect, useState} from "react";
import {RatingDTO, RatingTypes} from "../DTO/rating.types";
import {RatingApi} from "../utilities/ratingApi";
import {createRatingFromInterfaces} from "../utilities/ratingUtilities";
import {UserxApi} from "../utilities/userxApi";
import {UserxTypes} from "../DTO/userx.types";
import { Fieldset } from "primereact/fieldset";
import {Avatar} from "primereact/avatar";
import {Divider} from "primereact/divider";
import {Rating} from "primereact/rating";

interface RatingComponentProps {
    productId: number;
}

const RatingComponent: React.FC<RatingComponentProps> = ({productId}) => {
    const [ratings, setRatings] = useState<RatingTypes[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [selectedRating, setRating] = useState<RatingDTO | null>({
        productId: productId,
        comment: '',
        rating: null
    });

    //Unused
    //const [isNewRating, setIsNewRating] = useState<boolean>(false);
    //const [dialogVisible, setDialogVisible] = useState<boolean>(false);

    useEffect(() => {
        const fetchAllRatingsForProduct = async () => {
            try {
                const ratingData = await RatingApi.fetchAllRatingsByProduct(1)
                const ratingInstances = ratingData.map((rating: RatingDTO) => createRatingFromInterfaces(rating));
                setRatings(ratingInstances);
            } catch (err: any) {
                console.error('Error fetching ratings:', err);
            } finally {
                setLoading(false);
            }
        }
        void fetchAllRatingsForProduct();
    }, [productId]);

    useEffect(() => {
        const loadUserRating = async () => {
            const author: UserxTypes = await UserxApi.getCurrentUser();
            const existingRating = ratings.find(rating => rating.authorId === author.id);

            if (existingRating) {
                setRating(existingRating);
            } else {
                setRating({
                    rating: null,
                    comment: ''
                });
            }
        };
    }, [ratings, productId]);

    const createRating = async () => {
        if (!selectedRating?.rating || !selectedRating.comment.trim()) {
            return;
        }
        const author: UserxTypes = await UserxApi.getCurrentUser();
        const ratingToSave = new RatingTypes({
            ...selectedRating,
            authorId: author.id,
        });
        await RatingApi.createRating(productId, ratingToSave.toCreateJSON());

        setRating({
            rating: null,
            comment: ''
        });
    };

    const updateRating = async () => {
        if (!selectedRating?.id) return;

        try {
            const updatedRating : RatingDTO = await RatingApi.updateRating(productId, selectedRating.id, selectedRating)
            setRating(updatedRating);
        } catch (err: any) {
            console.error('Error updating Rating:', err);
        }
    }

    const onEditRating = (rating: RatingTypes) => {
        setRating(rating);
    }

    const deleteRating = async () => {
        if(!selectedRating?.id) return;

        try {
            await RatingApi.deleteRating(productId, selectedRating);
            setRatings(prevState => prevState.filter(prev => prev.id !== selectedRating.id))
            setRating({
                rating: null,
                comment: ''
            });
        } catch (err: any) {
            console.error('Error deleting Rating:', err);
        }
    }

    const legendTemplate = (
        <div className="flex align-items-center gap-2 px-2">
            <Avatar image="/images/majima_duck.png" shape="circle" style={{verticalAlign: 'middle'}}/>
            <span className="font-bold">Majima Duck</span>
            <Rating value={5} readOnly cancel={false} style={{ marginLeft: '0.5rem' }}/>
        </div>
    );

    return (
        <div className="card">
            <Divider align="center">
                <span className="p-tag">What other Users think of this product</span>
            </Divider>
            <Fieldset legend={legendTemplate}>
                <p className="m-0">
                    KIRYU-DUCK!
                </p>
            </Fieldset>
        </div>
    )
}

export default RatingComponent;