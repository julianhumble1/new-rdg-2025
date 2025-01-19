import { useState, useEffect } from "react"
import FestivalService from "../../services/FestivalService.js"
import FestivalRow from "./FestivalRow.jsx"

const AllFestivalsList = () => {

    const [festivals, setFestivals] = useState([])

    const [fetchError, setFetchError] = useState("")

    useEffect(() => {
        const fetchAllFestivals = async () => {
            try {
                const response = await FestivalService.getAllFestivals();
                setFestivals(response.data.festivals)
                setFetchError("")
            } catch (e) {
                setFetchError(e.message)
                
            }
        }
        fetchAllFestivals()
    }, [])


    return (<>
        <div>List of all festivals</div>
        {fetchError && 
            <div className="text-red-500">
                {fetchError}
            </div>
        }
        {festivals && 
            <div className="mx-3">
                <div className="grid grid-cols-12 bg-slate-400 italic font-bold">
                    <div className="col-span-2 p-1">Name</div>
                    <div className="col-span-2 p-1">Venue</div>
                    <div className="col-span-2 p-1">Description</div>
                    <div className="col-span-2 p-1">Year</div>
                    <div className="col-span-2 p-1">Month</div>
                    <div className="col-span-2 p-1">Date Created</div>
                </div>
                {festivals.map((festival, index) => (
                    <FestivalRow festivalData={festival} key={index} />
                ))}
            </div>


            
        }
    </>)
}

export default AllFestivalsList