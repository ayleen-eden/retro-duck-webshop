import React, {useEffect, useRef, useState} from "react";
import {RatingDTO, RatingScale, RatingTypes} from "../DTO/rating.types";
import {RatingApi} from "../utilities/ratingApi";
import {createRatingFromInterfaces} from "../utilities/ratingUtilities";
import {UserxApi} from "../utilities/userxApi";
import {UserxTypes} from "../DTO/userx.types";
import {Divider} from "primereact/divider";
import {Rating} from "primereact/rating";
import {Tag} from "primereact/tag";
import {FilterService} from "primereact/api";
import RatingForm from "./RatingForm";
import {Toast} from "primereact/toast";

FilterService.register('custom_range', (value, filters) => {
    const [from, to] = filters ?? [null, null];
    if (from === null && to === null) return true;
    if (from !== null && to === null) return from <= value;
    if (from === null && to !== null) return value <= to;
    return from <= value && value <= to;
});

interface RatingComponentProps {
    productId: number;
}

const RatingComponent: React.FC<RatingComponentProps> = ({productId}) => {
    const [ratings, setRatings] = useState<RatingTypes[]>([]);
    const [ratingValue, setRatingValue] = useState<number | undefined>();
    const [comment, setComment] = useState<string>('');
    const [loading, setLoading] = useState<boolean>(true);
    const [selectedRating, setRating] = useState<RatingDTO>(RatingTypes.empty);
    const toast = useRef<Toast>(null)

    //Unused
    //const [isNewRating, setIsNewRating] = useState<boolean>(false);
    //const [dialogVisible, setDialogVisible] = useState<boolean>(false);

    useEffect(() => {
        const fetchAllRatingsForProduct = async () => {
            try {
                const ratingData = await RatingApi.fetchAllRatingsByProduct(productId)
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
        if (loading) return;
        const loadUserRating = async () => {
            const author: UserxTypes = await UserxApi.getCurrentUser();
            const existingRating = ratings.find(rating => rating.authorId === author.id);

            if (existingRating) {
                setRating(existingRating);
            } else {
                setRating(RatingTypes.empty());
            }
        }
        void loadUserRating();
    }, [ratings, productId, loading]);

    const createRating = async () => {
        if (ratingValue == undefined) {
            toast.current?.show({
                severity: 'warn',
                summary: 'Your rating is still incomplete!',
                detail: 'Click on the stars to rate this product',
                life: 3000
            });
            return;
        }
        const author: UserxTypes = await UserxApi.getCurrentUser();
        const ratingToSave = new RatingTypes({
            ...selectedRating,
            rating: numberToRatingScale(ratingValue),
            comment: comment,
            authorId: author.id,
            productId: productId
        });
        const createdRating: RatingDTO = await RatingApi.createRating(productId, ratingToSave.toCreateJSON());
        setRating(createdRating);
    };

    const updateRating = async () => {
        if (!selectedRating?.id) return;

        try {
            const ratingToUpdate: RatingDTO = {
                ...selectedRating,
                rating: numberToRatingScale(ratingValue),
                comment: comment,
                authorId: selectedRating.authorId,
                productId: productId
            }
            const updatedRating: RatingDTO = await RatingApi.updateRating(productId, selectedRating.id, ratingToUpdate)
            setRating(updatedRating);
        } catch (err: any) {
            console.error('Error updating Rating:', err);
        }
    }

    const onEditRating = (rating: RatingTypes) => {
        setRating(rating);
    }

    const deleteRating = async () => {
        if (!selectedRating.id) return;

        try {
            await RatingApi.deleteRating(productId, selectedRating);
            setRatings(prevState => prevState.filter(prev => prev.id !== selectedRating.id))
            setRating(RatingTypes.empty);
        } catch (err: any) {
            console.error('Error deleting Rating:', err);
        }
    }

    const numberToRatingScale = (ratingValue: number | undefined): RatingScale | undefined => {
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

    const legendTemplate = (
        <div className="flex align-items-center gap-2 px-2">
            <span className="font-bold">Majima Duck</span>
            <Rating value={5} readOnly cancel={false} style={{marginLeft: '0.5rem'}}/>
        </div>
    );

    return (
        <div>
            <Toast ref={toast} position="top-right"/>
            <RatingForm selectedRating={selectedRating} ratingValue={ratingValue} comment={comment}
                        setRatingValue={setRatingValue} setComment={setComment} createRating={createRating}
                        updateRating={updateRating} deleteRating={deleteRating}></RatingForm>
            <div style={{textAlign: "center", marginTop: 50}}>
                <Divider className="pixel-divider-dashed" align="center">
                    <Tag className="pixel-tag pixel-tag-blue" value="What other Users think of this product"/>
                </Divider>
            </div>
            <div style={{textAlign: "center", marginTop: 50, marginBottom: 50}}>
                {ratings.length === 0 ? (
                    <h2> Be the first to voice your opinion! </h2>
                ) : (
                    <p> Rating is present </p>
                )}
            </div>
        </div>
    )
}

export default RatingComponent;