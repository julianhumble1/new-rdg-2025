import { Label, TextInput } from "flowbite-react";
import { useState, useEffect } from "react";
import VenueService from "../../services/VenueService.js";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";

import AltVenueTable from "./AllVenuesTable.jsx";
import { MagnifyingGlassIcon } from "@heroicons/react/16/solid";
import FeedbackToast from "../common/FeedbackToast.jsx";

const AllVenues = () => {
  const [venues, setVenues] = useState([]);

  const [showConfirmDelete, setShowConfirmDelete] = useState("");
  const [venueToDelete, setVenueToDelete] = useState(null);

  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const [nameSearch, setNameSearch] = useState("");

  const handleDelete = (venue) => {
    setShowConfirmDelete(true);
    setVenueToDelete(venue);
  };

  const handleConfirmDelete = async (venue) => {
    try {
      await VenueService.deleteVenue(venue.id);
      setShowConfirmDelete(false);
      setVenueToDelete(null);
      setSuccessMessage(`Successfully deleted '${venue.name}'`);
      fetchAllVenues();
    } catch (e) {
      setErrorMessage(e.message);
    }
  };

  const fetchAllVenues = async () => {
    try {
      const response = await VenueService.getAllVenues();
      setVenues(response.data.venues);
    } catch (e) {
      setErrorMessage(e.message);
    }
  };

  useEffect(() => {
    fetchAllVenues();
  }, []);

  return (
    <>
      <div>
        {showConfirmDelete && (
          <ConfirmDeleteModal
            setShowConfirmDelete={setShowConfirmDelete}
            itemToDelete={venueToDelete}
            handleConfirmDelete={handleConfirmDelete}
          />
        )}
        <SuccessMessage message={successMessage} />
        <ErrorMessage message={errorMessage} />
        <div className="grid lg:grid-cols-6 grid-cols-1">
          <div className="col-span-1 rounded m-2 border bg-slate-200 max-h-screen drop-shadow-md">
            <div className="flex flex-col">
              <div className="font-bold lg:text-center m-2">Filters</div>
              <div className="m-2">
                <Label value="Search" />
              </div>
              <TextInput
                icon={MagnifyingGlassIcon}
                placeholder="Venue Name"
                className="m-2 mt-0"
                value={nameSearch}
                onChange={(e) => setNameSearch(e.target.value)}
              />
            </div>
          </div>
          <span className="col-span-5 p-2 pl-0 overflow-x-auto">
            <AltVenueTable
              venues={venues}
              handleDelete={handleDelete}
              nameSearch={nameSearch}
            />
          </span>
        </div>
      </div>
    </>
  );
};

export default AllVenues;
