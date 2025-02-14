import AltPerformanceRow from "./AltPerformanceRow.jsx"

const AltPerformanceTable = ({ performances }) => {
    
    console.log(performances)

    if (performances.length > 0) return (
        <div className="col-span-2 flex flex-col md:rounded-r hover:bg-opacity-50 bg-sky-900 bg-opacity-35 h-full md:h-[30rem] w-full p-4">
            <div className="font-bold text-lg" >
                Performances
            </div>
            <div className="bg-white m-2 rounded overflow-auto ">
                {performances.map((performance, index) => (
                    <AltPerformanceRow key={index} performanceData={performance} />
                ))}

            </div>
        </div>
    )

    return (
        <div>No Performances</div>
    )
}

export default AltPerformanceTable