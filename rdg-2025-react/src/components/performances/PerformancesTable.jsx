import PerformanceRow from "./PerformanceRow.jsx"

const PerformanceTable = ({ performances, handleDelete }) => {

    if (performances.length > 0) return (
        <div className="col-span-2 flex flex-col md:rounded-r hover:bg-slate-300 bg-slate-200 h-full md:h-[30rem] w-full p-4">
            <div className="font-bold text-lg" >
                Performances
            </div>
            <div className="bg-white m-2 rounded overflow-auto ">
                {performances.map((performance, index) => (
                    <PerformanceRow key={index} performanceData={performance} handleDelete={handleDelete} />
                ))}
            </div>
        </div>
    )

    return (
        <div>No Performances</div>
    )
}

export default PerformanceTable