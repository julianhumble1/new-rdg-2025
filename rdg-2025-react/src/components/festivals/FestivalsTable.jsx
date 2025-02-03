import FestivalRow from "./FestivalRow.jsx"

const FestivalsTable = ({ festivals }) => {
    
    if (festivals.length > 0) {
        return (
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
        )
    } else {
        return <div className="font-bold m-3">No festivals to display</div>
    }

}

export default FestivalsTable