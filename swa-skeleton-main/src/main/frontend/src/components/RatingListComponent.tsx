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
import styles from "./PixelButton.module.css";

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
                    <h2> BE THE FIRST TO VOICE YOUR OPINION! </h2>
                ) : (
                    sortedRatings.map((rating) => (
                        <Fieldset
                            key={rating.id}
                            legend={
                                <div className="flex align-items-center justify-content-between px-2 pixel-fieldset p-fieldset legend">
                                    <span className="font-bold">
                                        {rating.username}
                                    </span>
                                    <Rating
                                        value={ratingScaleToNumber(rating.rating)}
                                        readOnly
                                        cancel={false}
                                        style={{marginTop: '0.25rem'}}
                                    />
                                </div>
                            }
                            className="mb-4 pixel-fieldset"
                        >
                            <p className="m-0">
                                {rating.comment}
                            </p>
                        </Fieldset>
                    ))
                )}
            </div>
        </>
    )
}

export default RatingListComponent;