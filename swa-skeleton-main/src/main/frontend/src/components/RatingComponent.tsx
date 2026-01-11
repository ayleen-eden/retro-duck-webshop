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
import {Tag} from "primereact/tag";
import {InputTextarea} from "primereact/inputtextarea";
import {Button} from "primereact/button";
import {FilterService} from "primereact/api";

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
        if (selectedRating.id !== undefined) {
            return;
        }
        const author: UserxTypes = await UserxApi.getCurrentUser();
        const ratingToSave = new RatingTypes({
            ...selectedRating,
            rating: ratingValue,
            comment: comment,
            authorId: author.id,
            productId: 1 //hardcoded
        });
        await RatingApi.createRating(productId, ratingToSave.toCreateJSON());
        setRating(ratingToSave);
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
        if(!selectedRating.id) return;

        try {
            await RatingApi.deleteRating(productId, selectedRating);
            setRatings(prevState => prevState.filter(prev => prev.id !== selectedRating.id))
            setRating(RatingTypes.empty);
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
            <Divider align="left">
                <Tag value="Tell us what you think of this product!" />
            </Divider>
            <div className="card flex flex-wrap justify-content-center gap-3">
                <Rating style={{display: 'inline-flex', marginLeft: '2rem', marginBottom: '1.5rem'}} value={ratingValue} onChange={(e) => setRatingValue(e.value ?? undefined)}/>
                <Button style={{marginLeft: '6rem', marginBottom: '0.75rem'}} size="small" label="Submit" icon="pi pi-check" iconPos="right" onClick={createRating} />
            </div>
            <InputTextarea style={{marginLeft: '2rem'}} placeholder="Enter your comment here" autoResize value={comment} onChange={(e) => setComment(e.target.value)} rows={5} cols={30} />

            {selectedRating.id !== undefined && (
            <div>
                <Button style={{marginLeft: '18rem', marginTop: '1rem'}} className="p-button-danger" size="small" label="Delete" icon="pi pi-trash" iconPos="right" onClick={deleteRating} />
            </div>
            )}
            <Divider align="center">
                <Tag value="What other Users think of this product" />
            </Divider>
            <div style={{textAlign: "center"}}>
                {ratings.length === 0 ? (
                    <h2>Be the first to voice your opinion!</h2>
                ) : (
                    <p>Rating is present</p>
                )}
            </div>
            <Fieldset legend={legendTemplate}> <p className="m-0"> KIRYU-DUCK! </p> </Fieldset>
        </div>
    )
}

export default RatingComponent;