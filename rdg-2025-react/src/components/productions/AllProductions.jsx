import { useState, useEffect } from "react";
import ProductionService from "../../services/ProductionService.js";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import ProductionsTable from "./ProductionsTable.jsx";
import { Label, TextInput, ToggleSwitch } from "flowbite-react";
import { MagnifyingGlassIcon } from "@heroicons/react/16/solid";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";

const AllProductions = () => {
  const [productions, setProductions] = useState([]);

  const [showConfirmDelete, setShowConfirmDelete] = useState("");
  const [productionToDelete, setProductionToDelete] = useState(null);

  const [nameSearch, setNameSearch] = useState("");
  const [venueSearch, setVenueSearch] = useState("");
  const [sundownersSearch, setSundownersSearch] = useState(false);

  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const fetchAllProductions = async () => {
    try {
      const response = await ProductionService.getAllProductions();
      setProductions(response.data.productions);
    } catch (e) {
      setErrorMessage(e.message);
    }
  };

  useEffect(() => {
    fetchAllProductions();
  }, []);

  const handleDelete = (production) => {
    setShowConfirmDelete(true);
    setProductionToDelete(production);
  };

  const handleConfirmDelete = async (production) => {
    try {
      await ProductionService.deleteProduction(production.id);
      setShowConfirmDelete(false);
      setSuccessMessage(`Successfully deleted ${production.name}`);
      fetchAllProductions();
    } catch (e) {
      setErrorMessage(e.message);
    }
  };

  return (
    <div>
      {showConfirmDelete && (
        <ConfirmDeleteModal
          setShowConfirmDelete={setShowConfirmDelete}
          itemToDelete={productionToDelete}
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
              placeholder="Production Name"
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
            <div className="m-2">
              <ToggleSwitch
                checked={sundownersSearch}
                onChange={setSundownersSearch}
                label="Sundowners Only?"
              />
            </div>
          </div>
        </div>
        <span className="col-span-5 p-2 pl-0 overflow-x-auto">
          <ProductionsTable
            productions={productions}
            handleDelete={handleDelete}
            nameSearch={nameSearch}
            venueSearch={venueSearch}
            sundownersSearch={sundownersSearch}
          />
        </span>
      </div>
    </div>
  );
};

export default AllProductions;
