import { useState, useEffect } from "react";
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js";
import { Label, Textarea, TextInput } from "flowbite-react";
import Select from "react-select";
import DatePicker from "react-datepicker";
import EventService from "../../services/EventService.js";

const EditEventForm = ({ eventData, handleEdit, setEditMode }) => {
  const [venueOptions, setVenueOptions] = useState([]);

  const [name, setName] = useState(eventData?.name || "");
  const [description, setDescription] = useState(
    eventData?.description ? eventData.description : "",
  );
  const [dateTime, setDateTime] = useState(
    eventData?.dateTime ? new Date(eventData.dateTime) : null,
  );
  const [venue, setVenue] = useState(
    eventData?.venue
      ? { label: eventData.venue.name, value: eventData.venue.id }
      : { label: "None", value: 0 },
  );

  const [descriptionLength, setDescriptionLength] = useState(
    eventData?.description ? eventData.description.length : 0,
  );

  useEffect(() => {
    const getVenues = async () => {
      try {
        setVenueOptions(await FetchValueOptionsHelper.fetchVenueOptions());
      } catch (e) {
        return;
      }
    };
    getVenues();
  }, []);

  return (
    <form
      className="flex flex-col gap-2"
      onSubmit={async (event) => {
        event.preventDefault();

        await EventService.updateEvent(
          eventData.id,
          name,
          description,
          dateTime,
          venue.value,
        );
        setEditMode(false);
      }}
    >
      <div>
        <div className="mb-2 block italic">
          <Label value="Event Name (required)" />
        </div>
        <TextInput
          placeholder="Event name"
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
          defaultValue={venue}
          value={venue}
        />
      </div>

      <div>
        <div className="mb-2 block italic">
          <Label value="Date & Time" />
        </div>
        <DatePicker
          selected={dateTime}
          onChange={(time) => setDateTime(time)}
          dateFormat="dd/MM/yyyy h:mm aa"
          showTimeSelect
          timeIntervals={15}
          popperPlacement="right"
          showIcon
          className="p-1 rounded border border-gray-300 text-sm"
          isClearable
        />
      </div>

      <div>
        <div className="mb-2 block italic">
          <Label
            value={`Description (max 6000 characters, current: ${descriptionLength})`}
          />
        </div>
        <Textarea
          placeholder="Description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          onBlur={(e) => setDescriptionLength(e.target.value.length)}
          rows={4}
        />
      </div>

      <div className="grid grid-cols-2 justify-end px-2">
        <button
          type="button"
          className="text-sm hover:underline font-bold text-center col-span-1"
          onClick={() => setEditMode(false)}
        >
          Cancel Edit Mode
        </button>
        <button className="hover:underline bg-sky-900  p-2 py-1 rounded w-full text-white col-span-1 text-sm">
          Confirm Edit
        </button>
      </div>
    </form>
  );
};

export default EditEventForm;
