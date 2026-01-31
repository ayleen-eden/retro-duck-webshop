import {RatingScale, ratingScaleToNumber, RatingTypes} from "../DTO/rating.types";
import React, {useState} from "react";
import {Divider} from "primereact/divider";
import {Tag} from "primereact/tag";
import {Fieldset} from "primereact/fieldset";
import {Rating} from "primereact/rating";
import {UserxApi} from "../utilities/userxApi";
import {UserxTypes} from "../DTO/userx.types";
import {Button} from "primereact/button";
import ratingComponent from "./RatingComponent";
import styles from "../styles/PixelButton.module.css";

interface RatingListProps {
    sortedRatings: RatingTypes[];
    sorting: 'asc' | 'desc';
    setSorting: React.Dispatch<React.SetStateAction<"asc" | "desc">>;
    ratingFilter: number | undefined;
    setRatingFilter: React.Dispatch<React.SetStateAction<number | undefined>>;
}

const RatingListComponent: React.FC<RatingListProps> = (
    {
        sortedRatings,
        sorting,
        setSorting,
        ratingFilter,
        setRatingFilter,
    }) => {

    const filterMessage = (ratingFilter: number | undefined): string => {
        switch (ratingFilter) {
            case 1:
                return "BE THE FIRST TO WRITE A CONTROVERSIAL HOTTAKE";
            case 2:
                return "BE THE FIRST TO DISLIKE THAT";
            case 3:
                return "3/5 TOO MUCH DUCK?";
            case 4:
                return "TELL US HOW IT ALMOST SAVED YOUR MARRIAGE!";
            case 5:
                return "CONVINCE SOMEONE THAT IT'S ABSOLUTE CINEMA";
            default:
                return "BE THE FIRST TO VOICE YOUR OPINION!";
        }
    };

    const fieldsetTemplate = (rating: RatingTypes) => {
        return (
            <Fieldset
                key={rating.id}
                legend={
                    <div className="flex align-items-center justify-content-between px-2 pixel-fieldset p-fieldset legend">
                                    <span className="font-bold">
                                        {rating.username}
                                    </span>
                        <Rating
                            style={{marginTop: '0.25rem', marginBottom: '0.25rem', marginLeft: '3.75rem'}}
                            value={ratingScaleToNumber(rating.rating)}
                            readOnly
                            cancel={false}
                        />
                        <div style={{marginTop: '1rem'}} >
                            {rating.timestamp?.toLocaleString()}
                        </div>
                    </div>
                }
                className="mb-4 pixel-fieldset"
            >
                <p className="m-0">
                    {rating.comment}
                </p>
            </Fieldset>
        );
    };


    return (
        <>
            <div style={{textAlign: "center", marginTop: "0.75rem"}}>
                    <Tag className="pixel-tag pixel-tag-blue" value="What other Users think of this product"/>
            </div>
            <div style={{display: "flex", marginTop: "0.75rem"}}>
                <Button className={`${styles.btn} ${styles.btn_yellow}`} style={{marginLeft: '1rem'}}
                        icon={sorting === "asc" ? "pi pi-sort-amount-up" : "pi pi-sort-amount-down"}
                        label={sorting === "asc" ? "Ascending" : "Descending"}
                        onClick={() => setSorting(sorting === "asc" ? "desc" : "asc")}/>

                <Rating style={{marginLeft: '2rem'}} value={ratingFilter} onChange={(e) => setRatingFilter(e.value ?? undefined)}/>
            </div>
            <div style={{textAlign: "center", marginTop: 50, marginBottom: 50}}>
                {sortedRatings.length === 0 ? (
                    <h2> {filterMessage(ratingFilter)} </h2>
                ) : (
                    sortedRatings.map((rating) => (
                        fieldsetTemplate(rating)
                    ))
                )}
            </div>
        </>
    )
}

export default RatingListComponent;