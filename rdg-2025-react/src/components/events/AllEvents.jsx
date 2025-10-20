import AllEventsTable from "./AllEventsTable.jsx";

const AllEvents = () => {
  return (
    <div className="flex flex-col sm:flex-row">
      {/* <div className="md:w-1/5 w-full">
        <FiltersTable  filters={["Name"]}/>
      </div> */}
      <div className="flex-1">
        <AllEventsTable />
      </div>
    </div>
  );
};

export default AllEvents;
