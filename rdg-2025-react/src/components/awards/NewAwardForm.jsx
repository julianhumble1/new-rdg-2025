import { useState } from "react";
import Select from "react-select";
import { Label, TextInput } from "flowbite-react";
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js";
import { Link, useNavigate } from "react-router-dom";
import AwardService from "../../services/AwardService.js";
import ContentCard from "../common/ContentCard.jsx";
import { usePeople } from "../../hooks/usePeople.js";
import { useProductions } from "../../hooks/useProductions.js";
import { useFestivals } from "../../hooks/useFestivals.js";
import CustomSpinner from "../common/CustomSpinner.jsx";

const NewAwardForm = () => {
  const navigate = useNavigate();

  const { people } = usePeople();
  const { productions } = useProductions();
  const { festivals } = useFestivals();

  const peopleOptions = people.data
    ? FetchValueOptionsHelper.formatPersonOptions(people.data)
    : [];

  const productionOptions = productions.data
    ? FetchValueOptionsHelper.formatProductionOptions(productions.data)
    : [];

  const festivalOptions = festivals.data
    ? FetchValueOptionsHelper.formatFestivalOptions(festivals.data)
    : [];

  const [name, setName] = useState("");
  const [production, setProduction] = useState(null);
  const [person, setPerson] = useState(null);
  const [festival, setFestival] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await AwardService.createNewAward(
        name,
        production.value,
        person ? person.value : null,
        festival ? festival.value : null,
      );
      if (response.data.award.production) {
        navigate(`/archive/productions/${response.data.award.production.id}`);
      } else if (response.data.award.person) {
        navigate(`/archive/people/${response.data.award.person.id}`);
      } else if (response.data.award.festival) {
        navigate(`/archive/festivals/${response.data.award.festival.id}`);
      } else {
        navigate("/archive/dashboard");
      }
    } catch (e) {
      return;
    }
  };

  const dataLoading =
    people.isLoading || productions.isLoading || festivals.isLoading;

  if (dataLoading) return <CustomSpinner />;

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
            options={peopleOptions}
            value={person}
            isLoading={people.isLoading}
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
            isLoading={productions.isLoading}
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
            isLoading={festivals.isLoading}
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
            to="/archive/dashboard"
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
