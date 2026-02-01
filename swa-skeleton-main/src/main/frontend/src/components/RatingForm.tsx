import React, {useState} from "react";
import {RatingDTO, RatingTypes} from "../DTO/rating.types";
import {Tag} from "primereact/tag";
import {Rating} from "primereact/rating";
import {Button} from "primereact/button";
import styles from "../styles/PixelButton.module.css";
import {InputTextarea} from "primereact/inputtextarea";

interface RatingFormProps {
    selectedRating: RatingDTO;
    ratingValue: number | undefined;
    comment: string;
    setRatingValue: React.Dispatch<React.SetStateAction<number | undefined>>,
    setComment: React.Dispatch<React.SetStateAction<string>>
    createRating: () => Promise<void>,
    updateRating: () => Promise<void>,
    deleteRating: () => Promise<void>
}

const RatingForm: React.FC<RatingFormProps> = (
    {
        selectedRating,
        ratingValue,
        comment,
        setRatingValue,
        setComment,
        createRating,
        updateRating,
        deleteRating
    }) => {

    /**
     * Rating form logic
     */
    return (
        <div>
            <Tag className="pixel-tag pixel-tag-blue" value="Tell us what you think of this product!"
                 style={{marginLeft: '3rem', marginTop: '1rem'}}/>

            <div className="card flex flex-wrap justify-content-center gap-3">
                <Rating style={{display: 'inline-flex', marginLeft: '3.4rem', marginBottom: '1.5rem'}}
                        value={ratingValue} onChange={(e) => setRatingValue(e.value ?? undefined)}/>
                {selectedRating.id == undefined ? (
                    <Button className={`${styles.btn} ${styles.btn_yellow}`}
                            style={{marginLeft: '10.75rem', marginBottom: '0.75rem', marginTop: 5}} size="small"
                            label="Submit" icon="pi pi-check" iconPos="right" onClick={createRating}/>
                ) : (
                    <Button className={`${styles.btn} ${styles.btn_yellow}`}
                            style={{marginLeft: '10.75rem', marginBottom: '0.75rem', marginTop: 5}} size="small"
                            label="Update" icon="pi pi-check" iconPos="right" onClick={updateRating}/>)}
            </div>
            <InputTextarea style={{marginLeft: '5rem', height: 200, width: 400}} placeholder="Enter your comment here"
                           autoResize value={comment} onChange={(e) => setComment(e.target.value)} rows={5} cols={30}/>

            {selectedRating.id !== undefined && (
                <div>
                    <Button className={`${styles.btn} ${styles.btn_grey}`}
                            style={{marginLeft: '23.75rem', marginTop: '1rem'}} size="small" label="Delete"
                            icon="pi pi-trash" iconPos="right" onClick={deleteRating}/>
                </div>
            )}
        </div>
    )
}
export default RatingForm;