import { useState, useEffect } from "react";
import FestivalService from "../../services/FestivalService.js";
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js";
import MonthDateUtils from "../../utils/MonthDateUtils.js";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import { Label, Textarea, TextInput } from "flowbite-react";
import Select from "react-select";
import { Link, useNavigate } from "react-router-dom";

const NewFestivalForm = () => {
  const navigate = useNavigate();

  const currentYear = new Date().getFullYear();

  const [name, setName] = useState("");
  const [venue, setVenue] = useState({ label: "None", value: 0 });
  const [year, setYear] = useState({ value: currentYear, label: currentYear });
  const [month, setMonth] = useState({ value: 1, label: "January" });
  const [description, setDescription] = useState("");

  const [venueOptions, setVenueOptions] = useState([]);
  const [yearOptions, setYearOptions] = useState([]);

  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const [descriptionLength, setDescriptionLength] = useState(0);

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await FestivalService.createNewFestival(
        name,
        venue.value,
        year.value,
        month.value,
        description,
      );
      navigate(`/festivals/${response.data.festival.id}`);
    } catch (e) {
      setSuccessMessage("");
      setErrorMessage(e.message);
    }
  };

  useEffect(() => {
    const getVenueOptions = async () => {
      try {
        setVenueOptions(await FetchValueOptionsHelper.fetchVenueOptions());
      } catch (e) {
        setErrorMessage(e.message);
      }
    };
    getVenueOptions();
    setYearOptions(MonthDateUtils.getYearsArray);
  }, []);

  return (
    <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 rounded p-4 m-2 flex flex-col gap-2 shadow-md">
      <SuccessMessage message={successMessage} />
      <ErrorMessage message={errorMessage} />
      <form className="flex flex-col gap-2 max-w-md" onSubmit={handleSubmit}>
        <div>
          <div className="mb-2 block italic">
            <Label value="Festival Name (required)" />
          </div>
          <TextInput
            placeholder="Edinburgh Fringe"
            required
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>
        <div>
          <div className="mb-2 block italic">
            <Label value="Venue" />
          </div>
          <Select
            options={venueOptions}
            onChange={setVenue}
            className="w-full text-sm"
            styles={{
              control: (baseStyles) => ({
                ...baseStyles,
                borderRadius: 8,
                padding: 1,
              }),
            }}
            isClearable
          />
        </div>
        <div>
          <div className="mb-2 block italic">
            <Label value="Year (required)" />
          </div>
          <Select
            options={yearOptions}
            default={year}
            onChange={setYear}
            className="w-full text-sm"
            styles={{
              control: (baseStyles) => ({
                ...baseStyles,
                borderRadius: 8,
                padding: 1,
              }),
            }}
            value={year}
            required={true}
          />
        </div>
        <div>
          <div className=" mb-2 block italic">
            <Label value="Month" />
          </div>
          <Select
            options={MonthDateUtils.monthOptions}
            onChange={setMonth}
            className="w-full text-sm"
            styles={{
              control: (baseStyles) => ({
                ...baseStyles,
                borderRadius: 8,
                padding: 1,
              }),
            }}
            isClearable
            value={month}
          />
        </div>
        <div>
          <div className="mb-2 block italic">
            <Label
              value={`Description (max 2000 characters, current: ${descriptionLength})`}
            />
          </div>
          <Textarea
            placeholder="A drama festival that takes place every year in Woking. "
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            onBlur={(e) => setDescriptionLength(e.target.value.length)}
            rows={4}
          />
        </div>
        <div className="grid grid-cols-2 justify-end px-2">
          <Link
            to="/festivals"
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
  );
};

export default NewFestivalForm;
