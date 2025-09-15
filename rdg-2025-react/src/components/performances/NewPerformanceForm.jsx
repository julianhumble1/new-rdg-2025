import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import PerformanceService from "../../services/PerformanceService.js";
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js";
import Select from "react-select";
import { Label, Textarea, TextInput } from "flowbite-react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import ContentCard from "../ui/ContentCard.jsx";

const NewPerformanceForm = () => {
  const navigate = useNavigate();

  const [production, setProduction] = useState(null);
  const [venue, setVenue] = useState(null);
  const [festival, setFestival] = useState(null);
  const [performanceTime, setPerformanceTime] = useState(new Date());
  const [description, setDescription] = useState("");
  const [standardPrice, setStandardPrice] = useState("");
  const [concessionPrice, setConcessionPrice] = useState("");
  const [boxOffice, setBoxOffice] = useState("");

  const [productionOptions, setProductionOptions] = useState([]);
  const [venueOptions, setVenueOptions] = useState([]);
  const [festivalOptions, setFestivalOptions] = useState([]);

  const [descriptionLength, setDescriptionLength] = useState(0);

  const [failMessage, setFailMessage] = useState("");

  useEffect(() => {
    const setOptions = async () => {
      try {
        setProductionOptions(
          await FetchValueOptionsHelper.fetchProductionOptions(),
        );
        setVenueOptions(await FetchValueOptionsHelper.fetchVenueOptions());
        setFestivalOptions(
          await FetchValueOptionsHelper.fetchFestivalOptions(),
        );
      } catch (e) {
        setFailMessage(e.message);
      }
    };
    setOptions();
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      await PerformanceService.addNewPerformance(
        production.value,
        venue.value,
        festival ? festival.value : "",
        performanceTime,
        description,
        standardPrice,
        concessionPrice,
        boxOffice,
      );
      navigate(`/productions/${production.value}`);
    } catch (e) {
      setFailMessage(e.message);
    }
  };

  return (
    <ContentCard>
      <form className="flex flex-col gap-2 max-w-md" onSubmit={handleSubmit}>
        <div>
          <div className="mb-2 block">
            <Label value="Production (required)" />
          </div>
          <Select
            options={productionOptions}
            value={production}
            required
            onChange={(selectedOption) => {
              setProduction(selectedOption);
              selectedOption.venue
                ? setVenue({
                    label: selectedOption.venue.name,
                    value: selectedOption.venue.id,
                  })
                : setVenue(null);
            }}
            className="w-full text-sm"
            styles={{
              control: (baseStyles) => ({
                ...baseStyles,
                borderRadius: 8,
                padding: 1,
              }),
            }}
          />
          <div className="text-xs m-2">
            If production has an associated venue, this will be automatically
            filled below. Can be overridden if necessary.
          </div>
        </div>
        <div>
          <div className="mb-2 block">
            <Label value="Venue (required)" />
          </div>
          <Select
            options={venueOptions}
            onChange={setVenue}
            value={venue}
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
            onChange={setFestival}
            isClearable
            value={festival}
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
            <Label value="Date & Time (required)" />
          </div>
          <DatePicker
            selected={performanceTime}
            onChange={(time) => setPerformanceTime(time)}
            dateFormat="dd/MM/yyyy h:mm aa"
            showTimeSelect
            timeIntervals={15}
            popperPlacement="right"
            showIcon
            className="p-1 rounded border border-gray-300 text-sm"
          />
        </div>
        <div>
          <div className="mb-2 block italic">
            <Label
              value={`Description (max 2000 characters, current: ${descriptionLength})`}
            />
          </div>
          <Textarea
            placeholder="Divisional Final of the All-England Theatre Festival"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            onBlur={(e) => setDescriptionLength(e.target.value.length)}
            rows={4}
          />
        </div>
        <div>
          <Label value="Standard Price (£)" />
          <TextInput
            type="number"
            step={0.01}
            onBlur={(e) => {
              const value = parseFloat(e.target.value).toFixed(2);
              e.target.value = value;
            }}
            onChange={(e) => setStandardPrice(e.target.value)}
          />
        </div>
        <div>
          <Label value="Concession Price (£)" />
          <TextInput
            type="number"
            step={0.01}
            onBlur={(e) => {
              const value = parseFloat(e.target.value).toFixed(2);
              e.target.value = value;
            }}
            onChange={(e) => setConcessionPrice(e.target.value)}
          />
        </div>
        <div>
          <div className="mb-2 block">
            <Label value="Box Office" />
          </div>
          <TextInput
            placeholder="www.boxoffice.com"
            value={boxOffice}
            onChange={(e) => setBoxOffice(e.target.value)}
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

export default NewPerformanceForm;
