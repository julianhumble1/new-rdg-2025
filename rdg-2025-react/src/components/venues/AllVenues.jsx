// ...existing code...
import { Label, TextInput } from "flowbite-react";
import { useState, useEffect } from "react";
import VenueService from "../../services/VenueService.js";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import { MagnifyingGlassIcon } from "@heroicons/react/16/solid";
import AllVenuesTable from "./AllVenuesTable.jsx";
import CustomSpinner from "../common/CustomSpinner.jsx";
import { useVenuesFilter } from "./useVenuesFilter.js";
import FiltersTable from "../common/FiltersTable.jsx";

const AllVenues = () => {
  const { filteredVenues, filtersForUI, loading } = useVenuesFilter()
 

  return (
       <div className="flex flex-col sm:flex-row">
      <FiltersTable filters={filtersForUI} />
      <div className="flex-1">
        {loading ? (
          <CustomSpinner />
        ) : (
            <AllVenuesTable venues={filteredVenues }  />
        )}
      </div>
    </div>
  )
};

export default AllVenues;
// ...existing code...
