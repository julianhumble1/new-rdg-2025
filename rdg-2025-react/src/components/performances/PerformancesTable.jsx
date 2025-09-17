import PerformanceRow from "./PerformanceRow.jsx";

const PerformanceTable = ({ performances, handleDelete }) => {
  if (performances && performances.length > 0) {
    return (
      <div className="md:w-1/2 w-full">
        <div className="bg-white m-2 rounded overflow-auto ">
          {performances.map((performance, index) => (
            <PerformanceRow
              key={index}
              performanceData={performance}
              handleDelete={handleDelete}
            />
          ))}
        </div>
      </div>
    );
  } else {
    return null;
  }
};

export default PerformanceTable;
