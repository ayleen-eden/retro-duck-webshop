import React, {useEffect, useMemo, useRef, useState} from "react";
import {RatingDTO, RatingScale, ratingScaleToNumber, RatingTypes} from "../DTO/rating.types";
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
import RatingListComponent from "./RatingListComponent";

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
    const [sorting, setSorting] = useState<'asc' | 'desc'>('desc')
    const [ratingFilter, setRatingFilter] = useState<number | undefined>(undefined);
    const toast = useRef<Toast>(null)
    const [user, setUser] = useState<UserxTypes | null>(null)

    useEffect(() => {
        const determineUser = async () => {
            try {
                const authenticated : boolean = await UserxApi.isAuthenticated();
                if (authenticated) {
                    const author: UserxTypes = await UserxApi.getCurrentUser();
                    setUser(author);
                }
            } catch (err: any) {
                console.log("Error determining user: ", err);
            }
        }
        void determineUser();
    }, []);

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

    const sortedRatings = useMemo(() => {
        return [...ratings]
            .filter(rating => {
                if (ratingFilter !== undefined) {
                    return ratingScaleToNumber(rating.rating) === ratingFilter;
                }
                return true;
            })
            .sort((a, b) =>
                sorting === "asc"
                    ? ratingScaleToNumber(a.rating) - ratingScaleToNumber(b.rating)
                    : ratingScaleToNumber(b.rating) - ratingScaleToNumber(a.rating)
            );
    }, [ratings, sorting, ratingFilter]);

    useEffect(() => {
        if (loading) return;
        const loadUserRating = async () => {
            if (user != null) {
                const existingRating = ratings.find(rating => rating.authorId === user.id);
                if (existingRating) {
                    setRating(existingRating);
                } else {
                    setRating(RatingTypes.empty());
                }
            }


        }
        void loadUserRating();
    }, [ratings, productId, loading]);

    const createRating = async () => {
        if (user == null) {
            toast.current?.show({
                severity: 'warn',
                summary: 'You need an account to create a rating!',
                detail: 'Click on Login to sign up now!',
                life: 3000
            });
            return;
        }
        if (ratingValue == undefined || comment == '') {
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
            username: author.username,
            productId: productId
        });
        const createdRating: RatingDTO = await RatingApi.createRating(productId, ratingToSave.toCreateJSON());
        setRating(createdRating);
        const newRating = RatingTypes.fromJSON(createdRating)
        setRatings(prevState => [...prevState, newRating])
    };

    const updateRating = async () => {
        if (ratingValue == undefined || comment == '') {
            toast.current?.show({
                severity: 'warn',
                summary: 'Your rating is still incomplete!',
                detail: 'Click on the stars to rate this product',
                life: 3000
            });
            return;
        }

        if (!selectedRating?.id) return;

        try {
            const ratingToUpdate: RatingDTO = {
                ...selectedRating,
                rating: numberToRatingScale(ratingValue),
                comment: comment,
                authorId: selectedRating.authorId,
                username: selectedRating.username,
                productId: productId
            }
            const updatedRating: RatingDTO = await RatingApi.updateRating(productId, selectedRating.id, ratingToUpdate)
            setRating(updatedRating);
            const newRating = RatingTypes.fromJSON(updatedRating);
            setRatings(prevState => prevState.map(rating => rating.id === newRating.id ? newRating : rating))
        } catch (err: any) {
            console.error('Error updating Rating:', err);
        }
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

    return (
        <div>
            <Toast ref={toast} position="top-right"/>
            <div style={{display: "flex", gap: "2rem",
                alignItems: "flex-start"}}>
                <div style={{flex: 1}}>
                <RatingForm selectedRating={selectedRating} ratingValue={ratingValue} comment={comment}
                            setRatingValue={setRatingValue} setComment={setComment} createRating={createRating}
                            updateRating={updateRating} deleteRating={deleteRating}></RatingForm>
                </div>
                <div style={{flex: 2}}>
                <RatingListComponent sortedRatings={sortedRatings} sorting={sorting} setSorting={setSorting}
                                     ratingFilter={ratingFilter}
                                     setRatingFilter={setRatingFilter}></RatingListComponent>
                </div>
            </div>
        </div>
    )
}

export default RatingComponent;