import { useEffect, useState } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import AwardService from "../../services/AwardService.js";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js";
import { Label, TextInput } from "flowbite-react";
import Select from "react-select";

const EditAwardForm = () => {
  const awardId = useParams().id;
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [production, setProduction] = useState(null);
  const [person, setPerson] = useState(null);
  const [festival, setFestival] = useState(null);

  const [productionOptions, setProductionOptions] = useState([]);
  const [personOptions, setPersonOptions] = useState([]);
  const [festivalOptions, setFestivalOptions] = useState([]);

  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    const setOptions = async () => {
      try {
        setProductionOptions(
          await FetchValueOptionsHelper.fetchProductionOptions(),
        );
        setPersonOptions(await FetchValueOptionsHelper.fetchPersonOptions());
        setFestivalOptions(
          await FetchValueOptionsHelper.fetchFestivalOptions(),
        );
      } catch (e) {
        setErrorMessage(e.message);
      }
    };
    setOptions();
  }, []);

  useEffect(() => {
    const getAwardById = async () => {
      try {
        const response = await AwardService.getAwardById(awardId);
        console.log(response);
        setName(response.data.award.name);
        setProduction(
          response.data.award.production
            ? {
                value: response.data.award.production.id,
                label: response.data.award.production.name,
              }
            : null,
        );
        setPerson(
          response.data.award.person
            ? {
                value: response.data.award.person.id,
                label: `${response.data.award.person.firstName} ${response.data.award.person.lastName}`,
              }
            : null,
        );
        setFestival(
          response.data.award.festival
            ? {
                value: response.data.award.festival.id,
                label: `${response.data.award.festival.name} (${response.data.award.festival.year})`,
              }
            : null,
        );
      } catch (e) {
        setErrorMessage(e.message);
      }
    };
    getAwardById();
  }, [awardId]);

  const handleSubmit = async (event) => {
    event.preventDefault();

    // Validate that production is selected
    if (!production) {
      setErrorMessage("Production is required");
      return;
    }

    try {
      const response = await AwardService.updateAward(
        awardId,
        name,
        production.value,
        person ? person.value : null,
        festival ? festival.value : null,
      );
      setSuccessMessage("Successfully updated award!");
      // Navigate back to the appropriate page based on the award's associations
      if (response.data.award.production) {
        navigate(`/productions/${response.data.award.production.id}`);
      } else if (response.data.award.person) {
        navigate(`/people/${response.data.award.person.id}`);
      } else if (response.data.award.festival) {
        navigate(`/festivals/${response.data.award.festival.id}`);
      } else {
        navigate("/dashboard");
      }
    } catch (e) {
      setErrorMessage(e.message);
    }
  };

  return (
    <div>
      <SuccessMessage message={successMessage} />
      <ErrorMessage message={errorMessage} />
      <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 rounded p-4 m-2 flex flex-col gap-2 shadow-md">
        <form className="flex flex-col gap-2 max-w-md" onSubmit={handleSubmit}>
          <div>
            <div className="mb-2 block italic">
              <Label value="Award Name (required)" />
            </div>
            <TextInput
              placeholder="Best Actor"
              required
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </div>
          <div>
            <div className="mb-2 block">
              <Label value="Person" />
            </div>
            <Select
              options={personOptions}
              value={person}
              onChange={setPerson}
              isClearable
              className="w-full text-sm"
              styles={{
                control: (baseStyles) => ({
                  ...baseStyles,
                  borderRadius: 8,
                  padding: 1,
                }),
              }}
            />
          </div>
          <div>
            <div className="mb-2 block">
              <Label value="Production (required)" />
            </div>
            <Select
              options={productionOptions}
              value={production}
              onChange={setProduction}
              required
              className="w-full text-sm"
              styles={{
                control: (baseStyles) => ({
                  ...baseStyles,
                  borderRadius: 8,
                  padding: 1,
                }),
              }}
            />
          </div>
          <div>
            <div className="mb-2 block">
              <Label value="Festival" />
            </div>
            <Select
              options={festivalOptions}
              value={festival}
              onChange={setFestival}
              isClearable
              className="w-full text-sm"
              styles={{
                control: (baseStyles) => ({
                  ...baseStyles,
                  borderRadius: 8,
                  padding: 1,
                }),
              }}
            />
          </div>
          <div className="grid grid-cols-2 justify-end px-2">
            <Link
              to="/dashboard"
              className="text-sm hover:underline font-bold text-center col-span-1 my-auto"
            >
              Cancel
            </Link>
            <button className="hover:underline bg-sky-900  p-2 py-1 rounded w-full text-white col-span-1 text-sm">
              Submit
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditAwardForm;
