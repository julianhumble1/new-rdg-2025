// ...existing code...
import { useEffect, useState } from "react";
import PersonService from "../../services/PersonService.js";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import { Label, TextInput } from "flowbite-react";
import PeopleTable from "./PeopleTable.jsx";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import { MagnifyingGlassIcon } from "@heroicons/react/16/solid";
import CustomSpinner from "../common/CustomSpinner.jsx";

const AllPeople = () => {
  const [people, setPeople] = useState([]);
  const [responseType, setResponseType] = useState("");

  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const [personToDelete, setPersonToDelete] = useState("");
  const [showConfirmDelete, setShowConfirmDelete] = useState("");

  const [nameSearch, setNameSearch] = useState("");

  // loading state
  const [loading, setLoading] = useState(true);

  const fetchAllPeople = async () => {
    setLoading(true);
    try {
      const response = await PersonService.getAllPeople();
      setPeople(response.data.people);
      setResponseType(response.data.responseType);
    } catch (e) {
      setErrorMessage(e.message);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = (person) => {
    setShowConfirmDelete(true);
    setPersonToDelete(person);
  };

  const handleConfirmDelete = async (person) => {
    try {
      await PersonService.deletePersonById(person.id);
      setShowConfirmDelete(false);
      setSuccessMessage(
        `Successfully deleted ${person.firstName} ${person.lastName}`,
      );
      fetchAllPeople();
    } catch (e) {
      setErrorMessage(e.message);
    }
  };

  useEffect(() => {
    fetchAllPeople();
  }, []);

  return (
    <div>
      {showConfirmDelete && (
        <ConfirmDeleteModal
          setShowConfirmDelete={setShowConfirmDelete}
          itemToDelete={personToDelete}
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
              placeholder="Name"
              className="m-2 mt-0"
              value={nameSearch}
              onChange={(e) => setNameSearch(e.target.value)}
            />
          </div>
        </div>

        <span className="col-span-5 p-2 pl-0 overflow-x-auto">
          {loading ? (
            <div className="flex items-center justify-center h-64">
              <CustomSpinner />
            </div>
          ) : (
            <PeopleTable
              people={people}
              responseType={responseType}
              handleDelete={handleDelete}
              nameSearch={nameSearch}
            />
          )}
        </span>
      </div>
    </div>
  );
};

export default AllPeople;
// ...existing code...