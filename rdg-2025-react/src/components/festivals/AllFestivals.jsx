import { useState, useEffect } from "react";
import FestivalService from "../../services/FestivalService.js";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import { Label, TextInput } from "flowbite-react";
import { MagnifyingGlassIcon } from "@heroicons/react/16/solid";
import FestivalsTable from "./FestivalsTable.jsx";

const AllFestivals = () => {
  const [festivals, setFestivals] = useState([]);

  const [nameSearch, setNameSearch] = useState("");
  const [venueSearch, setVenueSearch] = useState("");

  const [showConfirmDelete, setShowConfirmDelete] = useState("");
  const [festivalToDelete, setFestivalToDelete] = useState(null);

  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const fetchAllFestivals = async () => {
    try {
      const response = await FestivalService.getAllFestivals();
      setFestivals(response.data.festivals);
      setErrorMessage("");
    } catch (e) {
      setErrorMessage(e.message);
    }
  };

  useEffect(() => {
    fetchAllFestivals();
  }, []);

  const handleDelete = (festival) => {
    setShowConfirmDelete(true);
    setFestivalToDelete(festival);
  };

  const handleConfirmDelete = async (festival) => {
    await FestivalService.deleteFestivalById(festival.id);
    setShowConfirmDelete(false);
    setSuccessMessage(`Successfully deleted ${festival.name}`);
    fetchAllFestivals();
  };

  return (
    <div>
      {showConfirmDelete && (
        <ConfirmDeleteModal
          setShowConfirmDelete={setShowConfirmDelete}
          itemToDelete={festivalToDelete}
          handleConfirmDelete={handleConfirmDelete}
        />
      )}
      <SuccessMessage message={successMessage} />
      <ErrorMessage message={errorMessage} />
      <div className="grid lg:grid-cols-6 grid-cols-1">
        <div className="col-span-1 rounded m-2 border bg-slate-200 max-h-fit drop-shadow-md lg:pb-6">
          <div className="flex flex-col">
            <div className="font-bold lg:text-center m-2">Filters</div>
            <div className="m-2">
              <Label value="Search" />
            </div>
            <TextInput
              icon={MagnifyingGlassIcon}
              placeholder="Festival Name"
              className="m-2 mt-0"
              value={nameSearch}
              onChange={(e) => setNameSearch(e.target.value)}
            />
            <TextInput
              icon={MagnifyingGlassIcon}
              placeholder="Venue Name"
              className="m-2 mt-0"
              value={venueSearch}
              onChange={(e) => setVenueSearch(e.target.value)}
            />
          </div>
        </div>
        <span className="col-span-5 p-2 pl-0 overflow-x-auto">
          <FestivalsTable
            festivals={festivals}
            handleDelete={handleDelete}
            nameSearch={nameSearch}
            venueSearch={venueSearch}
          />
        </span>
      </div>
    </div>
  );
};

export default AllFestivals;
