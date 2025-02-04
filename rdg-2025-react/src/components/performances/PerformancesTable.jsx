import PerformanceRow from "./PerformanceRow.jsx"

const PerformancesTable = ({ performances }) => {
    
    if (performances.length > 0) {
        return (
            <div className="mx-3 w-2/3">
                <div className="text-lg font-bold">
                    Performances:
                </div>
                <div className="grid grid-cols-4 bg-slate-400 italic font-bold">
                    <div className="col-span-1 p-1">Date & Time</div>
                    <div className="col-span-1 p-1">Venue</div>
                    <div className="col-span-1 p-1">Festival</div>
                    <div className="col-span-1 p-1">Description</div>
                </div>
                {performances.map((performance, index) => (<PerformanceRow key={ index } performanceData={performance}/>)) }
            </div>
        )
        
    } else {
        return <div className="font-bold m-3">No performances to display</div>
    }

}

export default PerformancesTable