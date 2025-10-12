import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import EventService from "../../services/EventService.js";
import ContentCard from "../common/ContentCard.jsx";
import { Label, Textarea, TextInput } from "flowbite-react";
import DatePicker from "react-datepicker";

const NewEventForm = () => {
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [dateTime, setDateTime] = useState(new Date());

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await EventService.addNewEvent(
        name,
        description,
        dateTime,
      );
      console.log(response)
      navigate(`/events/${response.data.event.id}`);
    } catch {
      return;
    }
  };

  return (
    <ContentCard>
      <form className="flex flex-col gap-2 max-w-md" onSubmit={(event) => handleSubmit(event)}>
        <div>
          <div className="mb-2 block">
            <Label value="Event Name (required)" />
          </div>
          <TextInput
            placeholder="Name"
            required
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>
          <div>
          <div className="mb-2 block">
            <Label value="Description" />
          </div>
          <Textarea
            placeholder="Description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            rows={4}
          />
        </div>

        <div>
          <div className="mb-2 block">
            <Label value="Date & Time (required)" />
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

export default NewEventForm;
