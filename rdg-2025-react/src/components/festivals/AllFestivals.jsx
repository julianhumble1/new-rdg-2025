import { useState, useEffect } from "react";
import FestivalService from "../../services/FestivalService.js";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import { Label, TextInput } from "flowbite-react";
import { MagnifyingGlassIcon } from "@heroicons/react/16/solid";
import FestivalsTable from "./FestivalsTable.jsx";
import CustomSpinner from "../common/CustomSpinner.jsx";
import { useFestivalsFilter } from "./useFestivalsFilter.js";
import FiltersTable from "../common/FiltersTable.jsx";

const AllFestivals = () => {
  const { filteredFestivals, loading, filtersForUI } = useFestivalsFilter();

  return (
    <div className="flex flex-col sm:flex-row">
      <FiltersTable filters={filtersForUI} />
      <div className="flex-1">
        {loading ? (
          <CustomSpinner />
        ) : (
          <FestivalsTable festivals={filteredFestivals} />
        )}
      </div>
    </div>
  );
};

export default AllFestivals;
