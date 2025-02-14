import { format } from "date-fns"
import { Link } from "react-router-dom"
import MonthDateUtils from "../../utils/MonthDateUtils.js"

const FestivalHighlight = ({ festivalData, setEditMode, handleDelete }) => {
    
    if (festivalData) return (
        <div className="bg-sky-900 bg-opacity-35 md:rounded-l p-4 m-0 flex flex-col gap-2 col-span-3 h-full">
            <div className="text-black text-xl font-bold flex justify-between">
                <div>
                    {festivalData.name}
                </div>
                <div className="flex gap-2">
                    <button className="text-sm hover:underline" onClick={() => setEditMode(true)}>
                        Edit
                    </button>
                    <button className="text-sm hover:underline" onClick={() => handleDelete(festivalData)}>
                        Delete
                    </button>
                </div>
            </div>
            <div className="flex flex-col justify-between h-full ">
                <div className="flex flex-col gap-2">
                    {festivalData.venue &&
                        <div className="flex flex-col">
                            <div className="font-bold italic">
                                Venue
                            </div>
                            <Link className=" hover:underline font-bold" to={`/venues/${festivalData.venue.id}`}>
                                {festivalData.venue.name}
                            </Link>
                        </div>       
                    }
                    <div className="flex flex-col">
                        <div className="font-bold italic">
                            Date
                        </div>
                        <div>
                            {festivalData.month ? MonthDateUtils.monthMapping[festivalData.month] : ""} {festivalData.year}
                        </div>
                    </div>     
                    {festivalData.description &&
                        <div className="flex flex-col max-h-36 overflow-auto">
                            <div className="font-bold italic">
                                Description
                            </div>
                            <div>
                                {festivalData.description}
                            </div>
                        </div>       
                    }
                </div>
            </div>
            <div className="flex justify-between mt-2">
                <div>
                    <div className="flex text-sm gap-1">
                        <div className="font-bold italic">
                            Created:
                        </div>    
                        <div>
                            {format(new Date(festivalData.createdAt), "dd-MM-yyyy")}
                        </div>
                    </div>
                    <div className="flex text-sm gap-1">
                        <div className="font-bold italic">
                            Last Updated:
                        </div>    
                        <div>
                            {format(new Date(festivalData.updatedAt), "dd-MM-yyyy")}
                        </div>
                    </div>  
                </div>
                <Link to="/festivals" className="text-sm hover:underline font-bold text-end my-auto">
                    See All Festivals
                </Link>

            </div>
        </div>

    )


}

export default FestivalHighlight