import { format } from "date-fns"
import { Link } from "react-router-dom"

const ProductionHighlight = ({ productionData, setEditMode, handleDelete }) => {

    if (productionData) return (
        <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 w-full rounded p-4 mt-3 md:mx-0 m-3 flex flex-col gap-2 shadow-md">
            <div className="text-black text-xl font-bold flex justify-between">
                <div>
                    {productionData.name}
                </div>
                <div className="flex gap-2">
                    <button className="text-sm hover:underline" onClick={() => setEditMode(true)}>
                        Edit
                    </button>
                    <button className="text-sm hover:underline" onClick={() => handleDelete(productionData)}>
                        Delete
                    </button>
                </div>
            </div>
            {productionData.venue &&
                <div className="flex flex-col">
                    <div className="font-bold italic">
                        Venue
                    </div>
                    <Link className=" hover:underline font-bold" to={`/venues/${productionData.venue.id}`}>
                        {productionData.venue.name}
                    </Link>
                </div>       
            }
            {productionData.author &&
                <div className="flex flex-col">
                    <div className="font-bold italic">
                        Author
                    </div>
                    <div>
                        {productionData.author}
                    </div>
                </div>       
            }
            <div className="flex flex-col">
                <div className="font-bold italic">
                    Sundowners
                </div>
                <div>
                    {productionData.sundowners ? "Yes" : "No"}
                </div>
            </div>       
            
            {productionData.description &&
                <div className="flex flex-col">
                    <div className="font-bold italic">
                        Description
                    </div>
                    <div>
                        {productionData.description}
                    </div>
                </div>       
            }
            
            {productionData.auditionDate &&
                <div className="flex flex-col">
                    <div className="font-bold italic">
                        Audition Date
                    </div>
                    <div>
                        {format(new Date(productionData.auditionDate), "MMMM d, yyyy, h:mm a")}
                    </div>
                </div>       
            }
            <div>
                <div className="flex text-sm gap-1">
                    <div className="font-bold italic">
                        Created:
                    </div>    
                    <div>
                        {format(new Date(productionData.createdAt), "dd-MM-yyyy")}
                    </div>
                </div>
                <div className="flex text-sm gap-1">
                    <div className="font-bold italic">
                        Last Updated:
                    </div>    
                    <div>
                        {format(new Date(productionData.updatedAt), "dd-MM-yyyy")}
                    </div>
                </div>  
            </div>
            <Link to="/productions" className="text-sm hover:underline font-bold text-end">
                See All Productions
            </Link>
        </div>
    )

}

export default ProductionHighlight