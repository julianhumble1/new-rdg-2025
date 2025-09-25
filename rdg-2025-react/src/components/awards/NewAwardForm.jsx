import { useEffect, useState } from "react";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import Select from "react-select";
import { Label, TextInput } from "flowbite-react";
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js";
import { Link, useNavigate } from "react-router-dom";
import AwardService from "../../services/AwardService.js";
import ContentCard from "../common/ContentCard.jsx";

const NewAwardForm = () => {
  const navigate = useNavigate();

  const [productionOptions, setProductionOptions] = useState([]);
  const [personOptions, setPersonOptions] = useState([]);
  const [festivalOptions, setFestivalOptions] = useState([]);

  const [name, setName] = useState("");
  const [production, setProduction] = useState(null);
  const [person, setPerson] = useState(null);
  const [festival, setFestival] = useState(null);

  useEffect(() => {
    const setOptions = async () => {
      setProductionOptions(
        await FetchValueOptionsHelper.fetchProductionOptions(),
      );
      setPersonOptions(await FetchValueOptionsHelper.fetchPersonOptions());
      setFestivalOptions(await FetchValueOptionsHelper.fetchFestivalOptions());
    };
    try {
      setOptions();
    } catch (e) {
      return;
    }
  });

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await AwardService.createNewAward(
        name,
        production.value,
        person ? person.value : null,
        festival ? festival.value : null,
      );
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
      return;
    }
  };

  return (
    <ContentCard>
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
            <Label value="Festival (required)" />
          </div>
          <Select
            options={festivalOptions}
            value={festival}
            required
            onChange={setFestival}
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
    </ContentCard>
  );
};

export default NewAwardForm;
